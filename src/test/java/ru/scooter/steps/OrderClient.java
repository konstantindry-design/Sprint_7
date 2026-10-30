package ru.scooter.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.scooter.RestClient;
import ru.scooter.models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    @Step("Создание заказа")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(spec)
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrders() {
        return given()
                .spec(spec)
                .when()
                .get("/api/v1/orders")
                .then();
    }

    @Step("Принятие заказа {orderId} курьером {courierId}")
    public ValidatableResponse accept(Integer orderId, Integer courierId) {
        StringBuilder url = new StringBuilder("/api/v1/orders/accept");
        if (orderId != null) {
            url.append("/").append(orderId);
        }
        RequestSpecification specWithParams = given().spec(spec);
        if (courierId != null) {
            specWithParams.queryParam("courierId", courierId);
        }
        return specWithParams.when().put(url.toString()).then();
    }

    @Step("Получение заказа по треку {track}")
    public ValidatableResponse getByTrack(Integer track) {
        if (track != null) {
            return given()
                    .spec(spec)
                    .queryParam("t", track)
                    .when()
                    .get("/api/v1/orders/track")
                    .then();
        }
        return given()
                .spec(spec)
                .when()
                .get("/api/v1/orders/track")
                .then();
    }
}
