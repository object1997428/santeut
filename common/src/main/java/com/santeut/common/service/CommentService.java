package com.santeut.common.service;

import com.santeut.common.common.exception.RepositorySaveException;
import com.santeut.common.dto.request.UserInfoFeignRequestDto;
import com.santeut.common.entity.CommentEntity;
import com.santeut.common.feign.UserInfoClient;
import com.santeut.common.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserInfoClient userInfoClient;

    public void createComment(int postId, char postType, String commentContent) {
        // 유저 ID를 가져오는 feign 호출
        UserInfoFeignRequestDto userInfo = userInfoClient.getUserInfo().orElseThrow(() -> new RuntimeException("asdf"));

        try {
            commentRepository.save(CommentEntity.builder()
                    .commentReferenceType(postType)
                    .commentReferenceId(postId)
                    .id(userInfo.getUserId())
                    .commentContent(commentContent)
                    .build()
            );
        } catch (Exception e) {
            throw new RepositorySaveException("댓글 등록 에러 발생");
        }
    }

}
