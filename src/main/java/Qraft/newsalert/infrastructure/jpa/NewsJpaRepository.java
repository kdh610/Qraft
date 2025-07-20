package Qraft.newsalert.infrastructure.jpa;

import Qraft.newsalert.domain.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsJpaRepository extends JpaRepository<News, String> {
}
