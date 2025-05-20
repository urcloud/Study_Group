package study.group.domain.study.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import study.group.domain.study.entity.Category;
import study.group.domain.study.entity.Enum.AddrDo;
import study.group.domain.study.entity.Enum.AddrGu;
import study.group.domain.study.entity.Enum.CategoryType;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByTypeAndAddrDoAndAddrGu(CategoryType type, AddrDo addrDo, AddrGu addrGu);
}
