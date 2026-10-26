package ru.scooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierDeleteTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = Courier.random();
        courierClient.create(courier).statusCode(201);
        courierId = courierClient.login(CourierCredentials.from(courier))
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("Успешное удаление возвращает ok true")
    public void courierCanBeDeleted() {
        ValidatableResponse response = courierClient.delete(courierId);
        response.statusCode(200)
                .body("ok", equalTo(true));
        courier = null;
    }

    @Test
    @DisplayName("Удаление несуществующего курьера возвращает ошибку")
    public void deleteNonExistentCourier() {
        courierClient.delete(999999999)
                .statusCode(404);
    }

    @Test
    @DisplayName("Удаление без id возвращает ошибку")
    public void deleteWithoutId() {
        courierClient.deleteWithoutId()
                .statusCode(404);
    }
}

