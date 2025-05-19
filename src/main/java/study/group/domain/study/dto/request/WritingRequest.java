package study.group.domain.study.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.group.domain.study.entity.Enum.AddrDo;
import study.group.domain.study.entity.Enum.AddrGu;
import study.group.domain.study.entity.Enum.CategoryType;

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
  private CategoryType type;
  private AddrDo regionDo;
  private AddrGu regionGu;
}
