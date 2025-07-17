package com.katech.service.common.utils;

import com.katech.service.customer.dto.BaseResponse;
import com.katech.service.customer.dto.ResponseMetaData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ResponseBuilderUtils {

    public static <R> ResponseEntity<BaseResponse<R>> buildSuccessResponse(
            R responseBody, HttpServletRequest request) {
        BaseResponse<R> baseResponse = new BaseResponse<>();
        ResponseMetaData responseMetaData =
                ResponseMetaData.builder()
                        .httpCode(HttpStatus.OK.value())
                        .path(request.getServletPath())
                        .message(HttpStatus.OK.getReasonPhrase())
                        .responseTraceId(String.valueOf(request.getAttribute("traceId")))
                        .build();
        baseResponse.setMetadata(responseMetaData);
        baseResponse.setData(responseBody);
        return ResponseEntity.ok().body(baseResponse);
    }

    public static <R> ResponseEntity<BaseResponse<R>> buildSuccessResponse(
            R responseBody, HttpServletRequest request, HttpSession session) {
        BaseResponse<R> baseResponse = new BaseResponse<>();
        ResponseMetaData responseMetaData =
                ResponseMetaData.builder()
                        .httpCode(HttpStatus.OK.value())
                        .path(request.getServletPath())
                        .message(HttpStatus.OK.getReasonPhrase())
                        .responseTraceId(String.valueOf(request.getAttribute("traceId")))
                        .sessionId(session.getId())
                        .build();
        baseResponse.setMetadata(responseMetaData);
        baseResponse.setData(responseBody);
        return ResponseEntity.ok().body(baseResponse);
    }
}
