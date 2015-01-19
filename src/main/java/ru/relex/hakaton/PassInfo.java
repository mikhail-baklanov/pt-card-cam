package ru.relex.hakaton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PassInfo {

  static enum UserStatus {
      NONE ("none"),
      WORK ("work"),
      IGNORE ("ignore"),
      AWAY ("away"),
      ABSENT ("absent");
    private String value;

    private UserStatus(String value) {
      this.value = value;
    }

    public static UserStatus mvalueOf(String value) {
      UserStatus result = NONE;
      for (UserStatus userStatus : UserStatus.values()) {
        if (userStatus.value.equalsIgnoreCase(value)) {
          result = userStatus;
          break;
        }
      }
      return result;
    }

    public String getValue() {
      return value;
    }
  }

  private int               id;
  private int               userId;
  private String            firstName;
  private String            middleName;
  private String            lastName;
  private UserStatus        status;
  private Date              passTime;
  private List<UserMessage> userMessages = new ArrayList<UserMessage>();

  public List<UserMessage> getUserMessages() {
    return userMessages;
  }

  public void addUserMessage(UserMessage userMessage) {
    userMessages.add(userMessage);
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public void setPassTime(Date passTime) {
    this.passTime = passTime;
  }

  public int getId() {
    return id;
  }

  public int getUserId() {
    return userId;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public UserStatus getStatus() {
    return status;
  }

  public Date getPassTime() {
    return passTime;
  }

  public static PassInfo fromJSONObject(JSONObject obj) {
    PassInfo user = new PassInfo();

    try {
      user.setFirstName(JSONUtils.getString(obj, "firstName"));
      user.setLastName(JSONUtils.getString(obj, "lastName"));
      user.setMiddleName(JSONUtils.getString(obj, "middleName"));
      user.setPassTime(new Date(JSONUtils.getLong(obj, "passTime")));
      user.setStatus(UserStatus.mvalueOf(JSONUtils.getString(obj, "status")));
      user.setId(JSONUtils.getInt(obj, "id"));
      user.setUserId(JSONUtils.getInt(obj, "userId"));
      try {
        JSONArray userMessages = obj.getJSONArray("userMessages");
        System.out.println("json user messages: " + userMessages);
        if (userMessages != null) {
          for (int i = 0; i < userMessages.length(); i++) {
            user.addUserMessage(UserMessage.fromJSONObject(userMessages.getJSONObject(i)));
          }
        }
      }
      catch (Exception e) {
        try {
          JSONObject userMessage = obj.getJSONObject("userMessages");
          System.out.println("json user messages: " + userMessage);
          if (userMessage != null) {
              user.addUserMessage(UserMessage.fromJSONObject(userMessage));
          }
        }
        catch (Exception e2) {
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      user = null;
    }

    return user;

  }

  @Override
  public String toString() {
    return "PassInfo [id=" + id + ", userId=" + userId + ", firstName=" + firstName
        + ", middleName=" + middleName + ", lastName=" + lastName + ", status=" + status
        + ", passTime=" + passTime + ", userMessages=" + userMessages + "]";
  }

}
