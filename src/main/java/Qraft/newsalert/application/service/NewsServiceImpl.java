package Qraft.newsalert.application.service;

import Qraft.newsalert.application.port.out.MessageProducerPort;
import Qraft.newsalert.domain.entity.News;
import Qraft.newsalert.domain.repository.NewsRepository;
import Qraft.newsalert.application.port.in.MessageConsumerPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final MessageConsumerPort messageConsumer;
    private final MessageProducerPort messageProducer;

    @Override
    public void getNewsById(){
        messageConsumer.receiveMessage();
    }

    @Override
    public void saveNews(News news) {
        newsRepository.save(news);
        messageProducer.send(news.getId());
    }


}
