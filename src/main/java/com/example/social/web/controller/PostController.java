package com.example.social.web.controller;

import com.example.social.domain.entity.Comment;
import com.example.social.domain.entity.Post;
import com.example.social.domain.entity.User;
import com.example.social.domain.repository.PostRepository;
import com.example.social.service.PostService;
import com.example.social.service.UserService;
import com.example.social.web.dto.PostDTOs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @PostMapping(value = "/posts", consumes = {"multipart/form-data"})
    public ResponseEntity<PostDTOs.PostResponse> create(@RequestPart("image") MultipartFile image, @RequestPart(value="description", required=false) String description){
        Post post = postService.createPost(image, description);

        User author = userService.getUserVisibleById(post.getAuthorId());
        return  ResponseEntity.created(URI.create("/api/posts/" + post.getId())).body(toResponse(post, author, List.of()));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDTOs.PostResponse> get(@PathVariable Long id){
        Post post = postService.getPostOr404(id);
        var comments = postService.listComments(post.getId());
        User author = userService.getUserVisibleById(post.getAuthorId());

        return ResponseEntity.ok(toResponse(post, author, comments));
    }

    @PutMapping(value = "/posts/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<PostDTOs.PostResponse> update(@PathVariable Long id,  @RequestPart(value = "image", required = false) MultipartFile image, @RequestPart(value = "description", required = false) String description){
        Post post = postService.updatePost(id, image, description);
        var comments = postService.listComments(post.getId());
        User author = userService.getUserVisibleById(post.getAuthorId());

        return ResponseEntity.ok(toResponse(post, author, comments));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{id}/view")
    public ResponseEntity<Void> view(@PathVariable Long id){
        postService.incrementView(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTOs.PostResponse>> list(){
        var all = postService.listActive();
        List<Long> authorIds = all.stream().map(Post::getAuthorId).distinct().toList();
        Map<Long, User> authors = userService.getUsersByIds(authorIds).stream().collect(Collectors.toMap(User::getId, Function.identity()));

        return ResponseEntity.ok(all.stream().map(post -> toResponse(post, authors.get(post.getAuthorId()), List.of())).toList());
    }

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<PostDTOs.CommentResponse> addComment(@PathVariable Long id, @RequestBody @Valid PostDTOs.CommentCreateRequest commentCreateRequest){
        Comment comment = postService.addComment(id, commentCreateRequest.content());
        return ResponseEntity.ok(new PostDTOs.CommentResponse(comment.getId(), comment.getAuthorId(), comment.getContent(), comment.getCreatedAt()));
    }

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<List<PostDTOs.CommentResponse>> listComments(@PathVariable Long id){
        var list = postService.listComments(id).stream()
                .map(comment -> new PostDTOs.CommentResponse(comment.getId(), comment.getAuthorId(), comment.getContent(), comment.getCreatedAt())).toList();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId){
        postService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{id}/likes")
    public ResponseEntity<Void> like(@PathVariable Long id){
        postService.likePost(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{id}/likes")
    public ResponseEntity<Void> unlike(@PathVariable Long id){
        postService.unlikePost(id);
        return ResponseEntity.noContent().build();
    }

    private PostDTOs.PostResponse toResponse(Post p, User author, List<Comment> commentList){
        var comments = commentList.stream()
                .map(c -> new PostDTOs.CommentResponse(c.getId(), c.getAuthorId(), c.getContent(), c.getCreatedAt()))
                .toList();

        var authorDto = (author == null)
                ? new PostDTOs.PostResponse.Author(p.getAuthorId(), "Unknown User")
                : new PostDTOs.PostResponse.Author(author.getId(), author.getUsername());

        return new PostDTOs.PostResponse(p.getId(), authorDto, p.getDescription(), p.getImagePath(),
                p.getLikeCount(), p.getViewCount(), p.getCreatedAt(), p.getUpdatedAt(), comments);
    }
}
