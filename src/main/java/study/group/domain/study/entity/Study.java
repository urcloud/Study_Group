package study.group.domain.study.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import study.group.domain.apply.entity.Apply;
import study.group.domain.comment.entity.Comment;
import study.group.domain.likes.entity.Likes;
import study.group.domain.member.entity.Member;
import study.group.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "study")
public class Study extends BaseEntity {

  @Column(nullable = false, columnDefinition = "TEXT")
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false)
  private LocalDateTime closedAt;

  @Column(nullable=false)
  private int limitedPeople;

  @Column(nullable=false)
  private int currentPeople;

  @Column(nullable = false)
  private boolean online;

  @Column(nullable = false)
  private int totalLikes;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
  private List<Apply> applies;

  @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
  private List<Likes> likes;

  @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
  private List<Comment> comments;

  public void update(LocalDateTime modifiedAt, String title, String content, LocalDateTime closedAt, int limitedPeople, boolean online, Category category) {
    this.modifiedAt = modifiedAt;
    this.title = title;
    this.content = content;
    this.closedAt = closedAt;
    this.limitedPeople = limitedPeople;
    this.online = online;
    this.category = category;
  }
}
