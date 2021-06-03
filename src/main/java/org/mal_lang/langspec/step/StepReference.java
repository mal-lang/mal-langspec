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

package org.mal_lang.langspec.step;

import org.mal_lang.langspec.Asset;

/**
 * Immutable class representing a reference step in a MAL language.
 *
 * @since 1.0.0
 */
public abstract class StepReference extends StepExpression {
  StepReference(Asset sourceAsset, Asset targetAsset) {
    super(sourceAsset, targetAsset);
  }
}
