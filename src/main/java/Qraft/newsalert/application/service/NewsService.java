package Qraft.newsalert.application.service;

import Qraft.newsalert.domain.entity.News;

public interface NewsService {
    News getNewsById(Long id) throws InterruptedException;

}
