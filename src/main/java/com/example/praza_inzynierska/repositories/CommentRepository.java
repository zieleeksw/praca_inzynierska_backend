package com.example.praza_inzynierska.repositories;

import com.example.praza_inzynierska.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
