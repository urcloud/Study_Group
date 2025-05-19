package study.group.domain.study.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import study.group.domain.study.entity.Enum.AddrDo;
import study.group.domain.study.entity.Enum.AddrGu;
import study.group.domain.study.entity.Enum.CategoryType;
import study.group.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "category")
public class Category extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CategoryType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AddrDo addrDo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AddrGu addrGu;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
  private List<Study> studies;
}