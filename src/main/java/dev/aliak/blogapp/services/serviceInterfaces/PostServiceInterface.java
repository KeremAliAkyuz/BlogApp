package dev.aliak.blogapp.services.serviceInterfaces;

import dev.aliak.blogapp.payload.PostDTO;
import dev.aliak.blogapp.payload.PostResponse;

import java.util.List;

public interface PostServiceInterface {
    PostDTO createPost(PostDTO postDTO);
    PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir);
    PostDTO getOnePost(Long id);
    PostDTO updateOnePost(PostDTO postDTO,Long id);
    void deleteOnePost(Long id);
    List<PostDTO> getPostsByCategory(Long categoryId);
}
