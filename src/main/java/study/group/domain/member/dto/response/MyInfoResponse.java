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
public class MyInfoResponse {

  private long memberId;
  private String name;
  private int age;
  private String nickName;
  private String email;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static MyInfoResponse toDto(Member member) {
    return MyInfoResponse.builder()
        .memberId(member.getId())
        .name(member.getName())
        .age(member.getAge())
        .nickName(member.getNickName())
        .email(member.getEmail())
        .createdAt(member.getCreatedAt())
        .modifiedAt(member.getModifiedAt())
        .build();
  }
}