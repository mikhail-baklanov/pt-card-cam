package ru.relex.hakaton;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import ru.relex.hakaton.HTTPRequest.Responce;

public class SenderFacade {
  private String url;

  public SenderFacade(String targetURL, String auth) {
    url = targetURL;
    if (auth != null && auth.trim().length() != 0) {
      url += url.indexOf("?") > 0 ? "&" : "?";
      url += "auth=" + auth;
    }
  }

  public List<PassInfo> getPasses() {
    List<PassInfo> result = new ArrayList<PassInfo>();

    Responce r = HTTPRequest.execute("GET", url, null);
    System.out.println("code: " + r.getResponceCode() + ", body: " + r.getBody());
    if (r.getResponceCode() < 200 || r.getResponceCode() >= 300) {
      return null;
    }

    String json = r.getBody();
    System.out.println(json);
    try {
      JSONArray passes = new JSONObject(json).getJSONArray("passes");

      for (int i = 0; i < passes.length(); i++) {
        //System.out.println(passes.get(i));
        result.add(PassInfo.fromJSONObject(passes.getJSONObject(i)));
        System.out.println(result.get(result.size()-1));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return result;
  }

  public PassInfo sendId(String id) {
    String body = "{\"cardId\":\"" + id + "\"}";
    System.out.println(body);
    Responce r = HTTPRequest.execute("POST", url, body);
    System.out.println("code: " + r.getResponceCode() + ", body: " + r.getBody());
    if (r.getResponceCode() < 200 || r.getResponceCode() >= 300) {
      return null;
    }
    return PassInfo.fromJSONObject(new JSONObject(r.getBody()));
  }

  public boolean applyPassInfo(PassInfo pi) {
    String body = "{\"id\":\"" + pi.getId() + "\", \"status\":\""+pi.getStatus().getValue()+"\"}";
    System.out.println(body);
    Responce r = HTTPRequest.execute("PUT", url, body);
    System.out.println("code: " + r.getResponceCode() + ", body: " + r.getBody());
    if (r.getResponceCode() < 200 || r.getResponceCode() >= 300) {
      return false;
    }
    return true;
  }

}
