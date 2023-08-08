package uz.duol.blogging.dao.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.duol.blogging.domain.ThemeEntity;

public interface ThemeRepository extends JpaRepository<ThemeEntity, Long> {

}
