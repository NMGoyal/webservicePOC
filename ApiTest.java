package org.weatherapi;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ApiTest extends BasicExtentReport {

	public static final Logger logger = LogManager.getLogger(ApiTest.class);

	static String apiKey = "";

	JSONObject requestStation = new JSONObject();

	Response response;

	List<String> responseID = new ArrayList<String>();

	int getCount = 0;

	static PayloadRequest payload;
	static PostMethod postMethod;
	static DeleteMethod deleteMethod;

	@BeforeTest
	public static void setStation() {

		baseURI = "http://api.openweathermap.org";
		apiKey = "b1c0c7b628e6d98b2735cbde40d09cfc";

		payload = new PayloadRequest();
		postMethod = new PostMethod();
		deleteMethod = new DeleteMethod();
	}

	@Test(enabled = true, priority = 0, dataProvider = "StationDetails")
	public void addStation(String external_id, String name, double latitude, double longitude, int altitude) {

		test = extent.createTest("addStation");

		logger.info("POST request created for external_id : " + external_id);

		// Create a payload request
		requestStation = payload.createRequest(external_id, name, latitude, longitude, altitude);

		// Get the POST method response
		response = postMethod.postRequest(apiKey, requestStation);

		// Verify the POST method response status code is Created (201)
		Assert.assertEquals(response.statusCode(), 201);

		logger.info("POST request successful with status code: " + response.statusCode());
		logger.info("Response body : " + response.body().asString());

		// Save Response ID for GET, DELETE methods reference
		responseID.add(response.path("ID").toString());

	}

	@Test(enabled = true, priority = 1, dataProvider = "StationDetails", dependsOnMethods = { "addStation" })
	public void getStation(String external_id, String name, double latitude, double longitude, int altitude) {

		test = extent.createTest("getStation");

		// GET tests
		GetMethod getMethod = new GetMethod();
		SoftAssert softAssert = new SoftAssert();
		Response obtainedResponse = getMethod.getRequest(apiKey, responseID.get(getCount));

		JsonPath js = new JsonPath(obtainedResponse.asString());

		// validating external_id
		logger.info("validating external_id ... ");
		String actualExternalId = js.getString("external_id");
		softAssert.assertEquals(actualExternalId, external_id);

		// validating longitude
		logger.info("validating longitude ... ");
		double actualLongitude = js.getDouble("longitude");
		softAssert.assertEquals(actualLongitude, longitude);

		// validating latitude
		logger.info("validating latitude ... ");
		double actualLatitude = js.getDouble("latitude");
		softAssert.assertEquals(actualLatitude, latitude);

		// validating altitude
		logger.info("validating altitude ... ");
		int actualAltitude = js.getInt("altitude");
		softAssert.assertEquals(actualAltitude, altitude);

		softAssert.assertAll("");

		getCount++;
	}

	@Test(enabled = true, priority = 2, dependsOnMethods = { "addStation" })
	public void deleteStation() {

		test = extent.createTest("deleteStation");

		for (int i = 0; i < responseID.size(); i++) {

			logger.info("Station ID to Delete : " + responseID.get(i));

			// Get the DELETE method response
			response = deleteMethod.DeleteRequest(apiKey, responseID.get(i));
			logger.info("Response body : " + response.body().asString());

			// Verify the DELETE method response status code is Created (204)
			Assert.assertEquals(response.statusCode(), 204);
			logger.info("Status code response for DELETE Request : " + response.statusCode());

			// Sending the DELETE request for the same ID to ensure the ID is already
			// deleted. Expected response code should be 404
			response = deleteMethod.DeleteRequest(apiKey, responseID.get(i));
			logger.info("Response body : " + response.body().asString());

			// Verify the DELETE method response status code is Created (204)
			Assert.assertEquals(response.statusCode(), 404);
			logger.info("Status code response for DELETE Request with same ID again : " + response.statusCode());

		}

	}

	@DataProvider(name = "StationDetails")
	public Object[][] testData() {
		return new Object[][] { { "DEMO_TEST001", "Demo Station", 33.33, -111.43, 444 },
				{ "DEMO_TEST002", "Demo Station", 33.44, -12.44, 444 } };
	}

}
