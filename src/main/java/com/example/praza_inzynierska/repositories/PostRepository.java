package com.example.praza_inzynierska.repositories;

import com.example.praza_inzynierska.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
