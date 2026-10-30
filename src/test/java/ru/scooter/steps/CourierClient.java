package ru.scooter.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.scooter.RestClient;
import ru.scooter.models.Courier;
import ru.scooter.models.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient {

    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(spec)
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then();
    }

    @Step("Логин курьера")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(spec)
                .body(credentials)
                .when()
                .post("/api/v1/courier/login")
                .then();
    }

    @Step("Удаление курьера с id {id}")
    public ValidatableResponse delete(Integer id) {
        return given()
                .spec(spec)
                .delete("/api/v1/courier/" + id)
                .then();
    }

    @Step("Удаление курьера без id")
    public ValidatableResponse deleteWithoutId() {
        return given()
                .spec(spec)
                .delete("/api/v1/courier")
                .then();
    }
}
