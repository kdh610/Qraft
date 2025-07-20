package Qraft.newsalert.infrastructure.jpa;

import Qraft.newsalert.domain.entity.News;
import Qraft.newsalert.domain.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NewsRepositoryImpl implements NewsRepository {

    private final NewsJpaRepository newsJpaRepository;

    @Override
    public Optional<News> findById(String id) {
        return newsJpaRepository.findById(id);
    }

    @Override
    public void save(News news) {
        newsJpaRepository.save(news);
    }
}
