package com.asgardiateam.aptekaproject.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "data", "errorMessage", "timestamp"})
public class ResponseData<T> {

    private T data;

    private String errorMessage;

    private Long timestamp;

    public static <T> ResponseData<T> responseData(T t) {
        return new ResponseData<>(t, null, System.currentTimeMillis());
    }

    public static ResponseData<?> errorResponseData(String key) {
        return new ResponseData<>(null, key, System.currentTimeMillis());
    }

    public static <T> ResponseEntity<ResponseData<T>> ok(T t) {
        return ResponseEntity.ok(ResponseData.responseData(t));
    }
}