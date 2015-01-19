package ru.relex.hakaton;

import org.json.JSONObject;

public class JSONUtils {
  public static String getString(JSONObject obj, String fieldName) {
    try {
      return obj.getString(fieldName);
    }
    catch (Exception e) {
      return null;
    }
  }

  public static int getInt(JSONObject obj, String fieldName) {
    try {
      return obj.getInt(fieldName);
    }
    catch (Exception e) {
      return 0;
    }
  }

  public static long getLong(JSONObject obj, String fieldName) {
    try {
      return obj.getLong(fieldName);
    }
    catch (Exception e) {
      return 0L;
    }
  }

}
