package study.group.global.exception;

import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//서비스 코드에서 예외 발생 - CustomException(혹은 하위 클래스) throw - GlobalExceptionHandler에서 해당 Exception 잡음
//ErrorResult 생성 - ErrorResponse로 wrapping - HTTP 응답 + JSON 반환
@Slf4j
@Priority(Integer.MAX_VALUE)
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Void> handleGlobalException(Exception exception) {
    log.error("", exception);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse<Object>> handleNotFoundException(
      NotFoundException e) {
    return ResponseEntity.status(e.getErrorCode().getStatus())
        .body(ErrorResponse.ERROR(e.getErrorCode()));
  }

  @ExceptionHandler(InvalidValueException.class)
  public ErrorResponse<Void> handleInvalidValueException(InvalidValueException e) {
    return ErrorResponse.<Void>builder().errorResult(ErrorResult.Error(e.getErrorCode())).build();
  }

  @ExceptionHandler(CustomException.class)
  public ErrorResponse<Void> handleCustomException(CustomException e) {
    return ErrorResponse.<Void>builder().errorResult(ErrorResult.Error(e.getErrorCode())).build();
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorResponse<Object>> ValidationException(
      MethodArgumentNotValidException exception) {
    log.error("", exception);

    return ResponseEntity.status(ErrorCode.BAD_REQUEST.getStatus())
        .body(ErrorResponse.ERROR(ErrorCode.BAD_REQUEST));
  }

  /**
   * 정의한 enum 이외의 값을 입력했을때 발생하는 예외
   * @param exception
   * @return
   */
  @ExceptionHandler({HttpMessageNotReadableException.class})
  public ResponseEntity<ErrorResponse<Object>> HttpMessageNotReadableException(
      HttpMessageNotReadableException exception) {
    log.error("", exception);

    return ResponseEntity.status(ErrorCode.BAD_REQUEST.getStatus())
        .body(ErrorResponse.ERROR(ErrorCode.BAD_REQUEST));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse<Object>> ValidationException(
      MissingServletRequestParameterException exception) {
    log.error("", exception);

    return ResponseEntity.status(ErrorCode.BAD_REQUEST.getStatus())
        .body(ErrorResponse.ERROR(ErrorCode.BAD_REQUEST));
  }
}