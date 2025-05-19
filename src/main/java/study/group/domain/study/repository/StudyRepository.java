package study.group.domain.study.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.group.domain.study.entity.Study;

public interface StudyRepository extends JpaRepository<Study, Long> {
  boolean existsByIdAndMemberId(Long studyId, Long memberId);

  @Query("SELECT s FROM Study s WHERE s.closedAt < CURRENT_TIMESTAMP OR s.currentPeople >= s.limitedPeople")
  List<Study> findCompletedStudies();

  @Query("SELECT s FROM Study s WHERE s.closedAt >= CURRENT_TIMESTAMP AND s.currentPeople < s.limitedPeople")
  List<Study> findOngoingStudies();

  @Query("""
    SELECT s FROM Study s
    JOIN FETCH s.member m
    WHERE (s.closedAt < CURRENT_TIMESTAMP OR s.currentPeople >= s.limitedPeople)
      AND m.id = :memberId
      AND s.id = :studyId
""")
  Optional<Study> findCompletedStudyByIdAndMemberId(@Param("studyId") Long studyId, @Param("memberId") Long memberId);
}
