/*
 * Copyright 2020-2021 Foreseeti AB <https://foreseeti.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mal_lang.langspec;

import static org.mal_lang.langspec.Utils.checkNotNull;

import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParsingException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

/** Reads {@link Lang} objects from {@code .mar} files. */
public final class LangReader {
  private final InputStream in;
  private JsonObject langSpec = null;
  private final Map<String, byte[]> icons = new LinkedHashMap<>();
  private String license = null;
  private String notice = null;

  /**
   * Constructs a new {@code LangReader} object.
   *
   * @param in an input stream of a {@code .mar} file
   * @throws NullPointerException if {@code in} is {@code null}
   */
  public LangReader(InputStream in) {
    checkNotNull(in);
    this.in = in;
  }

  private void readLangSpec(ZipInputStream zipIn) throws IOException {
    var jsonReaderFactory = Json.createReaderFactory(null);
    var jsonReader = jsonReaderFactory.createReader(zipIn, StandardCharsets.UTF_8);
    try {
      this.langSpec = jsonReader.readObject();
    } catch (JsonParsingException e) {
      throw new IllegalArgumentException("Failed to parse \"langspec.json\" as a JSON object", e);
    } catch (JsonException e) {
      var cause = e.getCause();
      if (cause == null || !(cause instanceof IOException)) {
        throw new RuntimeException("JsonException did not contain an IOException cause");
      }
      throw (IOException) cause;
    }
  }

  private void readIcon(ZipInputStream zipIn, String name) throws IOException {
    // Remove "lang/" prefix
    var fileName = name.substring(6);
    if (!fileName.endsWith(".png") && !fileName.endsWith(".svg")) {
      return;
    }
    // Remove ".svg" or ".png" suffix
    var assetName = fileName.substring(0, fileName.length() - 4);
    if (!Utils.isIdentifier(assetName)) {
      return;
    }
    this.icons.put(fileName, zipIn.readAllBytes());
  }

  private String readString(ZipInputStream zipIn) throws IOException {
    return new String(zipIn.readAllBytes(), StandardCharsets.UTF_8);
  }

  /**
   * Reads a {@link Lang} object from the input stream.
   *
   * @return a {@link Lang} object from the input stream
   * @throws IOException if an I/O error has occurred
   * @throws IllegalArgumentException if the input stream contains invalid data
   */
  public Lang read() throws IOException {
    try (var zipIn = new ZipInputStream(in)) {
      for (var zipEntry = zipIn.getNextEntry(); zipEntry != null; zipEntry = zipIn.getNextEntry()) {
        if (!zipEntry.isDirectory()) {
          var name = zipEntry.getName();
          if (name.equals("langspec.json")) {
            readLangSpec(zipIn);
          } else if (name.startsWith("icons/")) {
            readIcon(zipIn, name);
          } else if (name.equals("LICENSE")) {
            this.license = readString(zipIn);
          } else if (name.equals("NOTICE")) {
            this.notice = readString(zipIn);
          }
        }
        zipIn.closeEntry();
      }
    }
    if (this.langSpec == null) {
      throw new IllegalArgumentException("File \"langspec.json\" not found");
    }
    return Lang.fromJson(this.langSpec, this.icons, this.license, this.notice);
  }
}
