

package com.example.demo.controller;


import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.PostDTO;
import com.example.demo.model.ErrorDTO;
import com.example.demo.Service.PostService;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.*;

@RestController
//@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

//    @GetMapping("/{postId}")
//    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
//        Post post = postService.getPostById(postId);
//        return ResponseEntity.ok(post);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Post>> getAllPosts() {
//        List<Post> postList = postService.getAllPosts();
//        return ResponseEntity.ok(postList);
//    }
//
    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO) {
        Post post = postService.createPost(postDTO);
        if (post != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully");
        }else{
            Map<String, String> response = new HashMap<>();
            response.put("Error","User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


//    @PutMapping("/{postId}")
//    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody String str ) {
//        Post post  = postService.editPost(postId,str);
//        return ResponseEntity.ok(post);
//    }
//
//    @DeleteMapping("/{postId}")
//    public ResponseEntity<ErrorDTO> deletePost(@PathVariable Long postId) {
//        postService.deletePost(postId);
//        return ResponseEntity.ok(new ErrorDTO("Post deleted successfully"));
//    }


//    @GetMapping("/post")
//    public ResponseEntity<Post> getPost(@RequestParam("postID") Long postId) {
//        Optional<Post> postOptional = postRepository.findById(postId);
//        if (postOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//        return ResponseEntity.ok(postOptional.get());
//    }

    @GetMapping("/post")
    public ResponseEntity<Object> getPost(@RequestParam("postID") Long postID) {
        try {
            // Call your PostService to retrieve the post by ID
            Optional<Post> postOptional = postRepository.findById(postID);

            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                // Create the response object with the desired structure
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("postID", post.getId());
                responseBody.put("postBody", post.getPostBody());
             //   responseBody.put("date", post.getDate());
                responseBody.put("date", post.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());


                // Add comments details
                List<Map<String, Object>> commentsList = new ArrayList<>();
                for (Comment comment : post.getComments()) {
                    Map<String, Object> commentDetails = new HashMap<>();
                    commentDetails.put("commentID", comment.getId());
                    commentDetails.put("commentBody", comment.getCommentBody());

                    // Add comment creator details
                    Map<String, Object> commentCreator = new HashMap<>();
                    commentCreator.put("userID", comment.getUser().getId());
                    commentCreator.put("name", comment.getUser().getName());
                    commentDetails.put("commentCreator", commentCreator);

                    commentsList.add(commentDetails);
                }
                responseBody.put("comments", commentsList);

                return ResponseEntity.ok(responseBody);
            } else {
                // If post does not exist, return a 404 Not Found status with the relevant error message
                Map<String, String> response = new HashMap<>();
                response.put("Error","Post does not exist");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (RuntimeException e) {
            // Handle any runtime exceptions and return a 500 Internal Server Error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }


//    @PatchMapping("/post")
//    public ResponseEntity<String> editPost(@RequestParam("postID")Long postId, @RequestBody String  request) {
//        Optional<Post> postOptional = postRepository.findById(postId);
//        if (postOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post does not exist");
//        }
//        Post post = postOptional.get();
//        post.setPostBody(request);
//        postRepository.save(post);
//        return ResponseEntity.ok("Post edited successfully");
//    }

    @PatchMapping("/post")
    public ResponseEntity<?> editPost(@RequestBody Map<String, String> request) {
        Long postId = Long.parseLong(request.get("postID").toString());
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("Error","Post does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Post post = postOptional.get();
        String updatedPostBody = request.get("postBody");
        post.setPostBody(updatedPostBody);
        postRepository.save(post);
        return ResponseEntity.ok("Post edited successfully");
    }


    @DeleteMapping("/post")
    public ResponseEntity<?> deletePost(@RequestParam("postID") Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("Error","Post does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        postRepository.delete(postOptional.get());
        return ResponseEntity.ok("Post deleted");
    }
}
