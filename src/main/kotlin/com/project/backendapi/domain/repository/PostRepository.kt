package com.project.backendapi.domain.repository

import com.project.backendapi.domain.dto.NeighborPostDTO
import com.project.backendapi.domain.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository : JpaRepository<Post, Long> {
    fun findByAuthorIdOrderByCreatedAtDesc(authorId: Long, pageable: Pageable): Page<Post>
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Post>

    @Query(
        "SELECT new com.project.backendapi.domain.dto.NeighborPostDTO(p.id, p.title) " +
        "FROM Post p WHERE p.id < :currentPostId ORDER BY p.id DESC LIMIT 1"
    )
    fun findPreviousPost(currentPostId: Long): NeighborPostDTO?

    @Query(
        "SELECT new com.project.backendapi.domain.dto.NeighborPostDTO(p.id, p.title) " +
        "FROM Post p WHERE p.id > :currentPostId ORDER BY p.id ASC LIMIT 1"
    )
    fun findNextPost(currentPostId: Long): NeighborPostDTO?
}
