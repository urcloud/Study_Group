package study.group.domain.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import study.group.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
  boolean existsByEmail(String email);
  boolean existsByNickName(String nickName);
  Optional<Member> findByEmail(String email);
}
