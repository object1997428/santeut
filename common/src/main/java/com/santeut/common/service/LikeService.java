package com.santeut.common.service;

import com.santeut.common.common.exception.AccessDeniedException;
import com.santeut.common.common.exception.DataNotFoundException;
import com.santeut.common.common.exception.FeignClientException;
import com.santeut.common.common.exception.RepositorySaveException;
import com.santeut.common.dto.response.CommentListResponseDto;
import com.santeut.common.entity.CommentEntity;
import com.santeut.common.feign.service.AuthServerService;
import com.santeut.common.repository.CommentRepository;
import com.santeut.common.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    //    private final UserInfoClient userInfoClient;
    private final AuthServerService authServerService;


    public int getLikeCnt(Integer postId, Character postType) {
        return likeRepository.countByLikeReferenceIdAndLikeReferenceType(postId, postType);
    }
}