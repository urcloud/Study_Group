package study.group.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum ErrorCode {

  // Common
  BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "400", "요청 파라미터나, 요청 바디의 값을 다시 확인하세요"),
  JSON_CONVERSION_FAILED(
      HttpStatus.INTERNAL_SERVER_ERROR.value(), "500", "JSON 형식이 아니라 변환에 실패했습니다"),
  INVALID_INPUT_VALUE(400, "C001", "유효하지 않은 입력입니다."),
  NOT_FOUND(404, "C002", "Not Found"),
  NO_QUERY_RESULT(404, "C003", "No Query Result"),
  DATETIME_INVALID(400, "C004", "유효하지 않은 날짜입니다"),
  NO_AUTHORIZATION(401, "C005", "권한이 없습니다"),

  // Member
  EMAIL_DUPLICATION(409, "M001", "이미 등록된 이메일입니다."),
  NICKNAME_DUPLICATION(409, "M002", "이미 존재하는 닉네임입니다."),
  PASSWORD_MISMATCH(400, "M003", "패스워드가 일치하지 않습니다."),
  MEMBER_NOT_FOUND(404, "M004", "존재하지 않는 회원 정보입니다"),
  NICKNAME_CHANGE_FAILED(400, "M005", "30일 후 닉네임 변경이 가능합니다."),
  LOGIN_FIRST(401, "M006", "로그인이 되어있지 않습니다."),

  // Study
  STUDY_NOT_FOUND(404, "S001", "존재하지 않는 글입니다."),
  STUDY_FAILED(400, "S002", "글 작성에 실패했습니다."),

  // Apply
  APPLY_FAILED(400, "A001", "신청에 실패했습니다."),
  CANCEL_APPLY_FAILED(400, "A002", "신청 취소에 실패했습니다."),
  APPLY_ALREADY(400, "A003", "이미 신청했습니다."),
  APPLY_NOT_FOUND(404, "A004", "신청내역이 존재하지 않습니다."),

  // Like
  LIKED_FAILED(400, "L001", "관심목록 추가에 실패했습니다."),

  // Comment
  COMMENT_FAILED(400, "E001", "댓글 추가에 실패했습니다.")
  ;


  private final Integer status;
  private final String code;
  private final String message;
}
