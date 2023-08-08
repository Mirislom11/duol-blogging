package uz.duol.blogging.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.duol.blogging.domain.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {}

