package responses;

import responses.sysadmin.AddGenomeReleaseResponse;
import util.AnnotationDataType;
import util.ExperimentData;
import util.GenomeReleaseData;
import util.ProcessFeedbackData;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class ResponseParser {
    
    private static Gson gson = new Gson();
    
    public static LoginResponse parseLoginResponse(String json) {
        LoginResponse loginResponse;
        try {
            loginResponse = gson.fromJson(json, LoginResponse.class);
        } catch (JsonParseException e) {
            return null;
        }
        return loginResponse;
    }
    
    public static ExperimentData parseRetrieveExp(String json) {
        ExperimentData retrieveExpResponse;
        try {
            retrieveExpResponse = gson.fromJson(json, ExperimentData.class);
        } catch (JsonParseException e) {
            return null;
        }
        return retrieveExpResponse;
    }
    
    public static ExperimentData[] parseSearchResponse(String json) {
        ExperimentData[] searchResponses;
        try {
            searchResponses = gson.fromJson(json, ExperimentData[].class);
        } catch (JsonParseException e) {
            return null;
        }
        return searchResponses;
    }
    
    public static AnnotationDataType[] parseGetAnnotationResponse(String json) {
        AnnotationDataType[] annotationResponses;
        try {
            annotationResponses = gson.fromJson(json,
                    AnnotationDataType[].class);
        } catch (JsonParseException e) {
            return null;
        }
        return annotationResponses;
    }
    
    public static GenomeReleaseData[] parseGetGenomeReleaseResponse(String json) {
        
        GenomeReleaseData[] genomeReleaseResponses;
        try {
            genomeReleaseResponses = gson.fromJson(json,
                    GenomeReleaseData[].class);
            
        } catch (JsonParseException e) {
            
            System.out.println("Could not parse json GR.");
            return null;
        }
        
        return genomeReleaseResponses;
    }
    
    public static AddFileToExperimentResponse parseUploadResponse(String json) {
        AddFileToExperimentResponse url = null;
        try {
            url = gson.fromJson(json, AddFileToExperimentResponse.class);
        } catch (JsonParseException e) {
            System.out.println("parse error");
        }
        return url;
    }
    
    public static AddGenomeReleaseResponse[] parseGenomeUploadResponse(
            String json) {
        
        AddGenomeReleaseResponse[] urls = null;
        try {
            urls = gson.fromJson(json, AddGenomeReleaseResponse[].class);
            
        } catch (JsonParseException e) {
            
            System.err
                    .println("Could not parse url response for adding genome release.");
        }
        
        return urls;
    }
    
    public static ProcessFeedbackData[] parseProcessFeedbackResponse(String json) {
        ProcessFeedbackData[] processFeedbackData;
        try {
            processFeedbackData = gson.fromJson(json,
                    ProcessFeedbackData[].class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        }
        return processFeedbackData;
    }
    
    public static ErrorResponse parseErrorResponse(String json) {
        
        ErrorResponse response;
        try {
            response = gson.fromJson(json, ErrorResponse.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }
}
