package dev.aliak.blogapp.services;

import dev.aliak.blogapp.entity.Category;
import dev.aliak.blogapp.payload.PostDTO;
import dev.aliak.blogapp.payload.PostResponse;
import dev.aliak.blogapp.entity.Post;
import dev.aliak.blogapp.exception.ResourceNotFoundException;
import dev.aliak.blogapp.repositories.CategoryRepository;
import dev.aliak.blogapp.repositories.PostRepository;
import dev.aliak.blogapp.services.serviceInterfaces.PostServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService implements PostServiceInterface {

    private final ModelMapper mapper;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy,String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //create Pageable instance
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);
        //get content for page object
        List<Post> listOfPosts = posts.getContent();
        List<PostDTO> content = listOfPosts.stream().map(post->mapToDTO(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse(content,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isLast()
        );

        return postResponse;


    }
    @Override
    public PostDTO getOnePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        return mapToDTO(post);
    }
    @Override
    public void deleteOnePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDTO> getPostsByCategory(Long categoryId) {

        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","id",categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);

        return posts.stream().map((post) -> mapToDTO(post)).collect(Collectors.toList());
    }

    @Override
    public PostDTO updateOnePost(PostDTO postDTO, Long id) {

        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));

        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category","Id",postDTO.getCategoryId()));

        post.setId(postDTO.getId());
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());
        post.setCategory(category);

        Post response = postRepository.save(post);

        return mapToDTO(response);

    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {

        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category","Id",postDTO.getCategoryId()));

        //Convert DTO to entity
        Post post = mapToEntity(postDTO);
        //set category
        post.setCategory(category);
        //save db
        Post newPost = postRepository.save(post);
        //Convert entity to DTO
        PostDTO postResponse = mapToDTO(newPost);

        return postResponse;
    }
    //convert Entity to DTO
    private PostDTO mapToDTO(Post post){
        PostDTO postDTO = mapper.map(post,PostDTO.class);

//        PostDTO postDTO = new PostDTO();
//        postDTO.setId(post.getId());
//        postDTO.setDescription(post.getDescription());
//        postDTO.setContent(post.getContent());
//        postDTO.setTitle(post.getTitle());
        return postDTO;
    }
    //convert DTO to entity
    private Post mapToEntity(PostDTO postDTO){
        Post post = mapper.map(postDTO, Post.class);

//        Post post = new Post();
//        post.setId(postDTO.getId());
//        post.setContent(postDTO.getContent());
//        post.setDescription(postDTO.getDescription());
//        post.setTitle(postDTO.getTitle());
        return post;
    }
}
