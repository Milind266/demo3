package com.example.demo.Service;

import com.example.demo.model.*;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    public CommentDTO getCommentById(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            return new CommentDTO(comment.getCommentBody(), comment.getPost().getId(), comment.getUser().getId());
        } else {
            throw new RuntimeException("Comment not found with id: " + commentId);
        }
    }

    public List<CommentDTO> getAllComments() {
        List<Comment> commentList = commentRepository.findAll();
        return commentList.stream()
                .map(comment -> new CommentDTO(comment.getCommentBody(), comment.getPost().getId(), comment.getUser().getId()))
                .collect(Collectors.toList());
    }

    /*****   public Comment createComment(CommentDTO commentDTO) {
      //  Comment comment = new Comment();
        Comment comment = convertToCommentEntity(commentDTO);
//        comment.setCommentBody(commentDTO.getCommentBody());
//        // Set other fields as needed
//        comment = commentRepository.save(comment);
//        return new CommentDTO(comment.getCommentBody(), comment.getPost().getId(), comment.getUser().getId());
        if(comment!=null){
            return commentRepository.save(comment);
        }else{
            return comment ;
        }
    }


    private Comment convertToCommentEntity(CommentDTO commentDTO) {
        // User user = UserRepository.findById(postDTO.getUserID()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Optional<User> userOptional = userRepository.findById(commentDTO.getUserID());
        Optional<Post> postOptional = postRepository.findById(commentDTO.getPostID());
        if (userOptional.isPresent() && postOptional.isPresent()) {
            User user = userOptional.get();
            Post post = postOptional.get();
            Comment comment  = new Comment();
            comment.setCommentBody(commentDTO.getCommentBody());
            comment.setUser(user);
            comment.setPost(post);
            comment.setDate(new Date());
            return comment;
            // Handle user retrieval
        } else {
            // User not found, handle accordingly
            return null;
        }

        //return post;
    }
****/
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment commentToUpdate = commentOptional.get();
            commentToUpdate.setCommentBody(commentDTO.getCommentBody());
            // Update other fields as needed
            commentRepository.save(commentToUpdate);
            return new CommentDTO(commentToUpdate.getCommentBody(), commentToUpdate.getPost().getId(), commentToUpdate.getUser().getId());
        } else {
            throw new RuntimeException("Comment not found with id: " + commentId);
        }
    }


   public Comment createComment(CommentDTO commentDTO) {
       Optional<User> userOptional = userRepository.findById(commentDTO.getUserID());
       Optional<Post> postOptional = postRepository.findById(commentDTO.getPostID());
       if (userOptional.isPresent() && postOptional.isPresent()) {
           User user = userOptional.get();
           Post post = postOptional.get();
           Comment comment = new Comment();
           comment.setCommentBody(commentDTO.getCommentBody());
           comment.setUser(user);
           comment.setPost(post);
           comment.setDate(new Date());
           return commentRepository.save(comment);
       } else {
           throw new RuntimeException("User or post not found. Comment cannot be created.");
       }
   }
    public void deleteComment(Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new RuntimeException("Comment not found with id: " + commentId);
        }
    }
}
