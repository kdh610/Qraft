package Qraft.newsalert.domain.repository;

import Qraft.newsalert.domain.entity.News;

import java.util.Optional;

public interface NewsRepository {
    Optional<News> findById(String id);
}
