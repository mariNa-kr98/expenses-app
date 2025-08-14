package gr.aueb.cf.expensesApp.core.exceptions;

import gr.aueb.cf.expensesApp.dto.ResponseMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler{

    // Handle custom AppException
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseMessageDTO> handleAppException(AppException ex) {

    ResponseMessageDTO response = new ResponseMessageDTO(ex.getErrorCode(), ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }

    // Handle JSON parse errors (e.g., enum mismatch)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseError(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>("Invalid request format: " + ex.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Handle unexpected errors (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        ex.printStackTrace(); // for console visibility
        return new ResponseEntity<>("Unexpected error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
