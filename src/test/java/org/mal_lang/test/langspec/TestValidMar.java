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

package org.mal_lang.test.langspec;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import org.junit.jupiter.api.Test;
import org.mal_lang.langspec.io.LangReader;
import org.mal_lang.langspec.io.LangWriter;

class TestValidMar {
  @Test
  void testValid() {
    try (var in = this.getClass().getResourceAsStream("valid.mar");
        var reader = new LangReader(in)) {
      var lang = reader.read();
      assertTrue(lang.hasDefine("id"));
      assertTrue(lang.hasDefine("version"));
      assertEquals("org.mal-lang.valid", lang.getDefine("id"));
      assertEquals("1.0.0", lang.getDefine("version"));
      assertEquals(2, lang.getDefines().size());
      assertTrue(lang.getCategories().isEmpty());
      assertTrue(lang.getAssets().isEmpty());
      assertTrue(lang.getAssociations().isEmpty());
      assertFalse(lang.hasLicense());
      assertFalse(lang.hasNotice());
      var jsonLang =
          Json.createObjectBuilder()
              .add("formatVersion", "1.0.0")
              .add(
                  "defines",
                  Json.createObjectBuilder()
                      .add("id", "org.mal-lang.valid")
                      .add("version", "1.0.0"))
              .add("categories", JsonValue.EMPTY_JSON_ARRAY)
              .add("assets", JsonValue.EMPTY_JSON_ARRAY)
              .add("associations", JsonValue.EMPTY_JSON_ARRAY)
              .build();
      assertEquals(jsonLang, lang.toJson());
    } catch (IOException e) {
      fail(e);
    }
  }

  @Test
  void testTest() {
    try (var in = this.getClass().getResourceAsStream("test.mar");
        var reader = new LangReader(in)) {
      var lang = reader.read();
      assertTrue(lang.hasDefine("id"));
      assertTrue(lang.hasDefine("version"));
      assertEquals("org.mal-lang.test", lang.getDefine("id"));
      assertEquals("1.0.0", lang.getDefine("version"));
      assertEquals(2, lang.getDefines().size());
      assertEquals(4, lang.getCategories().size());
      assertEquals(14, lang.getAssets().size());
      assertEquals(5, lang.getAssociations().size());
      assertTrue(lang.hasLicense());
      assertEquals("license\n", lang.getLicense());
      assertTrue(lang.hasNotice());
      assertEquals("notice\n", lang.getNotice());
      try (var jsonIn = this.getClass().getResourceAsStream("test.json");
          var jsonReader =
              Json.createReaderFactory(null).createReader(jsonIn, StandardCharsets.UTF_8)) {
        var jsonLang = jsonReader.readObject();
        assertEquals(jsonLang, lang.toJson());
      } catch (IOException e) {
        fail(e);
      }
      var tmpPath = Files.createTempFile("test-", ".mar");
      try (var out =
              Files.newOutputStream(
                  tmpPath, StandardOpenOption.DELETE_ON_CLOSE, StandardOpenOption.WRITE);
          var writer = new LangWriter(out)) {
        assertDoesNotThrow(() -> writer.write(lang));
      } catch (IOException e) {
        fail(e);
      }
    } catch (IOException e) {
      fail(e);
    }
  }
}
