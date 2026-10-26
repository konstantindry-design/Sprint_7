package ru.scooter;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class CourierClient {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";

    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .contentType(JSON)
                .body(courier)
                .when()
                .post(BASE_URI + "/api/v1/courier")
                .then();
    }

    @Step("Логин курьера")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .contentType(JSON)
                .body(credentials)
                .when()
                .post(BASE_URI + "/api/v1/courier/login")
                .then();
    }

    @Step("Удаление курьера с id {id}")
    public ValidatableResponse delete(Integer id) {
        return given()
                .delete(BASE_URI + "/api/v1/courier/" + id)
                .then();
    }

    @Step("Удаление курьера без id")
    public ValidatableResponse deleteWithoutId() {
        return given()
                .delete(BASE_URI + "/api/v1/courier")
                .then();
    }
}

