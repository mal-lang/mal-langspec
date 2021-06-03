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

package org.mal_lang.test.langspec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.mal_lang.langspec.io.LangReader;

class TestInvalidMar {
  @Test
  void testEmpty() {
    try (var in = this.getClass().getResourceAsStream("empty.mar");
        var reader = new LangReader(in)) {
      var e = assertThrows(IOException.class, () -> reader.read());
      assertEquals(IOException.class, e.getClass());
      assertEquals("File \"langspec.json\" not found", e.getMessage());
    } catch (IOException e) {
      fail(e);
    }
  }

  @Test
  void testNotZip() {
    try (var in = this.getClass().getResourceAsStream("not-zip.mar");
        var reader = new LangReader(in)) {
      var e = assertThrows(IOException.class, () -> reader.read());
      assertEquals(IOException.class, e.getClass());
      assertEquals("File \"langspec.json\" not found", e.getMessage());
    } catch (IOException e) {
      fail(e);
    }
  }

  @Test
  void testRandom() {
    try (var in = this.getClass().getResourceAsStream("random.mar");
        var reader = new LangReader(in)) {
      var e = assertThrows(IOException.class, () -> reader.read());
      assertEquals(IOException.class, e.getClass());
      assertEquals("File \"langspec.json\" not found", e.getMessage());
    } catch (IOException e) {
      fail(e);
    }
  }

  @Test
  void testNotJson() {
    try (var in = this.getClass().getResourceAsStream("not-json.mar");
        var reader = new LangReader(in)) {
      var e = assertThrows(IOException.class, () -> reader.read());
      assertEquals(IOException.class, e.getClass());
      assertEquals("Failed to parse \"langspec.json\"", e.getMessage());
    } catch (IOException e) {
      fail(e);
    }
  }

  @Test
  void testInvalid() {
    try (var in = this.getClass().getResourceAsStream("invalid.mar");
        var reader = new LangReader(in)) {
      var e = assertThrows(IOException.class, () -> reader.read());
      assertEquals(IOException.class, e.getClass());
      assertEquals("Failed to validate \"langspec.json\"", e.getMessage());
    } catch (IOException e) {
      fail(e);
    }
  }
}
