package com.example.FlowPay.Exception;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {


    @ExceptionHandler(ApiException.class)
    ResponseEntity<?> business(ApiException e){
    return  ResponseEntity.status(e.status).body(Map.of("message", e.getMessage()));
    }

// if exception occured;

    // 1. Validation errors: @Valid, @NotBlank, @Email, etc.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.putIfAbsent(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity.badRequest().body(errors);
    }

    // 2. Malformed JSON or unreadable request body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableRequest(
            HttpMessageNotReadableException ex) {

        return ResponseEntity.badRequest()
                .body(new ApiError("Invalid request body or JSON format"));
    }

    // 3. Invalid path variable or query parameter type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        return ResponseEntity.badRequest()
                .body(new ApiError(
                        "Invalid value for parameter: " + ex.getName()
                ));
    }

//    if duplicate transaction initiated
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<?> duplicate(ApiException e){
        return ResponseEntity.status(409).body(Map.of("message","duplicate or conflict record"));
    }

}
