package study.group.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.group.domain.study.entity.Study;

public interface StudyRepository extends JpaRepository<Study, Long> {

}
