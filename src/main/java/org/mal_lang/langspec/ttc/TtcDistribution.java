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

package org.mal_lang.langspec.ttc;

import static java.util.Objects.requireNonNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Enum representing a TTC distribution.
 *
 * @since 1.0.0
 */
public enum TtcDistribution {
  /**
   * Enum constant representing a bernoulli distribution.
   *
   * <p>Arguments: {@code [probability]}, where 0 ≤ {@code probability} ≤ 1
   *
   * @since 1.0.0
   */
  BERNOULLI("Bernoulli") {
    @Override
    public void validateArguments(double... arguments) {
      requireFinite(requireArgumentsSize(requireNonNull(arguments), 1));
      double probability = arguments[0];
      requireZeroToOne(probability);
    }

    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      double probability = arguments[0];
      return probability < 0.5 ? 0.0 : Double.MAX_VALUE;
    }

    @Override
    public double getMeanProbability(double... arguments) {
      this.validateArguments(arguments);
      double probability = arguments[0];
      return probability < 0.5 ? 0.0 : 1.0;
    }
  },

  /**
   * Enum constant representing a binomial distribution.
   *
   * <p>Arguments: {@code [numberOfTrials, probabilityOfSuccess]}, where 0 ≤ {@code numberOfTrials},
   * {@code numberOfTrials} ∈ ℤ, 0 ≤ {@code probabilityOfSuccess} ≤ 1
   *
   * @since 1.0.0
   */
  BINOMIAL("Binomial") {
    @Override
    public void validateArguments(double... arguments) {
      requireFinite(requireArgumentsSize(requireNonNull(arguments), 2));
      double numberOfTrials = arguments[0];
      double probabilityOfSuccess = arguments[1];
      requireInteger(requireNonNegative(numberOfTrials));
      requireZeroToOne(probabilityOfSuccess);
    }

    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      double numberOfTrials = arguments[0];
      double probabilityOfSuccess = arguments[1];
      return numberOfTrials * probabilityOfSuccess;
    }
  },

  /**
   * Enum constant representing an exponential distribution.
   *
   * <p>Arguments: {@code [rate]}, where 0 {@literal <} {@code rate}
   *
   * @since 1.0.0
   */
  EXPONENTIAL("Exponential") {
    @Override
    public void validateArguments(double... arguments) {
      requireFinite(requireArgumentsSize(requireNonNull(arguments), 1));
      double rate = arguments[0];
      requirePositive(rate);
    }

    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      double rate = arguments[0];
      return 1.0 / rate;
    }
  },

  /**
   * Enum constant representing a gamma distribution.
   *
   * <p>Arguments: {@code [shape, scale]}, where 0 {@literal <} {@code shape}, 0 {@literal <} {@code
   * scale}
   *
   * @since 1.0.0
   */
  GAMMA("Gamma") {
    @Override
    public void validateArguments(double... arguments) {
      requireFinite(requireArgumentsSize(requireNonNull(arguments), 2));
      double shape = arguments[0];
      double scale = arguments[1];
      requirePositive(shape);
      requirePositive(scale);
    }

    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      double shape = arguments[0];
      double scale = arguments[1];
      return shape * scale;
    }
  },

  /**
   * Enum constant representing a log-normal distribution.
   *
   * <p>Arguments: {@code [normalMean, normalStandardDeviation]}, where 0 {@literal <} {@code
   * normalStandardDeviation}
   *
   * @since 1.0.0
   */
  LOG_NORMAL("LogNormal") {
    @Override
    public void validateArguments(double... arguments) {
      requireFinite(requireArgumentsSize(requireNonNull(arguments), 2));
      double normalStandardDeviation = arguments[1];
      requirePositive(normalStandardDeviation);
    }

    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      double normalMean = arguments[0];
      double normalStandardDeviation = arguments[1];
      return Math.exp(normalMean + normalStandardDeviation * normalStandardDeviation / 2.0);
    }
  },

  /**
   * Enum constant representing a pareto distribution.
   *
   * <p>Arguments: {@code [minimumValue, shape]}, where 0 {@literal <} {@code minimumValue}, 0
   * {@literal <} {@code shape}
   *
   * @since 1.0.0
   */
  PARETO("Pareto") {
    @Override
    public void validateArguments(double... arguments) {
      requireFinite(requireArgumentsSize(requireNonNull(arguments), 2));
      double minimumValue = arguments[0];
      double shape = arguments[1];
      requirePositive(minimumValue);
      requirePositive(shape);
    }

    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      double minimumValue = arguments[0];
      double shape = arguments[1];
      return shape > 1.0 ? (shape * minimumValue) / (shape - 1.0) : Double.MAX_VALUE;
    }
  },

  /**
   * Enum constant representing a truncated normal distribution.
   *
   * <p>Arguments: {@code [mean, standardDeviation]}, where 0 {@literal <} {@code standardDeviation}
   *
   * @since 1.0.0
   */
  TRUNCATED_NORMAL("TruncatedNormal") {
    @Override
    public void validateArguments(double... arguments) {
      requireFinite(requireArgumentsSize(requireNonNull(arguments), 2));
      double standardDeviation = arguments[1];
      requirePositive(standardDeviation);
    }

    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      double mean = arguments[0];
      return mean;
    }
  },

  /**
   * Enum constant representing a uniform distribution.
   *
   * <p>Arguments: {@code [minimum, maximum]}, where {@code minimum} ≤ {@code maximum}
   *
   * @since 1.0.0
   */
  UNIFORM("Uniform") {
    @Override
    public void validateArguments(double... arguments) {
      requireFinite(requireArgumentsSize(requireNonNull(arguments), 2));
      double minimum = arguments[0];
      double maximum = arguments[1];
      if (minimum > maximum) {
        throw new IllegalArgumentException("Invalid arguments for distribution");
      }
    }

    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      double minimum = arguments[0];
      double maximum = arguments[1];
      return (maximum + minimum) / 2.0;
    }
  },

  /**
   * Enum constant representing the TTC distribution {@code EasyAndCertain}.
   *
   * <p>Defined as {@code Exponential(1.0)}
   *
   * @since 1.0.0
   */
  EASY_AND_CERTAIN("EasyAndCertain") {
    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      return new TtcFunction(EXPONENTIAL, new double[] {1.0}).getMeanTtc();
    }
  },

  /**
   * Enum constant representing the TTC distribution {@code EasyAndUncertain}.
   *
   * <p>Defined as {@code Bernoulli(0.5) + Exponential(1.0)}
   *
   * @since 1.0.0
   */
  EASY_AND_UNCERTAIN("EasyAndUncertain") {
    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      return new TtcAddition(
              new TtcFunction(BERNOULLI, new double[] {0.5}),
              new TtcFunction(EXPONENTIAL, new double[] {1.0}))
          .getMeanTtc();
    }
  },

  /**
   * Enum constant representing the TTC distribution {@code HardAndCertain}.
   *
   * <p>Defined as {@code Exponential(0.1)}
   *
   * @since 1.0.0
   */
  HARD_AND_CERTAIN("HardAndCertain") {
    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      return new TtcFunction(EXPONENTIAL, new double[] {0.1}).getMeanTtc();
    }
  },

  /**
   * Enum constant representing the TTC distribution {@code HardAndUncertain}.
   *
   * <p>Defined as {@code Bernoulli(0.5) + Exponential(0.1)}
   *
   * @since 1.0.0
   */
  HARD_AND_UNCERTAIN("HardAndUncertain") {
    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      return new TtcAddition(
              new TtcFunction(BERNOULLI, new double[] {0.5}),
              new TtcFunction(EXPONENTIAL, new double[] {0.1}))
          .getMeanTtc();
    }
  },

  /**
   * Enum constant representing the TTC distribution {@code VeryHardAndCertain}.
   *
   * <p>Defined as {@code Exponential(0.01)}
   *
   * @since 1.0.0
   */
  VERY_HARD_AND_CERTAIN("VeryHardAndCertain") {
    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      return new TtcFunction(EXPONENTIAL, new double[] {0.01}).getMeanTtc();
    }
  },

  /**
   * Enum constant representing the TTC distribution {@code VeryHardAndUncertain}.
   *
   * <p>Defined as {@code Bernoulli(0.5) + Exponential(0.01)}
   *
   * @since 1.0.0
   */
  VERY_HARD_AND_UNCERTAIN("VeryHardAndUncertain") {
    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      return new TtcAddition(
              new TtcFunction(BERNOULLI, new double[] {0.5}),
              new TtcFunction(EXPONENTIAL, new double[] {0.01}))
          .getMeanTtc();
    }
  },

  /**
   * Enum constant representing infinite TTC.
   *
   * @since 1.0.0
   */
  INFINITY("Infinity") {
    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      return Double.MAX_VALUE;
    }
  },

  /**
   * Enum constant representing zero TTC.
   *
   * @since 1.0.0
   */
  ZERO("Zero") {
    @Override
    public double getMeanTtc(double... arguments) {
      this.validateArguments(arguments);
      return 1.0 / 86400.0;
    }
  },

  /**
   * Enum constant representing enabled defense.
   *
   * @since 1.0.0
   */
  ENABLED("Enabled") {
    @Override
    public double getMeanProbability(double... arguments) {
      this.validateArguments(arguments);
      return 1.0;
    }
  },

  /**
   * Enum constant representing disabled defense.
   *
   * @since 1.0.0
   */
  DISABLED("Disabled") {
    @Override
    public double getMeanProbability(double... arguments) {
      this.validateArguments(arguments);
      return 0.0;
    }
  };

  private static final Map<String, TtcDistribution> DISTRIBUTION_MAP = new LinkedHashMap<>();

  static {
    for (var distribution : values()) {
      DISTRIBUTION_MAP.put(distribution.name, distribution);
    }
  }

  private final String name;

  TtcDistribution(String name) {
    this.name = name;
  }

  private static double[] requireArgumentsSize(double[] arguments, int size) {
    if (arguments.length != size) {
      throw new IllegalArgumentException("Invalid arguments for distribution");
    }
    return arguments;
  }

  private static double[] requireFinite(double[] arguments) {
    for (var argument : arguments) {
      if (!Double.isFinite(argument)) {
        throw new IllegalArgumentException("Invalid arguments for distribution");
      }
    }
    return arguments;
  }

  private static double requireZeroToOne(double argument) {
    if (argument < 0.0 || argument > 1.0) {
      throw new IllegalArgumentException("Invalid arguments for distribution");
    }
    return argument;
  }

  private static double requirePositive(double argument) {
    if (argument <= 0.0) {
      throw new IllegalArgumentException("Invalid arguments for distribution");
    }
    return argument;
  }

  private static double requireNonNegative(double argument) {
    if (argument < 0.0) {
      throw new IllegalArgumentException("Invalid arguments for distribution");
    }
    return argument;
  }

  private static double requireInteger(double argument) {
    if (Math.floor(argument) != argument) {
      throw new IllegalArgumentException("Invalid arguments for distribution");
    }
    return argument;
  }

  /**
   * Validates {@code arguments} against this {@code TtcDistribution}.
   *
   * @param arguments the arguments of the distribution
   * @throws java.lang.NullPointerException if {@code arguments} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code arguments} is not valid for this {@code
   *     TtcDistribution}
   * @since 1.0.0
   */
  public void validateArguments(double... arguments) {
    requireArgumentsSize(requireNonNull(arguments), 0);
  }

  /**
   * Returns the mean TTC of this {@code TtcDistribution} object given {@code arguments}.
   *
   * @param arguments the arguments of the distribution
   * @return the mean TTC of this {@code TtcDistribution} object given {@code arguments}
   * @throws java.lang.UnsupportedOperationException if this {@code TtcDistribution} does not
   *     support mean TTC
   * @throws java.lang.NullPointerException if {@code arguments} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code arguments} is not valid for this {@code
   *     TtcDistribution}
   * @since 1.0.0
   */
  public double getMeanTtc(double... arguments) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the mean probability of this {@code TtcDistribution} object given {@code arguments}.
   *
   * @param arguments the arguments of the distribution
   * @return the mean probability of this {@code TtcDistribution} object given {@code arguments}
   * @throws java.lang.UnsupportedOperationException if this {@code TtcDistribution} does not
   *     support mean probability
   * @throws java.lang.NullPointerException if {@code arguments} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code arguments} is not valid for this {@code
   *     TtcDistribution}
   * @since 1.0.0
   */
  public double getMeanProbability(double... arguments) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the name of this {@code TtcDistribution} object.
   *
   * @return the name of this {@code TtcDistribution} object
   * @since 1.0.0
   */
  @Override
  public String toString() {
    return this.name;
  }

  /**
   * Returns the distribution with the name {@code name}.
   *
   * @param name the name of the distribution
   * @return the distribution with the name {@code name}
   * @throws java.lang.NullPointerException if {@code name} is {@code null}
   * @throws java.lang.IllegalArgumentException if {@code name} is not the name of a distribution
   * @since 1.0.0
   */
  public static TtcDistribution fromString(String name) {
    requireNonNull(name);
    if (!TtcDistribution.DISTRIBUTION_MAP.containsKey(name)) {
      throw new IllegalArgumentException(String.format("TTC distribution \"%s\" not found", name));
    }
    return TtcDistribution.DISTRIBUTION_MAP.get(name);
  }
}
