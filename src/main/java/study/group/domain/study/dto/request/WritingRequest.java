package study.group.domain.study.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WritingRequest {
  private String title;
  private String content;
  private LocalDateTime closedAt;
  private int limitedPeople;
  private boolean online;
  private String type;
  private String regionDo;
  private String regionGu;
}
