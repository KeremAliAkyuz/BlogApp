package dev.aliak.blogapp.controllers;

import dev.aliak.blogapp.payload.PostDTO;
import dev.aliak.blogapp.payload.PostResponse;
import dev.aliak.blogapp.services.PostService;
import dev.aliak.blogapp.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    //pagination and sorting
    //default pageNo'yu 0 ve default pageSize'ı 10 olarak ayarladığımız için query yazmadığımız zaman 10 tane post olan bir page gönderir.
    @GetMapping("/api/v1/posts")
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
            ){
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO){
        return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }

    @GetMapping(value = "/api/posts/{id}")
    public ResponseEntity<PostDTO> getOnePostV1(@PathVariable Long id){
        return ResponseEntity.ok(postService.getOnePost(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/v1/posts/{id}")
    public ResponseEntity<String> deleteOnePost(@PathVariable Long id){
         postService.deleteOnePost(id);
         return new ResponseEntity<>("Post entity deleted successfully.",HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostDTO> updateOnePost(@Valid @RequestBody PostDTO postDTO , @PathVariable Long id){

        PostDTO postResponse = postService.updateOnePost(postDTO,id);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    //Build Get Posts by Category REST API
    @GetMapping("/api/v1/posts/category/{id}")
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@PathVariable("id") Long categoryId){
        List<PostDTO> postDTOS = postService.getPostsByCategory(categoryId);

        return ResponseEntity.ok(postDTOS);
    }
}
