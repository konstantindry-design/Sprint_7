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
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = Courier.random();
        courierClient.create(courier).statusCode(HttpStatus.SC_CREATED);
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
    @DisplayName("Курьер может авторизоваться")
    public void courierCanLogin() {
        courierClient.login(CourierCredentials.from(courier))
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин возвращает id")
    public void loginReturnsId() {
        ValidatableResponse response = courierClient.login(CourierCredentials.from(courier));
        response.statusCode(HttpStatus.SC_OK);
        int id = response.extract().path("id");
        org.junit.Assert.assertTrue(id > 0);
    }

    @Test
    @DisplayName("Ошибка при неправильном пароле")
    public void loginWithWrongPassword() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "wrongPass");
        courierClient.login(credentials)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при неправильном логине")
    public void loginWithWrongLogin() {
        CourierCredentials credentials = new CourierCredentials("wrongLogin", courier.getPassword());
        courierClient.login(credentials)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при логине без пароля")
    public void loginWithoutPassword() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), null);
        courierClient.login(credentials)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при логине без логина")
    public void loginWithoutLogin() {
        CourierCredentials credentials = new CourierCredentials(null, courier.getPassword());
        courierClient.login(credentials)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при авторизации несуществующего пользователя")
    public void loginNonExistentUser() {
        CourierCredentials credentials = new CourierCredentials("nonexistent" + System.currentTimeMillis(), "pass123");
        courierClient.login(credentials)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
