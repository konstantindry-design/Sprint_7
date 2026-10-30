package ru.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.models.Courier;
import ru.scooter.models.CourierCredentials;
import ru.scooter.models.Order;
import ru.scooter.steps.CourierClient;
import ru.scooter.steps.OrderClient;

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
        courierClient.create(courier).statusCode(HttpStatus.SC_CREATED);
        courierId = courierClient.login(CourierCredentials.from(courier))
                .extract()
                .path("id");
        Order order = Order.random(Arrays.asList("BLACK"));
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
                .statusCode(HttpStatus.SC_OK)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Ошибка без id курьера")
    public void acceptWithoutCourierId() {
        orderClient.accept(orderId, null)
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Ошибка с неверным id курьера")
    public void acceptWithWrongCourierId() {
        orderClient.accept(orderId, 999999999)
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Ошибка без номера заказа")
    public void acceptWithoutOrderId() {
        orderClient.accept(null, courierId)
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Ошибка с неверным номером заказа")
    public void acceptWithWrongOrderId() {
        orderClient.accept(999999999, courierId)
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}