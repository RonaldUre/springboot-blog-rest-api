package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
@Tag(
        name = "CRUD REST APIs for Post Resource"
)
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //create blog post
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/v1/posts")
    @SecurityRequirement(name = "Bear Authentication")
    @Operation(summary = "Create post Rest Api",
    description = "Create Post Rest Api used to saved post into Database")
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED"
    )
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(this.postService.createPost(postDto), HttpStatus.CREATED);
    }

    //get all post rest api
    @Operation(summary =  "GET all posts Rest Api",
            description = "GET ALL POSTS Rest Api is used to fetch all the post from Database")
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @GetMapping("/api/v1/posts")
    public PostResponse getAllPost(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return postService.getAllPost(pageNo, pageSize, sortBy, sortDir);
    }
    //get post by id
    @Operation(summary =  "GET post by id Rest Api",
            description = "GET post by id  Rest Api is used to get single post from Database")
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @GetMapping(value = "/api/v1/posts/{id}")
    public ResponseEntity<PostDto> getPostByIdV1(@PathVariable(name = "id") long id){

        return ResponseEntity.ok(postService.getPostById(id));

    }

    // update post by id restapi
    @Operation(summary =  "Update post Rest Api",
            description = "Update post Rest Api is used to updated a particular post in the Database")
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/v1/posts/{id}")
    @SecurityRequirement(name = "Bear Authentication")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable(name = "id") long id){
        PostDto postResponse = postService.updatePost(postDto, id);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    //delete post rest api
    @Operation(summary =  "Delete post Rest Api",
            description = "Delete post Rest Api is used to delete a particular post in the Database")
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/v1/posts/{id}")
    @SecurityRequirement(name = "Bear Authentication")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id")  long id){

        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted succesfully", HttpStatus.OK);
    }

    //Build get post by Category restApi
    //http://localhost:8080/api/posts/category/2
    @GetMapping("/api/v1/posts/category/{id}")
    public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable("id") Long categoryId){
        List<PostDto> posts = postService.getPostByCategoryId(categoryId);
        return ResponseEntity.ok(posts);
    }

}
