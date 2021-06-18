package org.weatherapi;

import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.json.JSONObject;

public class PostMethod {

	public Response postRequest(String apiKey, JSONObject requestStation) {

		Response response = given()
								.queryParam("appid", apiKey)
								.header("Content-Type", "application/json")
								.body(requestStation.toString())
							.when()
								.post("/data/3.0/stations")
							.then()
								.contentType(ContentType.JSON)
							    .extract().response();
					
		return response;
	}

}
