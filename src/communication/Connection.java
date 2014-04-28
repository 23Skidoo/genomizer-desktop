package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import requests.Request;

public class Connection {
	private String ip;
	private int port;
	private int responseCode;
	private String responseBody;

	public Connection(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public boolean sendRequest(Request request, String userID, String type) {
		try {
			String targetUrl = "http://" + ip + ":" + port + request.url;

			System.out.println(targetUrl);
			URL url = new URL(targetUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
            connection.setReadTimeout(1000);
			connection.setRequestMethod(request.type);
			connection.setRequestProperty("Content-Type", type);
			if(!userID.isEmpty()) {
				connection.setRequestProperty("Authorization", userID);
			}
			PrintWriter outputStream = new PrintWriter(
					connection.getOutputStream(), true);
			outputStream.println(request.toJson());
			outputStream.flush();
			responseCode = connection.getResponseCode();
			if (responseCode >= 300) {
				System.out.println("Connection error: " + responseCode);
				return false;
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String buffer;
			StringBuilder output = new StringBuilder();
			while ((buffer = in.readLine()) != null) {
				output.append(buffer);
			}
			responseBody = output.toString();
			connection.disconnect();
		} catch (IOException e) {
			System.out.println("Connection error: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public int getResponseCode() {
		return responseCode;
	}
	
	public String getResponseBody() {
		return responseBody;
	}

	public void checkType(String output) {

	}

}
