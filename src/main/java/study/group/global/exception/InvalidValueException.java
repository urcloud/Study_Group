package study.group.global.exception;

import lombok.Getter;

@Getter
public class InvalidValueException extends CustomException {
  public InvalidValueException(ErrorCode errorCode) {
    super(errorCode);
  }
}
