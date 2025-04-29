package study.group.global.exception;

public class InvalidValueException extends CustomException {
  public InvalidValueException(ErrorCode errorCode) {
    super(errorCode);
  }
}
