package study.group.domain.apply.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamInfoResponse {
  private Long studyId;
  private List<TeamMember> teamMembers;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TeamMember {
    private Long memberId;
    private String nickname;
    private String email;
  }
}
