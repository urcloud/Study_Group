package study.group.domain.study.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.group.domain.study.entity.Study;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListResponse {
  private long studyId;
  private String title;

  public static ListResponse toDto(Study study) {
    return ListResponse.builder()
        .studyId(study.getId())
        .title(study.getTitle())
        .build();
  }
}
