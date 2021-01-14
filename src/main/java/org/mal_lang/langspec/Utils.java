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

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidatingException;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.ProblemHandler;

/** Utility class containing static helper functions. */
public final class Utils {
  private static final String LANG_SCHEMA_JSON = "lang.schema.json";
  private static final JsonValidationService JSON_VALIDATION_SERVICE =
      JsonValidationService.newInstance();

  private Utils() {}

  /**
   * Checks that {@code objs} and all object in {@code objs} are not {@code null}.
   *
   * @param objs the objects to check
   * @throws NullPointerException if {@code objs} or any object in {@code objs} is {@code null}
   */
  public static void checkNotNull(Object... objs) {
    if (objs == null) {
      throw new NullPointerException();
    }
    for (var obj : objs) {
      if (obj == null) {
        throw new NullPointerException();
      }
    }
  }

  /**
   * Checks that {@code objs} and all object in {@code objs} are not {@code null}.
   *
   * @param objs the objects to check
   * @throws NullPointerException if {@code objs} or any object in {@code objs} is {@code null}
   */
  public static void checkNotNullList(List<?> objs) {
    if (objs == null) {
      throw new NullPointerException();
    }
    for (var obj : objs) {
      if (obj == null) {
        throw new NullPointerException();
      }
    }
  }

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
   * Returns whether {@code str} is a valid identifier.
   *
   * @param str the string to check
   * @return whether {@code str} is a valid identifier
   * @throws NullPointerException if {@code str} is {@code null}
   */
  public static boolean isIdentifier(String str) {
    checkNotNull(str);
    if (str.length() < 1 || !isIdentifierStart(str.charAt(0))) {
      return false;
    }
    for (int i = 1; i < str.length(); i++) {
      if (!isIdentifierPart(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks that all strings in {@code strs} are valid identifiers.
   *
   * @param strs the strings to check
   * @throws NullPointerException if {@code strs} or any string in {@code strs} is {@code null}
   * @throws IllegalArgumentException if any string in {@code strs} is not a valid identifier
   */
  public static void checkIdentifier(String... strs) {
    checkNotNull((Object[]) strs);
    for (var str : strs) {
      if (!isIdentifier(str)) {
        throw new IllegalArgumentException(String.format("\"%s\" is not a valid identifier", str));
      }
    }
  }

  private static InputStream getResourceAsStream(String name) throws FileNotFoundException {
    var in = Utils.class.getResourceAsStream(name);
    if (in == null) {
      throw new FileNotFoundException(String.format("%s (No such file or directory)", name));
    }
    return in;
  }

  private static InputStream getLangSchemaJsonAsStream() throws FileNotFoundException {
    return getResourceAsStream(LANG_SCHEMA_JSON);
  }

  private static JsonObject getLangSchemaJsonAsJsonObject() {
    try (var in = getLangSchemaJsonAsStream()) {
      var jsonReaderFactory = Json.createReaderFactory(null);
      try (var jsonReader = jsonReaderFactory.createReader(in, StandardCharsets.UTF_8)) {
        return jsonReader.readObject();
      } catch (JsonException e) {
        throw new RuntimeException("Failed to read schema as JSON", e);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to read schema", e);
    }
  }

  private static JsonSchema getLangSchemaJsonAsJsonSchema() {
    try (var in = getLangSchemaJsonAsStream()) {
      return JSON_VALIDATION_SERVICE.readSchema(in, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read schema", e);
    } catch (JsonException e) {
      throw new RuntimeException("Failed to read schema as JSON", e);
    }
  }

  private static JsonObject getJsonObject(JsonObject object, String name) {
    if (!object.containsKey(name)) {
      throw new RuntimeException("Invalid schema");
    }
    try {
      return object.getJsonObject(name);
    } catch (ClassCastException e) {
      throw new RuntimeException("Invalid schema", e);
    }
  }

  private static JsonArray getJsonArray(JsonObject object, String name) {
    if (!object.containsKey(name)) {
      throw new RuntimeException("Invalid schema");
    }
    try {
      return object.getJsonArray(name);
    } catch (ClassCastException e) {
      throw new RuntimeException("Invalid schema", e);
    }
  }

  private static String getArraySingleton(JsonArray array) {
    if (array.size() != 1) {
      throw new RuntimeException("Invalid schema");
    }
    try {
      return array.getString(0);
    } catch (ClassCastException e) {
      throw new RuntimeException("Invalid schema", e);
    }
  }

  /**
   * Returns the format version from the schema.
   *
   * @return the format version from the schema
   */
  public static String getFormatVersion() {
    var langSchemaJson = getLangSchemaJsonAsJsonObject();
    return getArraySingleton(
        getJsonArray(
            getJsonObject(getJsonObject(langSchemaJson, "properties"), "formatVersion"), "enum"));
  }

  /**
   * Returns whether {@code jsonLang} conforms to the schema.
   *
   * @param jsonLang a JSON representation of a MAL language
   * @return whether {@code jsonLang} conforms to the schema
   * @throws NullPointerException if {@code jsonLang} is {@code null}
   */
  public static boolean isValidJsonLang(JsonObject jsonLang) {
    checkNotNull(jsonLang);
    var jsonSchema = getLangSchemaJsonAsJsonSchema();
    var jsonParserFactory = Json.createParserFactory(null);
    try (var jsonParser = jsonParserFactory.createParser(jsonLang)) {
      try (var jsonReader =
          JSON_VALIDATION_SERVICE.createReader(jsonParser, jsonSchema, ProblemHandler.throwing())) {
        jsonReader.readObject();
        return true;
      } catch (JsonValidatingException e) {
        return false;
      } catch (JsonException e) {
        throw new RuntimeException("Failed to create JsonReader", e);
      }
    } catch (JsonException e) {
      throw new RuntimeException("Failed to create JsonParser", e);
    }
  }
}
