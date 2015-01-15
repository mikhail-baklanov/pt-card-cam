package ru.relex.hakaton;

import java.util.Date;

import ru.relex.hakaton.HTTPRequest.Responce;
import ru.relex.hakaton.PassInfo.UserStatus;

import org.json.JSONObject;

public class SenderFacade {
  private String url;

  public SenderFacade(String targetURL, String auth) {
    url = targetURL;
    if (auth != null && auth.trim().length() != 0) {
      url += url.indexOf("?") > 0 ? "&" : "?";
      url += "auth=" + auth;
    }
  }

  public PassInfo sendId(String id) {
    String body = "{\"pass\":{\"cardId\":\"" + id + "\"}}";
    System.out.println(body);
    Responce r = HTTPRequest.execute("POST", url, body);
    System.out.println("code: " + r.getResponceCode() + ", body: " + r.getBody());
    if (r.getResponceCode() < 200 || r.getResponceCode() >= 300) {
      return null;
    }
    PassInfo user = new PassInfo();

    String json = r.getBody();
    //json = "{\"pass\":{\"firstName\":\"Иван\", \"middleName\":\"Иванович\", \"lastName\":\"Иванов\"}}";
    try {
      JSONObject rootObj = new JSONObject(json);
      JSONObject obj = rootObj.getJSONObject("pass");
      user.setFirstName(getString(obj, "firstName"));
      user.setLastName(getString(obj, "lastName"));
      user.setMiddleName(getString(obj, "middleName"));
      user.setPassTime(new Date(getLong(obj, "passTime")));
      user.setStatus(UserStatus.mvalueOf(getString(obj, "status")));
      user.setId(getInt(obj, "id"));
      user.setUserId(getInt(obj, "userId"));
    }
    catch (Exception e) {
      e.printStackTrace();
      user = null;
    }

    return user;
  }

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
