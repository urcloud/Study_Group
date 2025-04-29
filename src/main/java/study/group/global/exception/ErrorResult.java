package study.group.global.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ErrorResult {
  private Integer resultCode;
  private String resultMessage;

  private static String concatErrorCodeAndMessage(ErrorCode errorCode) {
    return errorCode.getMessage();
  }

  public static ErrorResult Error(ErrorCode errorCode) {
    return ErrorResult.builder()
        .resultCode(errorCode.getStatus())
        .resultMessage(concatErrorCodeAndMessage(errorCode))
        .build();
  }

  @Builder
  private ErrorResult(Integer resultCode, String resultMessage) {
    Assert.notNull(resultCode, "resultCode must not be null");
    Assert.notNull(resultMessage, "resultMessage must not be null");
    this.resultCode = resultCode;
    this.resultMessage = resultMessage;
  }
}