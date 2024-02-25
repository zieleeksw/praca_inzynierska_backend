package com.example.praza_inzynierska.forum.repositories;

import com.example.praza_inzynierska.forum.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
