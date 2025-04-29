package study.group.domain.member.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.group.domain.member.entity.Member;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {

  private long memberId;
  private String name;
  private int age;
  private String nickName;
  private String email;
  private LocalDateTime createdAt;

  public static SignupResponse toDto(Member member) {
    return SignupResponse.builder()
        .memberId(member.getId())
        .name(member.getName())
        .age(member.getAge())
        .nickName(member.getNickName())
        .email(member.getEmail())
        .createdAt(member.getCreatedAt())
        .build();
  }
}
