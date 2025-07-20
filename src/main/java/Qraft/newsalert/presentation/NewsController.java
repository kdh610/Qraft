package Qraft.newsalert.presentation;

import Qraft.newsalert.application.service.NewsService;
import Qraft.newsalert.presentation.dto.SaveNewsRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping("/news")
    public String postNews(@RequestBody SaveNewsRequestDto newsRequest) {
        newsService.saveNews(SaveNewsRequestDto.toEntity(newsRequest));
        return "News posted successfully: ";
    }

}
