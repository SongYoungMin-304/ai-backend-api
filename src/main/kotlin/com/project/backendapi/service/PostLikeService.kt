package com.project.backendapi.service

import com.project.backendapi.domain.dto.PostLikeResponse
import com.project.backendapi.domain.entity.PostLike
import com.project.backendapi.domain.repository.PostLikeRepository
import com.project.backendapi.domain.repository.PostRepository
import com.project.backendapi.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostLikeService(
    private val postLikeRepository: PostLikeRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {

    fun addLike(postId: Long, userId: Long): PostLikeResponse {
        val post = postRepository.findById(postId)
            .orElseThrow { IllegalArgumentException("게시글을 찾을 수 없습니다") }

        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw IllegalArgumentException("이미 좋아요한 게시글입니다")
        }

        val like = PostLike(
            post = post,
            user = user
        )
        postLikeRepository.save(like)

        return PostLikeResponse(
            postId = postId,
            likeCount = postLikeRepository.countByPostId(postId),
            liked = true
        )
    }

    fun removeLike(postId: Long, userId: Long): PostLikeResponse {
        postRepository.findById(postId)
            .orElseThrow { IllegalArgumentException("게시글을 찾을 수 없습니다") }

        val like = postLikeRepository.findByPostIdAndUserId(postId, userId)
            ?: throw IllegalArgumentException("좋아요 기록을 찾을 수 없습니다")

        postLikeRepository.delete(like)

        return PostLikeResponse(
            postId = postId,
            likeCount = postLikeRepository.countByPostId(postId),
            liked = false
        )
    }

    @Transactional(readOnly = true)
    fun isLiked(postId: Long, userId: Long): Boolean {
        return postLikeRepository.existsByPostIdAndUserId(postId, userId)
    }

    @Transactional(readOnly = true)
    fun getLikeCount(postId: Long): Long {
        return postLikeRepository.countByPostId(postId)
    }
}
