package com.alco.algorithmic.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.webjars.NotFoundException;

import java.io.FileNotFoundException;


@AllArgsConstructor
@ControllerAdvice
public class ErrorResponseWrapper {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleJwtExceptions(@NonNull HttpServletResponse response){
        Cookie cookie = new Cookie("Auth-Key", "");
        cookie.setPath("/api");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RequestBodyError.class)
    public ResponseEntity<String> handleBodySizeErrors(RequestBodyError ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleBodyParseErrors(HttpMessageNotReadableException ex){
        return new ResponseEntity<>("Body is empty", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleMediaTypeException(HttpMediaTypeNotSupportedException ex) {
        return new ResponseEntity<>("Media Type not supported", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleLoginExceptions() {
        return new ResponseEntity<>("Incorrect data", HttpStatus.valueOf(401));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleDefaultErrors(ResponseStatusException ex){
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFound(NoHandlerFoundException ex){
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException ex){
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex){
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<String> handleBodySizeErrors(SizeLimitExceededException ex){
        return new ResponseEntity<>("Maximum size exceeded(500MB)", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>("Method not allow", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException1(Exception ex){
        ex.printStackTrace(System.out);
        return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleAllException2(RuntimeException ex){
        ex.printStackTrace(System.out);
        return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
