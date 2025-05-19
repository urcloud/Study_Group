package study.group.domain.apply.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import study.group.domain.apply.entity.Apply;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
  boolean existsByMemberIdAndStudyId(Long memberId, Long studyId);
  Optional<Apply> findByMemberIdAndStudyId(Long memberId, Long studyId);
  List<Apply> findAllByStudyId(Long studyId);
}
