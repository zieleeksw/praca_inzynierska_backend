package com.example.praza_inzynierska.forum.repositories;

import com.example.praza_inzynierska.forum.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
