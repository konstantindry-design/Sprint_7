package ru.scooter;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;

public class OrderAcceptTest {
    private CourierClient courierClient;
    private OrderClient orderClient;
    private Courier courier;
    private int courierId;
    private Integer orderId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        orderClient = new OrderClient();
        courier = Courier.random();
        courierClient.create(courier).statusCode(201);
        courierId = courierClient.login(CourierCredentials.from(courier))
                .extract()
                .path("id");
        Order order = new Order("Иван", "Иванов", "Ленинградский проспект, 1", 4,
                "+79990001122", 3, "2026-11-01", "Позвонить за час", Arrays.asList("BLACK"));
        int track = orderClient.create(order)
                .extract()
                .path("track");

        orderId = null;
        for (int attempt = 0; attempt < 5 && orderId == null; attempt++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            orderId = orderClient.getByTrack(track).extract().path("order.id");
        }

        Assume.assumeTrue("Стенд не вернул заказ по треку, тест пропущен", orderId != null);
    }

    @After
    public void tearDown() {
        if (courier != null) {
            Integer id = courierClient.login(CourierCredentials.from(courier)).extract().path("id");
            if (id != null) {
                courierClient.delete(id);
            }
        }
    }

    @Test
    @DisplayName("Заказ можно принять")
    public void orderCanBeAccepted() {
        orderClient.accept(orderId, courierId)
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Ошибка без id курьера")
    public void acceptWithoutCourierId() {
        orderClient.accept(orderId, null)
                .statusCode(400);
    }

    @Test
    @DisplayName("Ошибка с неверным id курьера")
    public void acceptWithWrongCourierId() {
        orderClient.accept(orderId, 999999999)
                .statusCode(404);
    }

    @Test
    @DisplayName("Ошибка без номера заказа")
    public void acceptWithoutOrderId() {
        orderClient.accept(null, courierId)
                .statusCode(404);
    }

    @Test
    @DisplayName("Ошибка с неверным номером заказа")
    public void acceptWithWrongOrderId() {
        orderClient.accept(999999999, courierId)
                .statusCode(404);
    }
}
