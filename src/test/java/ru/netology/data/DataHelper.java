package ru.netology.data;

import lombok.Value;

public class DataHelper {

   public static User getUser() {
       return new User("vasya", "qwerty123","12345");
   }

    public static Card getCard() {
        return new Card(new String[]{"5559 0000 0000 0001", "5559 0000 0000 0002"},
                "5559 0000 0000 0003");
    }


    @Value
    public static class User {
        String name;
        String password;
        String code;
    }

    @Value
    public static class Card {
        String[] cards;
        String fakeCard;
    }

}
