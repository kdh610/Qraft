package Qraft.newsalert.application.service;

import Qraft.newsalert.application.port.out.MessageProducerPort;
import Qraft.newsalert.domain.entity.News;
import Qraft.newsalert.domain.repository.NewsRepository;
import Qraft.newsalert.application.port.in.MessageConsumerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final MessageConsumerPort messageConsumer;
    private final MessageProducerPort messageProducer;

    @Override
    public News getNewsById(Long id){
        String newsId = messageConsumer.receiveMessage();
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + newsId));
    }

    @Override
    public void saveNews(News news) {
        newsRepository.save(news);
        messageProducer.send(news.getId());
    }
}
