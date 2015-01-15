package ru.relex.hakaton;

import java.util.Date;

public class HelloMessage {
  private static String[] hiMessages   = { "Здравствуйте, #1 #2", "#1, доброе утро!",
      "Доброе утро, #1 #2", "Уважаемый #3 #1 #2, рады приветствовать вас",
      "Как приятно видеть вас, #1", "#1, как прекрасно вы сегодня выглядите", "Утро доброе, #1" };
  private static String[] byMessages   = { "До свидания, #1 #2", "#1, до встречи!", "Пока, #1",
      "До скорой встречи, #1", "#1 #2, до новых встреч!",
      "#1, удачного вечера!"          };
  private static String[] brrrMessages = { "Вы кто такой?", "А я вас не знаю",
      "Стойте, стрелять буду", "Осторожно, посторонним вход воспрещен",
      "Вы кто? Стоять, бежать, лежать", "Извините, вы не прошли идентификацию",
      "Ай, яй, яй. Нельзя сюда"       };

  public static String getHiMessage(String firstName, String middleName, String lastName) {

    String message;
//    if (new Date().getHours() < 16)
      message = hiMessages[(int) (Math.random() * hiMessages.length)];
//    else
//      message = byMessages[(int) (Math.random() * byMessages.length)];

    message = message.replace("#1", firstName);
    message = message.replace("#2", middleName);
    message = message.replace("#3", lastName);
    return message;
  }

  public static String getBrrrMessage() {
    String message = brrrMessages[(int) (Math.random() * brrrMessages.length)];
    return message;
  }
}
