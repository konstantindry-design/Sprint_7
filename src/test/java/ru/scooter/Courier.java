package ru.scooter;

import io.qameta.allure.Step;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    @Step("Создание данных курьера")
    public static Courier random() {
        long time = System.currentTimeMillis();
        return new Courier("courier" + time, "pass" + time, "Саша");
    }
}

