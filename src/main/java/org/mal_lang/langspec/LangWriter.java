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
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.leadpony.joy.api.JsonGenerator;

/** Writes {@link Lang} objects to {@code .mar} files. */
public final class LangWriter {
  private final OutputStream out;

  /**
   * Constructs a new {@code LangWriter} object.
   *
   * @param out an output stream of a {@code .mar} file
   * @throws NullPointerException if {@code out} is {@code null}
   */
  public LangWriter(OutputStream out) {
    checkNotNull(out);
    this.out = out;
  }

  private void writeDirectory(ZipOutputStream zipOut, String name) throws IOException {
    zipOut.putNextEntry(new ZipEntry(name));
    zipOut.closeEntry();
  }

  private void writeByteArray(ZipOutputStream zipOut, String name, byte[] bytes)
      throws IOException {
    zipOut.putNextEntry(new ZipEntry(name));
    zipOut.write(bytes);
    zipOut.closeEntry();
  }

  private void writeLangSpec(ZipOutputStream zipOut, Lang lang) throws IOException {
    var jsonWriterFactory =
        Json.createWriterFactory(
            Map.ofEntries(
                Map.entry(JsonGenerator.PRETTY_PRINTING, true),
                Map.entry(JsonGenerator.INDENTATION_SIZE, 2)));
    var jsonWriter = jsonWriterFactory.createWriter(zipOut, StandardCharsets.UTF_8);
    try {
      zipOut.putNextEntry(new ZipEntry("langspec.json"));
      jsonWriter.writeObject(lang.toJson());
      zipOut.closeEntry();
    } catch (JsonException e) {
      var cause = e.getCause();
      if (cause == null || !(cause instanceof IOException)) {
        throw new RuntimeException("JsonException did not contain an IOException cause");
      }
      throw (IOException) cause;
    }
  }

  private void writeIcons(ZipOutputStream zipOut, Lang lang) throws IOException {
    writeDirectory(zipOut, "icons/");
    for (var asset : lang.getAssets()) {
      if (asset.hasLocalSvgIcon()) {
        writeByteArray(
            zipOut, String.format("icons/%s.svg", asset.getName()), asset.getLocalSvgIcon());
      }
      if (asset.hasLocalPngIcon()) {
        writeByteArray(
            zipOut, String.format("icons/%s.png", asset.getName()), asset.getLocalPngIcon());
      }
    }
  }

  private void writeString(ZipOutputStream zipOut, String name, String str) throws IOException {
    writeByteArray(zipOut, name, str.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Writes the specified {@link Lang} object to the output stream.
   *
   * @param lang the {@link Lang} object to write
   * @throws IOException if an I/O error has occurred
   * @throws NullPointerException if {@code lang} is {@code null}
   */
  public void write(Lang lang) throws IOException {
    checkNotNull(lang);
    try (var zipOut = new ZipOutputStream(out)) {
      writeLangSpec(zipOut, lang);
      writeIcons(zipOut, lang);
      if (lang.hasLicense()) {
        writeString(zipOut, "LICENSE", lang.getLicense());
      }
      if (lang.hasNotice()) {
        writeString(zipOut, "NOTICE", lang.getNotice());
      }
    }
  }
}
