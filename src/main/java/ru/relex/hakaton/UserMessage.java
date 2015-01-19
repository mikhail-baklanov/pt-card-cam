package ru.relex.hakaton;

import java.util.Date;

import org.json.JSONObject;

public class UserMessage {

  private String text;
  private String sender;
  private Date   date;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public static UserMessage fromJSONObject(JSONObject obj) {
    UserMessage user = new UserMessage();

    try {
      user.setText(JSONUtils.getString(obj, "text"));
      user.setSender(JSONUtils.getString(obj, "sender"));
      user.setDate(new Date(JSONUtils.getLong(obj, "date")));
    }
    catch (Exception e) {
      e.printStackTrace();
      user = null;
    }

    return user;

  }

  @Override
  public String toString() {
    return "UserMessage [text=" + text + ", sender=" + sender + ", date=" + date + "]";
  }

}
