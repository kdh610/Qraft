package Qraft.newsalert.presentation.dto;

import Qraft.newsalert.domain.entity.News;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class SaveNewsRequestDto {

    private String id;
    private String title;
    private String content;
    private LocalDateTime publishedAt;

    public static News toEntity(SaveNewsRequestDto requestDto) {
        return News.builder()
                .id(requestDto.getId())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .publishedAt(requestDto.getPublishedAt())
                .build();
    }
}
