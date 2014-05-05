<<<<<<< HEAD
package responses;

import util.ExperimentData;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class ResponseParser {

    private static Gson gson = new Gson();

    public static LoginResponse parseLoginResponse(String json) {
	LoginResponse loginResponse = null;
	try {
	    loginResponse = gson.fromJson(json, LoginResponse.class);
	} catch (JsonParseException e) {
	    return null;
	}
	return loginResponse;
    }

    public static ExperimentData[] parseSearchResponse(String json) {
    	ExperimentData[] searchResponses = null;
	try {
	    searchResponses = gson.fromJson(json, ExperimentData[].class);
	} catch (JsonParseException e) {
	    return null;
	}
	return searchResponses;
    }
}
=======
package responses;

import util.ExperimentData;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class ResponseParser {

    private static Gson gson = new Gson();

    public static LoginResponse parseLoginResponse(String json) {
	LoginResponse loginResponse = null;
	try {
	    loginResponse = gson.fromJson(json, LoginResponse.class);
	} catch (JsonParseException e) {
	    return null;
	}
	return loginResponse;
    }

    public static ExperimentData[] parseSearchResponse(String json) {
    	ExperimentData[] searchResponses = null;
	try {
	    searchResponses = gson.fromJson(json, ExperimentData[].class);
	} catch (JsonParseException e) {
	    return null;
	}
	return searchResponses;
    }
}
>>>>>>> branch 'master' of https://github.com/genomizer/genomizer-desktop.git
