package com.santeut.guild.service.implementation;

import com.santeut.guild.common.exception.CategoryNotFoundException;
import com.santeut.guild.common.exception.DataNotFoundException;
import com.santeut.guild.dto.request.PostCreateRequestDto;
import com.santeut.guild.dto.response.PostListResponseDto;
import com.santeut.guild.entity.GuildPostEntity;
import com.santeut.guild.repository.CategoryRepository;
import com.santeut.guild.repository.GuildPostRepository;
import com.santeut.guild.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<PostListResponseDto> getPosts(int guildId, int categoryId, int lastSeenId) {
        List<PostListResponseDto> postList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        for (GuildPostEntity entity : guildPostRepository.findAllByGuildIdAndCategoryIdAndIdLessThanOrderByIdDesc(guildId, categoryId, lastSeenId, pageable).orElseThrow(() -> new DataNotFoundException("게시글이 없습니다."))) {
            postList.add(PostListResponseDto.builder()
                    .guildPostId(entity.getId())
                    .categoryId(entity.getCategoryId())
                    .guildId(entity.getGuildId())
                    .userId(entity.getUserId())
                    .guildPostTitle(entity.getGuildPostTitle())
                    .guildPostContent(entity.getGuildPostContent())
                    .createdAt(entity.getCreatedAt())
                    .likeCnt(0) // Fix : 하드코딩 된 값 common 서버와 통신해서 넣기
                    .commentCnt(0) // Fix : 하드코딩 된 값 common 서버와 통신해서 넣식
                    .hitCnt(entity.getHitCnt())
                    .build()
            );
        }

        return postList;
    }
}
