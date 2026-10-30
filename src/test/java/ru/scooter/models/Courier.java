package ru.scooter.models;

import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Courier {
    private String login;
    private String password;
    private String firstName;

    @Step("Создание данных курьера")
    public static Courier random() {
        long time = System.currentTimeMillis();
        return new Courier("courier" + time, "pass" + time, "Саша");
    }
}

