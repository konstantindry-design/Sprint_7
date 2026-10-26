package ru.scooter;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderTrackTest {
    private OrderClient orderClient;
    private int track;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        Order order = new Order("Иван", "Иванов", "Ленинградский проспект, 1", 4,
                "+79990001122", 3, "2026-11-01", "Позвонить за час", Arrays.asList("GREY"));
        track = orderClient.create(order)
                .extract()
                .path("track");
    }

    @Test
    @DisplayName("По номеру возвращается объект с заказом")
    public void orderIsReturnedByTrack() {
        orderClient.getByTrack(track)
                .statusCode(200)
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Запрос без номера заказа возвращает ошибку")
    public void requestWithoutTrack() {
        orderClient.getByTrack(null)
                .statusCode(400);
    }

    @Test
    @DisplayName("Запрос с несуществующим заказом возвращает ошибку")
    public void requestWithNonExistentTrack() {
        orderClient.getByTrack(999999999)
                .statusCode(404);
    }
}

