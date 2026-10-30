package ru.scooter.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;

    public static Order random() {
        return random(null);
    }

    public static Order random(List<String> color) {
        return new Order(
                "Нарутто",
                "Уматаки",
                "Отрадное, 142 апартаменты",
                4,
                "+7 800 355 35 35",
                5,
                LocalDate.now().toString(),
                "Прими заказ, а то приду",
                color
        );
    }
}
