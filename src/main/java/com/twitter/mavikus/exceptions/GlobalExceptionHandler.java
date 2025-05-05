package com.twitter.mavikus.exceptions;/*
Interceptor - Hatalarda Araya Girer
*/

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler // Otomatik algılamazsa bunu ekle (MaviKusErrorException.class)
    public ResponseEntity<MaviKusErrorResponse> handleException(MaviKusErrorException theException) {
        MaviKusErrorResponse errorResponse = new MaviKusErrorResponse();

        errorResponse.setStatusCode(theException.getHttpStatus().value());
        errorResponse.setMessage(theException.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, theException.getHttpStatus());
    }

    // Bu Global Hata Yakalama Kısmı, gelen hatayı INTERNAL_SERVER_ERROR olarak ayarlar.
    @ExceptionHandler // Otomatik algılamazsa bunu ekle (Exception.class)
    public ResponseEntity<MaviKusErrorResponse> handleException(Exception theException) {
        MaviKusErrorResponse errorResponse = new MaviKusErrorResponse();

        errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage(theException.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
