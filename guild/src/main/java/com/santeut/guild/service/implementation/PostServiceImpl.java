package com.santeut.guild.service.implementation;

import com.santeut.guild.common.exception.CategoryNotFoundException;
import com.santeut.guild.dto.request.PostCreateRequestDto;
import com.santeut.guild.entity.GuildPostEntity;
import com.santeut.guild.repository.CategoryRepository;
import com.santeut.guild.repository.GuildPostRepository;
import com.santeut.guild.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final GuildPostRepository guildPostRepository;
    private final CategoryRepository categoryRepository;

    // 길드 게시글 작성
    @Override
    public void createPost(PostCreateRequestDto reqDto, int userId) {
        int categoryId = categoryRepository.findByCategoryName(reqDto.getType()).orElseThrow(()->new CategoryNotFoundException("카테고리가 존재하지 않습니다.")).getCategoryId();
        guildPostRepository.save(GuildPostEntity
                        .builder()
                        .guildId(reqDto.getGuildId())
                        .categoryId(categoryId)
                        .userId(userId)
                        .guildPostTitle(reqDto.getTitle())
                        .guildPostContent(reqDto.getContent())
                        .build()
        );
    }
}
