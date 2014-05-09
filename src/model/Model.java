package model;

import java.util.ArrayList;

import communication.UploadHandler;
import requests.AddAnnotationRequest;
import requests.AddFileToExperiment;
import requests.DeleteAnnotationRequest;
import requests.DownloadFileRequest;
import requests.GetAnnotationRequest;
import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RequestFactory;
import requests.SearchRequest;
import requests.rawToProfileRequest;
import responses.DownloadFileResponse;
import responses.LoginResponse;
import responses.ResponseParser;
import util.AnnotationDataType;
import util.DeleteAnnoationData;
import util.ExperimentData;

import com.google.gson.Gson;
import communication.Connection;
import communication.DownloadHandler;
import communication.HTTPURLUpload;

public class Model implements GenomizerModel {

	private String userID = "";
	private Connection conn;
	private SearchHistory searchHistory;

	public Model(Connection conn) {
		searchHistory = new SearchHistory();
		this.setConn(conn);
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}


	@Override
	public boolean rawToProfile(String fileName, String fileID, String expid,
			String processtype, String[] parameters, String metadata,
			String genomeRelease, String author) {


				rawToProfileRequest rawToProfilerequest = RequestFactory
									.makeRawToProfileRequest(fileName, fileID, expid,
											processtype, parameters, metadata,
										 genomeRelease, author);

				conn.sendRequest(rawToProfilerequest, userID, "text/plain");
				if (conn.getResponseCode() == 201) {
					return true;
				} else {
					System.out.println("Response Code: " + conn.getResponseCode());
					return false;
				}
	}

	@Override
	public boolean loginUser(String username, String password) {
		if (!username.isEmpty() && !password.isEmpty()) {
			System.out.println("login test");
			LoginRequest request = RequestFactory.makeLoginRequest(username,
					password);
			conn.sendRequest(request, userID, "application/json");
			if (conn.getResponseCode() == 200) {
				LoginResponse loginResponse = ResponseParser
						.parseLoginResponse(conn.getResponseBody());
				if (loginResponse != null) {
					userID = loginResponse.token;
					System.out.println(userID);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean logoutUser() {
		LogoutRequest request = RequestFactory.makeLogoutRequest();
		conn.sendRequest(request, userID, "text/plain");
		if (conn.getResponseCode() == 200) {
			userID = "";
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean uploadFile() {
		AddFileToExperiment request = RequestFactory.makeAddFile("test",
				"test", "1.3GB", "raw");
		conn.sendRequest(request, userID, "application/json");
		String url = conn.getResponseBody();
		if (url != null) {
			System.out.println("URL: " + url);
		}
        System.out.println("url");
        /*HTTPURLUpload handler = new HTTPURLUpload(
				"/var/www/html/uploads/test321.txt",
				"/home/dv12/dv12csr/edu/test321");
		handler.sendFile("pvt", "pvt");*/
        System.out.println("Test");
        UploadHandler handler = new UploadHandler(
		  "/var/www/html/uploads/test321.txt",
		  "/home/dv12/dv12csr/edu/test321", userID, "pvt:pvt");
        Thread thread = new Thread(handler);
        thread.start();

		return true;
	}

	@Override
	public boolean downloadFile(String url, String fileID, String path) {
        //Use this until search works on the server
		DownloadFileRequest request = RequestFactory.makeDownloadFileRequest(
				 fileID, ".wig");

        System.out.println("Test: " + fileID);
        conn.sendRequest(request, userID, "text/plain");
		Gson gson = new Gson();
		DownloadFileResponse response = gson.fromJson(conn.getResponseBody(),
				DownloadFileResponse.class);
        System.out.println(conn.getResponseBody());
        DownloadHandler handler = new DownloadHandler("pvt", "pvt");
        handler.download(url, path, userID);
		System.out.println("Test");
		return true;
	}

	@Override
	public ExperimentData[] search(String pubmedString) {
		SearchRequest request = RequestFactory.makeSearchRequest(pubmedString);
		conn.sendRequest(request, userID, "text/plain");
        if(conn.getResponseCode() == 200) {
			ExperimentData[] searchResponses = ResponseParser.parseSearchResponse(conn.getResponseBody());
			if (searchResponses != null && searchResponses.length > 0) {
				searchHistory.addSearchToHistory(searchResponses);
				return searchResponses;
			}
		}
		return null;
	}

	@Override
	public void setIp(String ip) {
		conn.setIp(ip);
	}

	@Override
	public boolean addNewAnnotation(String name, String[] categories,
			boolean forced) throws IllegalArgumentException {

		if (name.isEmpty()) {
			throw new IllegalArgumentException(
					"Must have a name for the annotation!");
		}

		AnnotationDataType[] annotations = getAnnotations();
		if (annotations == null) {
			return false;
		}
		for (int i = 0; i < annotations.length; i++) {
			AnnotationDataType a = annotations[i];
			if (a.getName().equalsIgnoreCase(name)) {
				throw new IllegalArgumentException(
						"Annotations must have a unique name, " + name
								+ " already exists");
			}
		}

		if (categories == null || categories.length == 0) {
			categories = new String[] { "Yes", "No", "Unknown" };
		}

		AddAnnotationRequest request = RequestFactory.makeAddAnnotationRequest(
				name, categories, forced);
		conn.sendRequest(request, userID, "application/json");
		if (conn.getResponseCode() == 201) {
			System.err.println("addAnnotation sent succesfully!");
			return true;
		} else {
			System.err
					.println("addAnnotaion FAILURE, did not recive 201 response");
			return false;
		}
	}

	@Override
	public boolean deleteAnnotation(DeleteAnnoationData deleteAnnoationData) {

		DeleteAnnotationRequest request = RequestFactory
				.makeDeleteAnnotationRequest(deleteAnnoationData);
		conn.sendRequest(request, userID, "application/json");
		if (conn.getResponseCode() == 200) {
			System.err.println("Annotation named " + deleteAnnoationData
					+ " deleted succesfully");
			return true;
		} else {
			System.err.println("Could not delete annotation name " + deleteAnnoationData
					+ "!");
		}
		return false;
	}

	public synchronized AnnotationDataType[] getAnnotations() {
		GetAnnotationRequest request = RequestFactory
				.makeGetAnnotationRequest();
		conn.sendRequest(request, userID, "text/plain");
		if (conn.getResponseCode() == 200) {
			System.err.println("Sent getAnnotionrequestsuccess!");
			AnnotationDataType[] annotations = ResponseParser
					.parseGetAnnotationResponse(conn.getResponseBody());
			return annotations;
		} else {
			System.out.println("responsecode: " + conn.getResponseCode());
			System.err.println("Could not get annotations!");
		}
		return new AnnotationDataType[]{};
	}

}
