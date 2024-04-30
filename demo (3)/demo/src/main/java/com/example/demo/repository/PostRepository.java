package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.demo.model.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByDateDesc();
}
