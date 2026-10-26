package ru.scooter;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private final RequestSpecification request = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .build();

    @Step("Создание заказа")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(request)
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrders() {
        return given()
                .spec(request)
                .when()
                .get("/api/v1/orders")
                .then();
    }

    @Step("Принятие заказа {orderId} курьером {courierId}")
    public ValidatableResponse accept(Integer orderId, Integer courierId) {
        StringBuilder url = new StringBuilder(BASE_URI + "/api/v1/orders/accept");
        if (orderId != null) {
            url.append("/").append(orderId);
        }
        RequestSpecification spec = given().spec(request);
        if (courierId != null) {
            spec.queryParam("courierId", courierId);
        }
        return spec.when().put(url.toString()).then();
    }

    @Step("Получение заказа по треку {track}")
    public ValidatableResponse getByTrack(Integer track) {
        if (track != null) {
            return given()
                    .spec(request)
                    .queryParam("t", track)
                    .when()
                    .get("/api/v1/orders/track")
                    .then();
        }
        return given()
                .spec(request)
                .when()
                .get("/api/v1/orders/track")
                .then();
    }
}