package study.group.global.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends CustomException {
  public NotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
