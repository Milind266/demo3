package com.example.demo.controller;




import com.example.demo.Service.PostService;
import com.example.demo.model.*;
import com.example.demo.Service.UserService;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.*;

@RestController

    public class UserController {

        @Autowired
        private UserService userService;
    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

        @GetMapping("/{userId}")
        public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
            UserDTO userDTO = userService.getUserById(userId);
            return ResponseEntity.ok(userDTO);
        }

        @GetMapping("/user")
        public ResponseEntity<Object> getUserDetails(@RequestParam("userID") Long userID) {
            Optional<User> userOptional = userRepository.findById(userID);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Create the response object with the desired structure
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("name", user.getName());
                responseBody.put("userID", user.getId());
                responseBody.put("email", user.getEmail());

                return ResponseEntity.ok(responseBody);
            } else {
                // If user does not exist, return a 404 Not Found status with the relevant error message
                Map<String, String> response = new HashMap<>();
                response.put("Error","User does not exist");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        }

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        List<Map<String, Object>> userList = new ArrayList<>();

        for (UserDTO user : users) {
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("name", user.getName());
            userDetails.put("userID", user.getId());
            userDetails.put("email", user.getEmail());
            userList.add(userDetails);
        }

        return ResponseEntity.ok(userList);
    }


    @GetMapping("/")
    public ResponseEntity<?> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        // Sort the posts by date in reverse chronological order
        posts.sort(Comparator.comparing(Post::getDate).reversed());

        List<Map<String, Object>> postList = new ArrayList<>();

        for (Post post : posts) {
            Map<String, Object> postDetails = new HashMap<>();
            postDetails.put("postID", post.getId());
            postDetails.put("postBody", post.getPostBody());
           // postDetails.put("date", post.getDate());
            postDetails.put("date", post.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());


            List<Map<String, Object>> commentList = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                Map<String, Object> commentDetails = new HashMap<>();
                commentDetails.put("commentID", comment.getId());
                commentDetails.put("commentBody", comment.getCommentBody());

                Map<String, Object> commentCreator = new HashMap<>();
                commentCreator.put("userID", comment.getUser().getId());
                commentCreator.put("name", comment.getUser().getName());
                commentDetails.put("commentCreator", commentCreator);

                commentList.add(commentDetails);
            }

            postDetails.put("comments", commentList);
            postList.add(postDetails);
        }
//        Map<String, List<Map<String, Object>>> response = new HashMap<>();
//        response.put("posts", postList);

        return ResponseEntity.ok(postList);
    }

//        @GetMapping
//        public ResponseEntity<List<UserDTO>> getAllUsers() {
//            List<UserDTO> userDTOList = userService.getAllUsers();
//            return ResponseEntity.ok(userDTOList);
//        }

        @PostMapping
        public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
            UserDTO createdUserDTO = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
        }

        @PutMapping("/{userId}")
        public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
            UserDTO updatedUserDTO = userService.updateUser(userId, userDTO);
            return ResponseEntity.ok(updatedUserDTO);
        }

        @DeleteMapping("/{userId}")
        public ResponseEntity<ErrorDTO> deleteUser(@PathVariable Long userId) {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ErrorDTO("User deleted successfully"));
        }
    }


