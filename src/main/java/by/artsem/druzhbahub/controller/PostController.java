package by.artsem.druzhbahub.controller;

import by.artsem.druzhbahub.model.Post;
import by.artsem.druzhbahub.model.Profile;
import by.artsem.druzhbahub.model.dto.image.ImageResponseDto;
import by.artsem.druzhbahub.model.dto.post.*;
import by.artsem.druzhbahub.model.dto.post.mapper.PostMapper;
import by.artsem.druzhbahub.model.dto.profile.ShortProfileResponseDto;
import by.artsem.druzhbahub.model.dto.profile.mapper.ProfileMapper;
import by.artsem.druzhbahub.service.ImageService;
import by.artsem.druzhbahub.service.PostService;
import by.artsem.druzhbahub.service.impl.RecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin
public class PostController {

    private final PostService postService;

    private final ImageService imageService;

    private final ModelMapper modelMapper;

    private final RecommendationService recommendationService;

    private final String avatarUrl = "https://media.istockphoto.com/id/1337144146/vector/default-avatar-profile-icon-vector.jpg?s=612x612&w=0&k=20&c=BIbFwuv7FxTWvh5S3vB6bkT0Qv8Vn8N5Ffseq84ClGI=";

    @PostMapping
    public ResponseEntity<HttpStatus> createPost(@RequestBody @Valid PostCreateRequestDTO postDto) {
        postService.create(PostMapper.mapToEntity(postDto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long id,
            @RequestBody @Valid PostUpdateRequestDTO postDto
    ) {
        Post updatedPost = postService.update(id, modelMapper.map(postDto, Post.class));
        return new ResponseEntity<>(
                PostMapper.mapToDto(updatedPost),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/summary")
    public ResponseEntity<PostResponseDto> updateSummary(
            @PathVariable Long id,
            @RequestBody @Valid PostUpdateSummaryRequestDTO dto
    ) {
        Post updatedPost = postService.updateSummary(id, dto.getSummary());
        return new ResponseEntity<>(
                PostMapper.mapToDto(updatedPost),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/event")
    public ResponseEntity<PostResponseDto> updateEvent(
            @PathVariable Long id,
            @RequestBody @Valid PostUpdateEventRequestDTO dto
    ) {
        Post updatedPost = postService.updateEvent(id, dto.getEventId());
        return new ResponseEntity<>(
                PostMapper.mapToDto(updatedPost),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        Post post = postService.getById(id);
        return new ResponseEntity<>(
                PostMapper.mapToDto(post),
                HttpStatus.OK
        );
    }

    @GetMapping("/recommended/{profileId}")
    public ResponseEntity<List<FullPostResponseDto>> getRecommendedPostsByProfileId(@PathVariable Long profileId) {
        List<Post> posts = recommendationService.getRecommendedPostsByProfileId(profileId);
        return new ResponseEntity<>(
                posts.stream().map(post -> {
                    Profile profile = post.getProfile();
                    List<ImageResponseDto> images = imageService.getProfileImageUrls(profile.getId());
                    ShortProfileResponseDto profileDto;
                    if (!images.isEmpty())
                        profileDto = ProfileMapper.mapToShortDto(profile, images.get(images.size() - 1).getUrl());
                    else
                        profileDto = ProfileMapper.mapToShortDto(profile, avatarUrl);
                    return PostMapper.mapToFullDto(post, profileDto);
                }).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<Post> posts = postService.getAll();
        return new ResponseEntity<>(
                posts.stream().map(PostMapper::mapToDto).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }
}