package ru.scooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierCreateTest {
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
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
    @DisplayName("Курьера можно создать")
    public void courierCanBeCreated() {
        courier = Courier.random();
        courierClient.create(courier)
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void duplicateCourierIsNotAllowed() {
        courier = Courier.random();
        courierClient.create(courier)
                .statusCode(201)
                .body("ok", equalTo(true));

        Courier duplicate = new Courier(courier.getLogin(), "anotherPass", "Лёша");
        courierClient.create(duplicate)
                .statusCode(409);
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    public void createCourierWithoutLogin() {
        courier = null;
        Courier courierWithoutLogin = new Courier(null, "pass123", "Саша");
        courierClient.create(courierWithoutLogin)
                .statusCode(400);
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    public void createCourierWithoutPassword() {
        courier = null;
        Courier courierWithoutPassword = new Courier("courier" + System.currentTimeMillis(), null, "Саша");
        courierClient.create(courierWithoutPassword)
                .statusCode(400);
    }

    @Test
    @DisplayName("Курьера можно создать без имени")
    public void createCourierWithoutFirstName() {
        courier = new Courier("courier" + System.currentTimeMillis(), "pass123", null);
        courierClient.create(courier)
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Успешный запрос возвращает ok true")
    public void successfulCreateReturnsOk() {
        courier = Courier.random();
        ValidatableResponse response = courierClient.create(courier);
        response.statusCode(201);
        response.body("ok", notNullValue());
        response.body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера с существующим логином возвращает ошибку")
    public void createCourierWithExistingLogin() {
        courier = Courier.random();
        courierClient.create(courier)
                .statusCode(201);

        Courier sameLogin = new Courier(courier.getLogin(), "pass" + System.currentTimeMillis(), "Лёша");
        courierClient.create(sameLogin)
                .statusCode(409);
    }
}

