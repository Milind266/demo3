package com.example.demo.controller;




import com.example.demo.model.*;
import com.example.demo.Service.CommentService;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
   // @RequestMapping("/comment")
    public class CommentController {

        @Autowired
        private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
//        @GetMapping("/{commentId}")
//        public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long commentId) {
//            CommentDTO commentDTO = commentService.getCommentById(commentId);
//            return ResponseEntity.ok(commentDTO);
//        }
//
//        @GetMapping
//        public ResponseEntity<List<CommentDTO>> getAllComments() {
//            List<CommentDTO> commentDTOList = commentService.getAllComments();
//            return ResponseEntity.ok(commentDTOList);
//        }

//        @PostMapping("/comment")
//        public ResponseEntity<String> createComment(@RequestBody CommentDTO commentDTO) {
//            Comment comment = commentService.createComment(commentDTO);
//
//            Optional<User> userOptional = userRepository.findById(commentDTO.getUserID());
//            Optional<Post> postOptional = postRepository.findById(commentDTO.getPostID());
//            //CommentDTO createdCommentDTO = commentService.createComment(commentDTO);
//            if (comment!= null) {
//                return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully");
//            }else if(userOptional.isPresent()){
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post does not exist");
//            }else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
//            }
//           // return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDTO);
//        }

    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO) {
        Optional<User> userOptional = userRepository.findById(commentDTO.getUserID());
        Optional<Post> postOptional = postRepository.findById(commentDTO.getPostID());
        Map<String, String> response = new HashMap<>();
        if (userOptional.isPresent() && postOptional.isPresent()) {
            Comment comment = commentService.createComment(commentDTO);
            if (comment != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create comment");
            }
        } else if (userOptional.isEmpty()) {
            response.put("Error","User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            response.put("Error","Post does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


//        @PutMapping("/{commentId}")
//        public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
//            CommentDTO updatedCommentDTO = commentService.updateComment(commentId, commentDTO);
//            return ResponseEntity.ok(updatedCommentDTO);
//        }
//
//        @DeleteMapping("/{commentId}")
//        public ResponseEntity<ErrorDTO> deleteComment(@PathVariable Long commentId) {
//            commentService.deleteComment(commentId);
//            return ResponseEntity.ok(new ErrorDTO("Comment deleted successfully"));
//        }

//    @GetMapping("/comment")
//    public ResponseEntity<Comment> getComment(@RequestParam("CommentID") Long commentId) {
//        Optional<Comment> commentOptional = commentRepository.findById(commentId);
//        if (commentOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//        return ResponseEntity.ok(commentOptional.get());
//    }

    @GetMapping("/comment")
    public ResponseEntity<Object> getComment(@RequestParam("commentID") Long commentID) {
        try {
            // Call your CommentService to retrieve the comment by ID
            Optional<Comment> commentOptional = commentRepository.findById(commentID);

            if (commentOptional.isPresent()) {

                Comment comment = commentOptional.get();
                // Create the response object with the desired structure
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("commentID", comment.getId());
                responseBody.put("commentBody", comment.getCommentBody());
                // Add comment creator details
                Map<String, Object> commentCreator = new HashMap<>();
                commentCreator.put("userID", comment.getUser().getId());
                commentCreator.put("name", comment.getUser().getName());
                responseBody.put("commentCreator", commentCreator);
                return ResponseEntity.ok(responseBody);
            } else {
                // If comment does not exist, return a 404 Not Found status with the relevant error message
                Map<String, String> response = new HashMap<>();
                response.put("Error","Comment does not exist");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (RuntimeException e) {
            // Handle any runtime exceptions and return a 500 Internal Server Error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }


//    @PatchMapping("/comment")
//    public ResponseEntity<String> editComment(@RequestParam("commentId") Long commentId, @RequestBody String request) {
//        Optional<Comment> commentOptional = commentRepository.findById(commentId);
//        if (commentOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment does not exist");
//        }
//        Comment comment = commentOptional.get();
//        comment.setCommentBody(request);
//        commentRepository.save(comment);
//        return ResponseEntity.ok("Comment edited successfully");
//    }

    @PatchMapping("/comment")
    public ResponseEntity<?> editComment(@RequestBody Map<String, String> requestBody) {
        try {
            String commentBody = requestBody.get("commentBody");
            Long commentID = Long.parseLong(requestBody.get("commentID").toString());
            // Check if the comment exists
            Optional<Comment> commentOptional = commentRepository.findById(commentID);
            if (commentOptional.isPresent()) {
                // If comment exists, update its body and save changes
                Comment commentToUpdate = commentOptional.get();
                commentToUpdate.setCommentBody(commentBody);
                commentRepository.save(commentToUpdate);
                return ResponseEntity.ok("Comment edited successfully");
            } else {
                // If comment does not exist, return a 404 Not Found status with the relevant error message
                Map<String, String> response = new HashMap<>();
                response.put("Error","Comment does not exist");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            // Handle any exceptions and return a 500 Internal Server Error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }


    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(@RequestParam Long commentID) {
        try {
            // Check if the comment exists
            Optional<Comment> commentOptional = commentRepository.findById(commentID);
            if (commentOptional.isPresent()) {
                // If comment exists, delete it from the database
                commentRepository.deleteById(commentID);
                return ResponseEntity.ok("Comment deleted");
            } else {
                // If comment does not exist, return a 404 Not Found status with the relevant error message
                Map<String, String> response = new HashMap<>();
                response.put("Error","Comment does not exist");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            // Handle any exceptions and return a 500 Internal Server Error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }


    }


