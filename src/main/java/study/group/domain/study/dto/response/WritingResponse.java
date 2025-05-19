package study.group.domain.study.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.group.domain.study.entity.Enum.AddrDo;
import study.group.domain.study.entity.Enum.AddrGu;
import study.group.domain.study.entity.Enum.CategoryType;
import study.group.domain.study.entity.Study;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WritingResponse {
  private long studyId;
  private long memberId;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String title;
  private String content;
  private LocalDateTime closedAt;
  private int limitedPeople;
  private int currentPeople;
  private boolean online;
  private int totalLikes;
  private CategoryType type;
  private AddrDo regionDo;
  private AddrGu regionGu;

  public static WritingResponse toDto(Study study) {
    return WritingResponse.builder()
        .studyId(study.getId())
        .memberId(study.getMember().getId())
        .createdAt(study.getCreatedAt())
        .modifiedAt(study.getModifiedAt())
        .title(study.getTitle())
        .content(study.getContent())
        .closedAt(study.getClosedAt())
        .limitedPeople(study.getLimitedPeople())
        .currentPeople(study.getCurrentPeople())
        .online(study.isOnline())
        .totalLikes(study.getTotalLikes())
        .type(study.getCategory().getType())
        .regionDo(study.getCategory().getAddrDo())
        .regionGu(study.getCategory().getAddrGu())
        .build();
  }
}
