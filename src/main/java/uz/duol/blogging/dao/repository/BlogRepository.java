package uz.duol.blogging.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import uz.duol.blogging.domain.BlogEntity;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<BlogEntity, Long>,
        JpaSpecificationExecutor<BlogEntity> {

    @Query("""
            SELECT b FROM BlogEntity b\s
            JOIN b.comments c\s
            WHERE b.id = :id""")
    Optional<BlogEntity> findBlogEntityWithComments (@Param("id") Long id);

    @Modifying
    @Query("""
            UPDATE BlogEntity\s
            SET numberViews = (numberViews + 1),
            lastUpdatedDate = CURRENT_TIMESTAMP \n
            WHERE id in (:ids)
            """)
    void updateNumberViews (@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {"theme"})
    @Override
    Page<BlogEntity> findAll(Specification<BlogEntity> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"theme"})
    @Override
    Optional<BlogEntity> findById(Long aLong);
}
