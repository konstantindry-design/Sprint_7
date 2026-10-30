package ru.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.scooter.models.Courier;
import ru.scooter.models.CourierCredentials;
import ru.scooter.steps.CourierClient;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierDeleteTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = Courier.random();
        courierClient.create(courier).statusCode(HttpStatus.SC_CREATED);
        courierId = courierClient.login(CourierCredentials.from(courier))
                .extract()
                .path("id");
    }

    @After
    public void tearDown() {
        if (courier != null) {
            ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
            Integer id = loginResponse.extract().path("id");
            if (id != null) {
                courierClient.delete(id);
            }
            courier = null;
        }
    }

    @Test
    @DisplayName("Успешное удаление возвращает ok true")
    public void courierCanBeDeleted() {
        courierClient.delete(courierId)
                .statusCode(HttpStatus.SC_OK)
                .body("ok", equalTo(true));
        courier = null;
    }

    @Test
    @DisplayName("Удаление несуществующего курьера возвращает ошибку")
    public void deleteNonExistentCourier() {
        courierClient.delete(999999999)
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Удаление без id возвращает ошибку")
    public void deleteWithoutId() {
        courierClient.deleteWithoutId()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
