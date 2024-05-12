package com.santeut.guild.service;

import com.santeut.guild.dto.request.PostCreateRequestDto;
import com.santeut.guild.dto.request.GuildPostUpdateRequestDto;
import com.santeut.guild.dto.response.PostListResponseDto;
import com.santeut.guild.dto.response.PostReadResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    void createPost(PostCreateRequestDto postCreateRequestDto, List<MultipartFile> images, int userId);

    PostListResponseDto getPosts(int guildId, int categoryId, int lastSeenId);

    PostReadResponseDto readPost(int guildPostId, int userId);

    void deletePost(int guildPostId, int userId);

    void updatePost(GuildPostUpdateRequestDto guildPostUpdateRequestDto, int guildPostId, int userId);

}
