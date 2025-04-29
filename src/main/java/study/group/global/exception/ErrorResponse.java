package study.group.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ErrorResponse<T> {
  private ErrorResult errorResult;
  private T body;

  public static ErrorResponse<Object> ERROR(ErrorCode errorCode) {
    return ErrorResponse.<Object>builder().errorResult(ErrorResult.Error(errorCode)).build();
  }
}