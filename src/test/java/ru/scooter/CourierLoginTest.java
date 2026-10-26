package ru.scooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = Courier.random();
        courierClient.create(courier).statusCode(201);
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void courierCanLogin() {
        courierClient.login(CourierCredentials.from(courier))
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин возвращает id")
    public void loginReturnsId() {
        ValidatableResponse response = courierClient.login(CourierCredentials.from(courier));
        response.statusCode(200);
        int id = response.extract().path("id");
        org.junit.Assert.assertTrue(id > 0);
    }

    @Test
    @DisplayName("Ошибка при неправильном пароле")
    public void loginWithWrongPassword() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "wrongPass");
        courierClient.login(credentials)
                .statusCode(404);
    }

    @Test
    @DisplayName("Ошибка при неправильном логине")
    public void loginWithWrongLogin() {
        CourierCredentials credentials = new CourierCredentials("wrongLogin", courier.getPassword());
        courierClient.login(credentials)
                .statusCode(404);
    }

    @Test
    @DisplayName("Ошибка при логине без пароля")
    public void loginWithoutPassword() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), null);
        courierClient.login(credentials)
                .statusCode(504);
    }

    @Test
    @DisplayName("Ошибка при логине без логина")
    public void loginWithoutLogin() {
        CourierCredentials credentials = new CourierCredentials(null, courier.getPassword());
        courierClient.login(credentials)
                .statusCode(400);
    }

    @Test
    @DisplayName("Ошибка при авторизации несуществующего пользователя")
    public void loginNonExistentUser() {
        CourierCredentials credentials = new CourierCredentials("nonexistent" + System.currentTimeMillis(), "pass123");
        courierClient.login(credentials)
                .statusCode(404);
    }
}