package com.santeut.guild.service.implementation;

import com.santeut.guild.common.exception.CategoryNotFoundException;
import com.santeut.guild.common.exception.DataNotFoundException;
import com.santeut.guild.common.exception.FeignClientException;
import com.santeut.guild.dto.request.PostCreateRequestDto;
import com.santeut.guild.dto.response.PostListResponseDto;
import com.santeut.guild.dto.response.PostReadResponseDto;
import com.santeut.guild.entity.GuildPostEntity;
import com.santeut.guild.feign.AuthClient;
import com.santeut.guild.feign.CommonClient;
import com.santeut.guild.feign.dto.CommentFeignDto;
import com.santeut.guild.feign.dto.CommentListFeignDto;
import com.santeut.guild.repository.CategoryRepository;
import com.santeut.guild.repository.GuildPostRepository;
import com.santeut.guild.service.PostService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final GuildPostRepository guildPostRepository;
    private final CategoryRepository categoryRepository;
    private final CommonClient commonClient;
    private final AuthClient authClient;

    // 길드 게시글 작성
    @Override
    public void createPost(PostCreateRequestDto reqDto, int userId) {
//        int categoryId = categoryRepository.findByCategoryName(reqDto.getType()).orElseThrow(()->new CategoryNotFoundException("카테고리가 존재하지 않습니다.")).getCategoryId();
        GuildPostEntity newPost = guildPostRepository.save(GuildPostEntity .builder()
                        .categoryId(reqDto.getCategoryId())
                        .guildId(reqDto.getGuildId())
                        .userId(userId)
                        .guildPostTitle(reqDto.getGuildPostTitle())
                        .guildPostContent(reqDto.getGuildPostContent())
                        .build()
        );
        // 이미지 넣기
        try {
            for (String url : reqDto.getImages()) {
                Map<String, String> imageUrl = new HashMap<>();
                imageUrl.put("imageUrl", url);
                commonClient.saveImageUrl(newPost.getId(), 'G', imageUrl);
            }
        } catch(FeignClientException e) {
            log.error("이미지 저장 에러 : {}", e.getMessage());
        }
    }

    // 길드 게시글 목록 가져오기
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

    // 게시글 디테일 가져오기
    @Override
    public PostReadResponseDto readPost(int guildPostId, int userId) {

        // 길드포스트 찾는 query
//        GuildPostEntity entity = guildPostRepository.findById(guildPostId).orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));
        GuildPostEntity entity = guildPostRepository.findById(guildPostId).orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));

        // 카테고리 이름 찾는 query
        String categoryName = categoryRepository.findById(entity.getCategoryId()).orElseThrow(()->new DataNotFoundException("카테고리 이름을 찾을 수 없습니다.")).getCategoryName();

        // auth서버에 유저 닉네임 API 호출
        String userNickName = authClient.getUserInfo(entity.getUserId()).orElseThrow(()->new FeignClientException("auth 서버로부터 유저 정보를 받아오지 못했습니다.")).getData().getUserNickname();

        // 좋아요 개수 , 댓글 개수, 좋아요 눌렀는지 여부  API 호출
        int commentCnt = commonClient.getCommentCnt(entity.getId(), 'G').orElseThrow(()->new FeignClientException("common 서버에서 댓글 개수 받아오지 못했습니다.")).getData().get("commentCnt");
        int likeCnt = commonClient.getLikeCnt(entity.getId(), 'G').orElseThrow(()->new FeignClientException("common 서버에서 좋아요 개수 받아오지 못했습니다.")).getData().get("likeCnt");
        boolean likePushed = commonClient.likePushed(entity.getId(), 'G', userId).orElseThrow(()->new FeignClientException("common 서버에서 좋아요 개수 받아오지 못했습니다.")).getData().get("likePushed");

        // 게시글의 이미지 리스트 들고오기 API 호출
        List<String> images  = commonClient.getImages(entity.getId(),'G').orElseThrow(()->new FeignClientException("common 서버에서 이미지 경로 정보 불러오지 못했습니다.")).getData();

        // 댓글 목록 리스트 들고오기 API 호출
        List<CommentListFeignDto.Comment> commentList = commonClient.getCommentList(entity.getId(), 'G').orElseThrow(() -> new FeignClientException("common서버에서 댓글 목록을 가져오지 못했습니다.")).getData().getCommentList();

        //글 작성자인지 확인
        boolean isWriter = userId == entity.getUserId();

        // Dto에 적절한 값 넣어줘서 생성
        return PostReadResponseDto.builder()
                .guildPostId(entity.getId())
                .guildId(entity.getGuildId())
                .categoryId(entity.getCategoryId())
                .categoryName(categoryName)
                .guildPostTitle(entity.getGuildPostTitle())
                .guildPostContent(entity.getGuildPostContent())
                .userNickname(userNickName)
                .createdAt(entity.getCreatedAt())
                .likeCnt(likeCnt)
                .commentCnt(commentCnt)
                .hitCnt(entity.getHitCnt())
                .isWriter(isWriter)
                .commentList(commentList)
                .images(images)
                .isLike(likePushed)
                .build();
    }
}
