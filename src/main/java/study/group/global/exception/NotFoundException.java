package study.group.global.exception;

public class NotFoundException extends CustomException {
  public NotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
