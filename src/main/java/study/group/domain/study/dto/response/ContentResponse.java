package study.group.domain.study.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.group.domain.study.entity.Study;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponse  {
  private long studyId;
  private String title;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private int limitedPeople;
  private int currentPeople;
  private boolean online;
  private int totalLikes;
  private String type;
  private String regionDo;
  private String regionGu;
  private long memberId;
  private String nickname;
  private boolean likedByCurrentUser;

  public static ContentResponse toDto(Study study, boolean likedByCurrentUser) {
    return ContentResponse.builder()
        .studyId(study.getId())
        .title(study.getTitle())
        .content(study.getContent())
        .createdAt(study.getCreatedAt())
        .modifiedAt(study.getModifiedAt())
        .limitedPeople(study.getLimitedPeople())
        .currentPeople(study.getCurrentPeople())
        .online(study.isOnline())
        .totalLikes(study.getTotalLikes())
        .type(study.getCategory().getType())
        .regionDo(study.getCategory().getAddrDo())
        .regionGu(study.getCategory().getAddrGu())
        .memberId(study.getMember().getId())
        .likedByCurrentUser(likedByCurrentUser)
        .build();
  }
}
