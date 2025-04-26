package study.group.domain.member.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import study.group.domain.apply.entity.Apply;
import study.group.domain.comment.entity.Comment;
import study.group.domain.likes.entity.Likes;
import study.group.domain.study.entity.Study;
import study.group.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "member")
public class Member extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private int age;

  @Column(nullable = false)
  private String nickName;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Apply> applies;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Study> studies;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Likes> likes;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Comment> comments;
}
