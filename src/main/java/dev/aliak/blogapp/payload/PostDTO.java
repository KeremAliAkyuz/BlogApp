package dev.aliak.blogapp.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDTO {
    private Long id;
    //title should not be null or empty
    //title should have at least 2 characters
    @NotEmpty
    @Size(min = 2,message = "Post title should have at least 2 characters")
    private String title;
    //description should not be null or empty
    //description should have at least 7 characters
    @NotEmpty
    @Size(min = 10,message = "Post description should have at least 8 characters")
    private String description;
    @NotEmpty
    private String content;
    private Set<CommentDTO> comments;

    private Long categoryId;
}
