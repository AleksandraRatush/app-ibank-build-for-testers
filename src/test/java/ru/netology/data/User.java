package ru.netology.data;

import lombok.Data;
import lombok.Value;

@Value
public class User {

    String name = "vasya";
    String password = "qwerty123";
    String code = "12345";
    String[] cards = {"5559 0000 0000 0001", "5559 0000 0000 0002"};
    String fakeCard = "5559 0000 0000 0003";

    private static final User user = new User();

    private User() {

    }

    public static User getInstance() {
        return user;
    }

}
