package com.UserMS.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardResponseStructure {
    private String responseCode;
    private String errorCode;
    private String errorDescription;
    private LocalDateTime responseTime;
    private Object responseData;

    public static StandardResponseStructure success(Object data) {
        StandardResponseStructure response = new StandardResponseStructure();
        response.setResponseCode("200");
        response.setErrorCode(null);
        response.setErrorDescription(null);
        response.setResponseTime(LocalDateTime.now());
        response.setResponseData(data);
        return response;
    }

    public static StandardResponseStructure error(String responseCode, String errorCode, String errorDescription) {
        StandardResponseStructure response = new StandardResponseStructure();
        response.setResponseCode(responseCode);
        response.setErrorCode(errorCode);
        response.setErrorDescription(errorDescription);
        response.setResponseTime(LocalDateTime.now());
        response.setResponseData(null);
        return response;
    }
}
