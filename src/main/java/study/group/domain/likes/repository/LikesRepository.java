package study.group.domain.likes.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import study.group.domain.likes.entity.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long> {
  boolean existsByMemberIdAndStudyId(Long memberId, Long studyId);
  Likes findByMemberIdAndStudyId(Long memberId, Long studyId);
}
