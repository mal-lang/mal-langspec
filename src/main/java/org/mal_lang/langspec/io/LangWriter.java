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

import jakarta.json.Json;
import java.io.Closeable;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.leadpony.joy.api.JsonGenerator;
import org.mal_lang.langspec.Lang;

/**
 * Writes {@link org.mal_lang.langspec.Lang} objects into {@code .mar} files.
 *
 * @since 1.0.0
 */
public final class LangWriter implements Closeable {
  private final OutputStream out;
  private boolean isWritten = false;
  private boolean isClosed = false;

  /**
   * Constructs a new {@code LangWriter} object.
   *
   * @param out an output stream to which a {@code .mar} file is to be written
   * @throws java.lang.NullPointerException if {@code out} is {@code null}
   * @since 1.0.0
   */
  public LangWriter(OutputStream out) {
    this.out = requireNonNull(out);
  }

  private static void writeLangSpec(ZipOutputStream zipOut, Lang lang) throws IOException {
    var jsonWriterFactory =
        Json.createWriterFactory(
            Map.ofEntries(
                Map.entry(JsonGenerator.PRETTY_PRINTING, true),
                Map.entry(JsonGenerator.INDENTATION_SIZE, 2)));
    var wrappedOut =
        new FilterOutputStream(zipOut) {
          @Override
          public void close() throws IOException {}
        };

    zipOut.putNextEntry(new ZipEntry("langspec.json"));
    try (var jsonWriter = jsonWriterFactory.createWriter(wrappedOut, StandardCharsets.UTF_8)) {
      jsonWriter.writeObject(lang.toJson());
    }
    zipOut.closeEntry();
  }

  /**
   * Writes the specified {@link org.mal_lang.langspec.Lang} object to the output source. This
   * method needs to be called only once for a writer instance.
   *
   * @param lang {@link org.mal_lang.langspec.Lang} object that is to be written to the output
   *     source
   * @throws java.lang.NullPointerException if {@code lang} is {@code null}
   * @throws java.io.IOException if an I/O error occurs
   * @throws java.lang.IllegalStateException if {@code write} or {@code close} method is already
   *     called
   * @since 1.0.0
   */
  public void write(Lang lang) throws IOException {
    if (this.isWritten) {
      throw new IllegalStateException("write method is already called");
    }
    if (this.isClosed) {
      throw new IllegalStateException("close method is already called");
    }
    requireNonNull(lang);
    try (var zipOut = new ZipOutputStream(this.out, StandardCharsets.UTF_8)) {
      LangWriter.writeLangSpec(zipOut, lang);
      zipOut.putNextEntry(new ZipEntry("icons/"));
      zipOut.closeEntry();
      for (var asset : lang.getAssets()) {
        if (asset.hasLocalSvgIcon()) {
          zipOut.putNextEntry(new ZipEntry(String.format("icons/%s.svg", asset.getName())));
          zipOut.write(asset.getLocalSvgIcon());
          zipOut.closeEntry();
        }
        if (asset.hasLocalPngIcon()) {
          zipOut.putNextEntry(new ZipEntry(String.format("icons/%s.png", asset.getName())));
          zipOut.write(asset.getLocalPngIcon());
          zipOut.closeEntry();
        }
      }
      if (lang.hasLicense()) {
        zipOut.putNextEntry(new ZipEntry("LICENSE"));
        zipOut.write(lang.getLicense().getBytes(StandardCharsets.UTF_8));
        zipOut.closeEntry();
      }
      if (lang.hasNotice()) {
        zipOut.putNextEntry(new ZipEntry("NOTICE"));
        zipOut.write(lang.getNotice().getBytes(StandardCharsets.UTF_8));
        zipOut.closeEntry();
      }
    } finally {
      this.isWritten = true;
    }
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
      this.out.close();
    } finally {
      this.isClosed = true;
    }
  }
}
