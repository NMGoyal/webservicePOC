package org.weatherapi;

import static io.restassured.RestAssured.*;

import io.restassured.response.Response;

public class DeleteMethod {

	public Response DeleteRequest(String apiKey, String id) {

		Response response = given()
								.param("appid", apiKey)
								.header("Content-Type", "application/json")
							.when()
								.delete("/data/3.0/stations/"+id)
							.then()
							    .extract().response();

		return response;
	}

}