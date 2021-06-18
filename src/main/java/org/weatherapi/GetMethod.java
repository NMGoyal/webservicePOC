package org.weatherapi;

import static io.restassured.RestAssured.*;

import io.restassured.response.Response;

public class GetMethod {

    public Response getRequest(String apiKey, String responseID) {

        Response response = given()
        						.queryParam("appid", apiKey)
        						.header("content-type", "application/json")
        					.when()
        						.get("/data/3.0/stations/" + responseID)
        					.then()
        						.extract().response();

        return response;
    }
}
