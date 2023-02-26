package com.asgardiateam.aptekaproject.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

import static com.asgardiateam.aptekaproject.common.ResponseData.errorResponseData;
import static com.asgardiateam.aptekaproject.constants.MessageKey.*;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;
import static org.springframework.http.ResponseEntity.badRequest;

@Log4j2
@RestController
@ControllerAdvice
@RequiredArgsConstructor
@SuppressWarnings("NullableProblems")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error(ex);
        return ResponseEntity.internalServerError().body(errorResponseData(INTERNAL_ERROR));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        log.error(Arrays.toString(ex.getStackTrace()));

        String message = isNull(ex.getBindingResult().getFieldError()) ? PARAMETERS_NOT_VALID : ex.getBindingResult().getFieldError().getDefaultMessage();

        return badRequest().body(errorResponseData(message));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return badRequest().body(errorResponseData(PARAMETERS_NOT_VALID));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentials(BadCredentialsException ex) {
        log.error(ex);
        return ResponseEntity.badRequest().body(errorResponseData(BAD_CREDENTIALS));
    }

    @ExceptionHandler(AptekaException.class)
    public ResponseEntity<?> handleBadRequestAlertException(AptekaException ex) {
        log.error(ex);
        return badRequest().body(errorResponseData(ex.getMessage()));
    }


}
