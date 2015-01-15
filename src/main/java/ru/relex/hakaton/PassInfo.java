package ru.relex.hakaton;

import java.util.Date;

public class PassInfo {
  
  static enum UserStatus {
    NONE("none"),
    WORK("work"),
    AWAY("away"),
    ABSENT("absent");
    private String value;
    private UserStatus(String value) {
      this.value = value;
    }
    public static UserStatus mvalueOf(String value) {
      UserStatus result = NONE;
      for (UserStatus userStatus: UserStatus.values()) {
        if (userStatus.value.equalsIgnoreCase(value)) {
          result = userStatus;
          break;
        }
      }
      return result;
    }
  }
  private int id;
  private int userId;
  private String firstName;
  private String middleName;
  private String lastName;
  private UserStatus status;
  private Date   passTime;

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

  @Override
  public String toString() {
    return "UserInfo [id=" + id + ", userId=" + userId + ", firstName=" + firstName
        + ", middleName=" + middleName + ", lastName=" + lastName + ", status=" + status
        + ", passTime=" + passTime + "]";
  }

}
