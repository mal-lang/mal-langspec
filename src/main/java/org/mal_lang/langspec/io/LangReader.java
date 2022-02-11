/*
 * Copyright 2020-2022 Foreseeti AB <https://foreseeti.com>
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

package org.mal_lang.langspec.io;

import static java.util.Objects.requireNonNull;

import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParsingException;
import java.io.Closeable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;
import org.leadpony.justify.api.JsonValidatingException;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.ProblemHandler;
import org.mal_lang.langspec.Lang;
import org.mal_lang.langspec.Utils;
import org.mal_lang.langspec.builders.LangBuilder;

/**
 * Reads {@code .mar} files into {@link org.mal_lang.langspec.Lang} objects.
 *
 * @since 1.0.0
 */
public final class LangReader implements Closeable {
  private final InputStream in;
  private boolean isRead = false;
  private boolean isClosed = false;
  private JsonObject langSpec;
  private final Map<String, byte[]> svgIcons = new LinkedHashMap<>();
  private final Map<String, byte[]> pngIcons = new LinkedHashMap<>();
  private String license;
  private String notice;

  /**
   * Constructs a new {@code LangReader} object.
   *
   * @param in an input stream from which a {@code .mar} file is to be read
   * @throws java.lang.NullPointerException if {@code in} is {@code null}
   * @since 1.0.0
   */
  public LangReader(InputStream in) {
    this.in = requireNonNull(in);
  }

  private static JsonObject readLangSpec(ZipInputStream zipIn) throws IOException {
    try (var schemaIn = Utils.getLangSpecSchema()) {
      var service = JsonValidationService.newInstance();
      var schema = service.readSchema(schemaIn, StandardCharsets.UTF_8);
      var wrappedIn =
          new FilterInputStream(zipIn) {
            @Override
            public void close() throws IOException {}
          };

      try (var reader =
          service.createReader(
              wrappedIn, StandardCharsets.UTF_8, schema, ProblemHandler.throwing())) {
        return reader.readObject();
      } catch (JsonValidatingException e) {
        throw new IOException("Failed to validate \"langspec.json\"", e);
      } catch (JsonParsingException e) {
        throw new IOException("Failed to parse \"langspec.json\"", e);
      }
    }
  }

  /**
   * Returns a {@link org.mal_lang.langspec.Lang} object that is represented in the input source.
   * This method needs to be called only once for a reader instance.
   *
   * @return a {@link org.mal_lang.langspec.Lang} object
   * @throws java.io.IOException if an I/O error occurs
   * @throws java.lang.IllegalStateException if {@code read} or {@code close} method is already
   *     called
   * @since 1.0.0
   */
  public Lang read() throws IOException {
    return this.read(true, true);
  }

  /**
   * Returns a {@link org.mal_lang.langspec.Lang} object that is represented in the input source.
   * This method needs to be called only once for a reader instance.
   *
   * @param readIcons whether icons should be read
   * @param readLicense whether license and notice should be read
   * @return a {@link org.mal_lang.langspec.Lang} object
   * @throws java.io.IOException if an I/O error occurs
   * @throws java.lang.IllegalStateException if {@code read} or {@code close} method is already
   *     called
   * @since 1.0.0
   */
  public Lang read(boolean readIcons, boolean readLicense) throws IOException {
    if (this.isRead) {
      throw new IllegalStateException("read method is already called");
    }
    if (this.isClosed) {
      throw new IllegalStateException("close method is already called");
    }
    try (var zipIn = new ZipInputStream(this.in, StandardCharsets.UTF_8)) {
      for (var zipEntry = zipIn.getNextEntry(); zipEntry != null; zipEntry = zipIn.getNextEntry()) {
        if (!zipEntry.isDirectory()) {
          var name = zipEntry.getName();
          if (name.equals("langspec.json")) {
            this.langSpec = LangReader.readLangSpec(zipIn);
          } else if (readIcons && name.startsWith("icons/")) {
            if (name.endsWith(".svg")) {
              var assetName = name.substring("icons/".length(), name.length() - ".svg".length());
              if (Utils.isIdentifier(assetName)) {
                this.svgIcons.put(assetName, zipIn.readAllBytes());
              }
            } else if (name.endsWith(".png")) {
              var assetName = name.substring("icons/".length(), name.length() - ".png".length());
              if (Utils.isIdentifier(assetName)) {
                this.pngIcons.put(assetName, zipIn.readAllBytes());
              }
            }
          } else if (readLicense && name.equals("LICENSE")) {
            this.license = new String(zipIn.readAllBytes(), StandardCharsets.UTF_8);
          } else if (readLicense && name.equals("NOTICE")) {
            this.notice = new String(zipIn.readAllBytes(), StandardCharsets.UTF_8);
          }
        }
      }
    } finally {
      this.isRead = true;
    }
    if (this.langSpec == null) {
      throw new IOException("File \"langspec.json\" not found");
    }
    return Lang.fromBuilder(
        LangBuilder.fromJson(
            this.langSpec, this.svgIcons, this.pngIcons, this.license, this.notice));
  }

  /**
   * Closes this stream and releases any system resources associated with it. If the stream is
   * already closed then invoking this method has no effect.
   *
   * @throws java.io.IOException if an I/O error occurs
   * @since 1.0.0
   */
  @Override
  public void close() throws IOException {
    if (this.isClosed) {
      return;
    }
    try {
      this.in.close();
    } finally {
      this.isClosed = true;
    }
  }
}
