package study.group.domain.study.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import study.group.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "category")
public class Category extends BaseEntity {

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private String addrDo;

  @Column(nullable = false)
  private String addrGu;

  @OneToOne(mappedBy = "category")
  private Study study;
}