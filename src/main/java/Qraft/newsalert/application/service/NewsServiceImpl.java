package Qraft.newsalert.application.service;

import Qraft.newsalert.domain.entity.News;
import Qraft.newsalert.domain.repository.NewsRepository;
import Qraft.newsalert.infrastructure.adapter.in.MessageConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final MessageConsumer messageConsumer;

    @Override
    public News getNewsById(Long id){
        String newsId = messageConsumer.receiveMessage();
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + newsId));
    }
}
