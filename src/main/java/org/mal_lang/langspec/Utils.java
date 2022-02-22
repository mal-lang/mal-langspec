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

package org.mal_lang.langspec;

import static java.util.Objects.requireNonNull;

import jakarta.json.Json;
import jakarta.json.stream.JsonParsingException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility class containing static helper functions.
 *
 * @since 1.0.0
 */
public final class Utils {
  private Utils() {}

  private static boolean isLetter(char ch) {
    return ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z';
  }

  private static boolean isDigit(char ch) {
    return ch >= '0' && ch <= '9';
  }

  private static boolean isIdentifierStart(char ch) {
    return isLetter(ch) || ch == '_';
  }

  private static boolean isIdentifierPart(char ch) {
    return isLetter(ch) || isDigit(ch) || ch == '_';
  }

  /**
   * Returns whether {@code name} is a valid identifier.
   *
   * @param name the {@link java.lang.CharSequence} to check
   * @return whether {@code name} is a valid identifier
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @since 1.0.0
   */
  public static boolean isIdentifier(CharSequence name) {
    requireNonNull(name);
    if (name.length() == 0) {
      return false;
    }
    if (!isIdentifierStart(name.charAt(0))) {
      return false;
    }
    for (int i = 1; i < name.length(); i++) {
      if (!isIdentifierPart(name.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks that {@code name} is a valid identifier.
   *
   * @param name the {@link java.lang.CharSequence} to check
   * @return {@code name} as a {@link java.lang.String}
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not a valid identifier
   * @since 1.0.0
   */
  public static String requireIdentifier(CharSequence name) {
    if (!Utils.isIdentifier(name)) {
      throw new IllegalArgumentException(String.format("\"%s\" is not a valid identifier", name));
    }
    return name.toString();
  }

  /**
   * Returns an input stream from which the langspec schema can be read.
   *
   * @return an input stream from which the langspec schema can be read
   * @since 1.0.0
   */
  public static InputStream getLangSpecSchema() {
    var in = Lang.class.getResourceAsStream("lang.schema.json");
    if (in == null) {
      throw new RuntimeException("Failed to get resource \"lang.schema.json\"");
    }
    return in;
  }

  /**
   * Returns the format version as a {@link java.lang.String}.
   *
   * @return the format version as a {@link java.lang.String}
   * @since 1.0.0
   */
  public static String getFormatVersion() {
    try (var schemaIn = Utils.getLangSpecSchema();
        var reader =
            Json.createReaderFactory(null).createReader(schemaIn, StandardCharsets.UTF_8)) {
      return reader
          .readObject()
          .getJsonObject("properties")
          .getJsonObject("formatVersion")
          .getString("const");
    } catch (JsonParsingException e) {
      throw new RuntimeException("Failed to parse \"lang.schema.json\"", e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
