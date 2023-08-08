package uz.duol.blogging.exception;

import org.hibernate.loader.custom.NonUniqueDiscoveredSqlAliasException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import uz.duol.blogging.exchange.ApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionTranslator {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Map<String, List<String>>>> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
    ApiResponse<Map<String, List<String>>> response = new ApiResponse<>();
    response.setStatus(HttpStatus.BAD_REQUEST.name());
    response.setData(getErrorsMap(errors));
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler({ResourceNotFoundException.class})
  public ResponseEntity<ApiResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex, NativeWebRequest request) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message(ex.getMessage())
            .status(HttpStatus.NOT_FOUND.name())
            .data(null)
            .build());
  }

  @ExceptionHandler({BadRequestException.class})
  public ResponseEntity<ApiResponse> handleBadRequestException(
      ResourceNotFoundException ex, NativeWebRequest request) {
    return ResponseEntity.ok(
        ApiResponse.builder()
            .message(ex.getMessage())
            .status(HttpStatus.BAD_REQUEST.name())
            .data(null)
            .build());
  }

  private Map<String, List<String>> getErrorsMap(List<String> errors) {
    Map<String, List<String>> errorResponse = new HashMap<>();
    errorResponse.put("errors", errors);
    return errorResponse;
  }
}
