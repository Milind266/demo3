package com.example.demo.Service;

import com.example.demo.model.Post;
import com.example.demo.model.PostDTO;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    public Post getPostById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        return postOptional.orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(PostDTO postDTO) {
        // Convert PostDTO to Post entity
        Post post = convertToPostEntity(postDTO);

        // Save the post using postRepository
        if(post!=null) {
            return postRepository.save(post);
        }else{
            return post ;
        }
    }

    private Post convertToPostEntity(PostDTO postDTO) {
       // User user = UserRepository.findById(postDTO.getUserID()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Optional<User> userOptional = userRepository.findById(postDTO.getUserID());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Post post = new Post();
            post.setPostBody(postDTO.getPostBody());
            post.setUser(user);
            post.setDate(new Date());
            return post;
            // Handle user retrieval
        } else {
            // User not found, handle accordingly
            return null;
        }

        //return post;
    }


    public Post editPost(Long postId, String str ) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post =postOptional.get();
            post.setId(postId);
            post.setPostBody(str);
            return postRepository.save(post);
        } else {
            throw new RuntimeException("Post not found with id: " + postId);
        }
    }

    public void deletePost(Long postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
        } else {
            throw new RuntimeException("Post not found with id: " + postId);
        }
    }
}
