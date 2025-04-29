package study.group.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.group.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
