package ru.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.models.Order;
import ru.scooter.steps.OrderClient;

import java.util.Arrays;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderTrackTest {
    private OrderClient orderClient;
    private int track;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        Order order = Order.random(Arrays.asList("GREY"));
        track = orderClient.create(order)
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("track");
    }

    @Test
    @DisplayName("По номеру возвращается объект с заказом")
    public void orderIsReturnedByTrack() {
        orderClient.getByTrack(track)
                .statusCode(SC_OK)
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Запрос без номера заказа возвращает ошибку")
    public void requestWithoutTrack() {
        orderClient.getByTrack(null)
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Запрос с несуществующим заказом возвращает ошибку")
    public void requestWithNonExistentTrack() {
        orderClient.getByTrack(999999999)
                .statusCode(SC_NOT_FOUND);
    }
}
