package ru.relex.hakaton;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequest {
  public static class Responce {
    private String body;
    private int    responceCode;

    public void setBody(String body) {
      this.body = body;
    }

    public void setResponceCode(int responceCode) {
      this.responceCode = responceCode;
    }

    public String getBody() {
      return body;
    }

    public int getResponceCode() {
      return responceCode;
    }
  }

  public static Responce execute(String requestMethod, String targetURL, String body) {
    URL url;
    HttpURLConnection connection = null;
    Responce responce = new Responce();
    try {
      // Create connection
      url = new URL(targetURL);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(requestMethod);
      connection.setRequestProperty("Content-Type", "application/json");

      connection
          .setRequestProperty("Content-Length", "" + Integer.toString(body.getBytes().length));
      connection.setRequestProperty("Content-Language", "ru");

      connection.setUseCaches(false);
      connection.setDoInput(true);

      if ("POST".equals(requestMethod) || "PUT".equals(requestMethod)) {
        // Send request
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(body);
        wr.flush();
        wr.close();
      }

      connection.connect();

      // Get Response
      InputStream is = connection.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      String line;
      StringBuffer responseBody = new StringBuffer();
      while ((line = rd.readLine()) != null) {
        responseBody.append(line);
        responseBody.append('\r');
      }
      rd.close();
      responce.setBody(responseBody.toString());
      responce.setResponceCode(connection.getResponseCode());
      return responce;

    }
    catch (Exception e) {

      try {
        responce.setResponceCode(connection == null ? 0 : connection.getResponseCode());
        responce.setBody(e.getMessage());
      }
      catch (IOException e1) {
        responce.setResponceCode(0);
        responce.setBody(e1.getMessage());
      }
      return responce;

    }
    finally {

      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  public static void main(String[] args) {
    Responce r = execute("POST", "http://posttestserver.com/post.php?dir=example", "test");
    System.out.println("code: " + r.getResponceCode() + ", body: " + r.getBody());
  }
}
