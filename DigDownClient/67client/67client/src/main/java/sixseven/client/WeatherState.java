package sixseven.client;

public enum WeatherState {
  RAIN,
  THUNDER,
  CLEAR;

  static {
    String[] textArray = new String[] {"THUNDER", "CLEAR", "RAIN"};
  }
}
