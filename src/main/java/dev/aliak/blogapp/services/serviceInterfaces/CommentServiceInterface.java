package dev.aliak.blogapp.services.serviceInterfaces;

import dev.aliak.blogapp.payload.CommentDTO;

import java.util.List;

public interface CommentServiceInterface {
    CommentDTO createComment(Long postId, CommentDTO commentDTO);

    List<CommentDTO> getCommentsByPostId(long postId);
    CommentDTO getOneCommentByIDs(Long postId, Long commentId);
    CommentDTO updateOnePost(Long postId, Long commentId, CommentDTO commentDTO);

    void deletComment(Long postId,Long commentId);
}
