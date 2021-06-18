package org.weatherapi;

import java.util.Random;

import org.json.JSONObject;

public class PayloadRequest {

	public JSONObject createRequest(String external_id, String name, double latitude, double longitude,
			int altitude) {

		Random rand = new Random();
		JSONObject requestStation = new JSONObject();

		requestStation.put("external_id", external_id);
		requestStation.put("name", name + rand.nextInt(1000));
		requestStation.put("latitude", latitude);
		requestStation.put("longitude", longitude);
		requestStation.put("altitude", altitude);

		return requestStation;

	}
}
