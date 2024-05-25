package com.santeut.guild.service.implementation;

import com.santeut.guild.common.exception.AccessDeniedException;
import com.santeut.guild.common.exception.DataNotFoundException;
import com.santeut.guild.common.exception.FeignClientException;
import com.santeut.guild.dto.request.GuildPostUpdateRequestDto;
import com.santeut.guild.dto.request.PostCreateRequestDto;
import com.santeut.guild.dto.response.PostListResponseDto;
import com.santeut.guild.dto.response.PostReadResponseDto;
import com.santeut.guild.entity.GuildPostEntity;
import com.santeut.guild.feign.AuthClient;
import com.santeut.guild.feign.CommonClient;
import com.santeut.guild.feign.dto.CommentListFeignDto;
import com.santeut.guild.repository.CategoryRepository;
import com.santeut.guild.repository.GuildPostRepository;
import com.santeut.guild.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public void createPost(PostCreateRequestDto reqDto, List<MultipartFile> images , int userId) {

        // 게시글 DB에 저장
        GuildPostEntity newPost = guildPostRepository.save(GuildPostEntity .builder()
                        .categoryId(reqDto.getCategoryId())
                        .guildId(reqDto.getGuildId())
                        .userId(userId)
                        .guildPostTitle(reqDto.getGuildPostTitle())
                        .guildPostContent(reqDto.getGuildPostContent())
                        .build()
        );

        // 이미지 넣기 로직
        if(images !=null && !images.isEmpty()) {
            commonClient.saveImage(newPost.getId(), 'G', images);
        }
    }

    // 길드 게시글 목록 가져오기
    @Override
    public PostListResponseDto getPosts(int guildId, int categoryId, int lastSeenId) {
        List<PostListResponseDto> postList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        return new PostListResponseDto(guildPostRepository.findAllByGuildIdAndCategoryIdAndIsDeletedFalseAndIdLessThanOrderByIdDesc(guildId, categoryId, lastSeenId, pageable)
                .orElseThrow(() -> new DataNotFoundException("게시글이 없습니다."))
                .stream()
                .map(post -> {
                    // 닉네임 받아오기 위한 feign 함수
                    String userNickName = authClient.getUserInfo(post.getUserId()).orElseThrow(null).getData().getUserNickname();
                    // 좋아요 수 가져오기 위한 feign 함수
                    int likeCnt = commonClient.getLikeCnt(post.getId(), 'G').orElseThrow(null).getData().get("likeCnt");
                    // 댓글 수 가져오기 위한 feign 함수
                    int commentCnt = commonClient.getCommentCnt(post.getId(), 'G').orElseThrow(null).getData().get("commentCnt");

                    return PostListResponseDto.Post.builder()
                            .guildPostId(post.getId())
                            .categoryId(post.getCategoryId())
                            .guildId(post.getGuildId())
                            .userId(post.getUserId())
                            .userNickName(userNickName)
                            .guildPostTitle(post.getGuildPostTitle())
                            .guildPostContent(post.getGuildPostContent())
                            .createdAt(post.getCreatedAt())
                            .likeCnt(likeCnt)
                            .commentCnt(commentCnt)
                            .hitCnt(post.getHitCnt())
                            .build();

                }).collect(Collectors.toList()));
    }

    // 게시글 디테일 가져오기
    @Override
    public PostReadResponseDto readPost(int guildPostId, int userId) {

        // 길드포스트 찾는 query
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
                .userId(entity.getUserId())
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

    // 길드 게시글 삭제하기
    @Override
    public void deletePost(int guildPostId, int userId) {
        GuildPostEntity entity = guildPostRepository.findById(guildPostId).orElseThrow();
        // 게시글의 유저ID와 요청한 유저의 ID가 일치하는지 확인 하는 로직
        if(entity.getUserId() == userId) {
            guildPostRepository.deleteGuildPostDirectly(guildPostId, LocalDateTime.now());
        }else {
            throw new AccessDeniedException("삭제할 권한이 없습니다.");
        }
    }

    // 길드 게시글 수정하기
    @Override
    public void updatePost(GuildPostUpdateRequestDto guildPostUpdateRequestDto, int guildPostId, int userId) {

        // 권한 확인을 위해 글쓴이 ID를 받아옴
        GuildPostEntity entity = guildPostRepository.findById(guildPostId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // 작성자인지 검사함
        if (entity.getUserId() != userId) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        // 업데이트 쿼리 실행
        String guildPostTitle = guildPostUpdateRequestDto.getGuildPostTitle();
        String guildPostContent = guildPostUpdateRequestDto.getGuildPostContent();
        guildPostRepository.updateGuildPostDirectly(guildPostId, guildPostTitle, guildPostContent, LocalDateTime.now());
    }
}