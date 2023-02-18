package dev.aliak.blogapp.controllers;

import dev.aliak.blogapp.payload.CommentDTO;
import dev.aliak.blogapp.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{post-id}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(name = "post-id") Long postId,@Valid @RequestBody CommentDTO commentDTO){

        return new ResponseEntity<>(commentService.createComment(postId,commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{post-id}/comments")
    public List<CommentDTO> getCommentsByPostId(@PathVariable(name = "post-id") Long postId){
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/posts/{post-id}/comments/{comment-id}")
    public ResponseEntity<CommentDTO> getOneCommentByIDs(
            @PathVariable(name = "post-id") Long postId,@PathVariable(name = "comment-id")Long commentId){

            return ResponseEntity.ok(commentService.getOneCommentByIDs(postId,commentId));
    }

    @PutMapping("/posts/{post-id}/comments/{comment-id}")
    public ResponseEntity<CommentDTO> updateOnePost(
            @PathVariable(name = "post-id")Long postId,
            @PathVariable(name = "comment-id")Long commentId,
            @Valid @RequestBody CommentDTO commentDTO){

        CommentDTO commentResponse=commentService.updateOnePost(postId,commentId,commentDTO);
        return new ResponseEntity<>(commentResponse,HttpStatus.OK);
    }
    @DeleteMapping("/posts/{post-id}/comments/{comment-id}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "post-id") Long postId,
                                                @PathVariable(name = "comment-id") Long commentId){
        commentService.deletComment(postId,commentId);
        return new ResponseEntity<>("Comment deleted succesfully",HttpStatus.OK);
    }

}
