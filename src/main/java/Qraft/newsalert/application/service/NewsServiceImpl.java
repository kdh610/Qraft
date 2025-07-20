package Qraft.newsalert.application.service;

import Qraft.newsalert.application.port.out.MessageProducerPort;
import Qraft.newsalert.domain.entity.News;
import Qraft.newsalert.domain.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final MessageProducerPort messageProducer;

    /**
     * 번역된 뉴스를 DB에 저장하고, 메세지 큐에 뉴스의 고유ID를 전송합니다.
     * @param news
     */
    @Override
    public void saveNews(News news) {
        newsRepository.save(news);
        messageProducer.send(news.getId());
    }


}
