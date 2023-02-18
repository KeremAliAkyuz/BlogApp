package dev.aliak.blogapp.services;

import dev.aliak.blogapp.payload.CommentDTO;
import dev.aliak.blogapp.entity.Comment;
import dev.aliak.blogapp.entity.Post;
import dev.aliak.blogapp.exception.BlogAPIException;
import dev.aliak.blogapp.exception.ResourceNotFoundException;
import dev.aliak.blogapp.repositories.CommentRepository;
import dev.aliak.blogapp.repositories.PostRepository;
import dev.aliak.blogapp.services.serviceInterfaces.CommentServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService implements CommentServiceInterface {
    private final ModelMapper mapper;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {

        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        Comment comment = mapToEntity(commentDTO);
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        return  mapToDTO(newComment);

    }

    @Override
    public List<CommentDTO> getCommentsByPostId(long postId) {
        //retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);
        //convert list of comment entities to list of comment dto's
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getOneCommentByIDs(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id",commentId));
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        return mapToDTO(comment);
    }
    @Override
    public CommentDTO updateOnePost(Long postId, Long commentId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id",commentId));
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        comment.setName(commentDTO.getName());
        comment.setBody(commentDTO.getBody());
        comment.setEmail(commentDTO.getEmail());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDTO(updatedComment);

    }

    @Override
    public void deletComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","id",commentId));
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        commentRepository.delete(comment);
    }


    //convert Entity to DTO
    private CommentDTO mapToDTO(Comment comment){
        CommentDTO commentDTO = mapper.map(comment,CommentDTO.class);
//        CommentDTO commentDTO = new CommentDTO();
//        commentDTO.setId(comment.getId());
//        commentDTO.setBody(comment.getBody());
//        commentDTO.setEmail(comment.getEmail());
//        commentDTO.setName(comment.getName());
        return commentDTO;
    }
    //convert DTO to entity
    private Comment mapToEntity(CommentDTO commentDTO){
        Comment comment = mapper.map(commentDTO,Comment.class);
//        Comment comment = new Comment();
//        comment.setId(commentDTO.getId());
//        comment.setBody(commentDTO.getBody());
//        comment.setEmail(commentDTO.getEmail());
//        comment.setName(commentDTO.getName());
        return comment;
    }


}
