package no.ntnu.greenhouse;

import java.util.Objects;

/**
 * Represents one sensor reading (value).
 */
public class SensorReading {
  private final String type;
  private double value;
  private final String unit;

  /**
   * Create a new sensor reading.
   *
   * @param type  The type of sensor being read
   * @param value The current value of the sensor
   * @param unit  The unit, for example: %, lux
   */
  public SensorReading(String type, double value, String unit) {
    this.type = type;
    this.value = value;
    this.unit = unit;
  }

  /**
   * Getter for type of sensor.
   *
   * @return returns the type of sensor
   */
  public String getType() {
    return type;
  }

  /**
   * Getter for value from the sensor reading.
   *
   * @return The value from sensor reading.
   */
  public double getValue() {
    return value;
  }

  /**
   * Getter for unit measurements.
   *
   * @return The unit from the sensor reading.
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Setter for new value from the sensor reader.
   *
   * @param newValue The new value to be set.
   */
  public void setValue(double newValue) {
    this.value = newValue;
  }

  /**
   * Returns a string representation of the sensor reading.
   *
   * @return A string representation.
   */
  @Override
  public String toString() {
    return "{ type=" + type + ", value=" + value + ", unit=" + unit + " }";
  }

  /**
   * Get a human-readable (formatted) version of the current reading, including the unit.
   *
   * @return The sensor reading and the unit
   */
  public String getFormatted() {
    return value + unit;
  }

  /**
   * Checks if some other object is equal to this object.
   *
   * @param o The object to compare.
   * @return True if the object is the same as the argument o. Returns false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SensorReading that = (SensorReading) o;
    return Double.compare(value, that.value) == 0
        && Objects.equals(type, that.type)
        && Objects.equals(unit, that.unit);
  }

  /**
   * Returns a hash value for the sensor reading.
   *
   * @return A hash code value for this sensor reading.
   */
  @Override
  public int hashCode() {
    return Objects.hash(type, value, unit);
  }
}
