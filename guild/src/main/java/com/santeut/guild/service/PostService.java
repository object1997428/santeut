package com.santeut.guild.service;

import com.santeut.guild.dto.request.PostCreateRequestDto;

public interface PostService {

    void createPost(PostCreateRequestDto postCreateRequestDto, int userId);
}
