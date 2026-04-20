package com.project.backendapi.service

import com.project.backendapi.domain.dto.CommentLikeResponse
import com.project.backendapi.domain.entity.CommentLike
import com.project.backendapi.domain.repository.CommentLikeRepository
import com.project.backendapi.domain.repository.CommentRepository
import com.project.backendapi.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentLikeService(
    private val commentLikeRepository: CommentLikeRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {

    fun addLike(commentId: Long, userId: Long): CommentLikeResponse {
        val comment = commentRepository.findById(commentId)
            .orElseThrow { IllegalArgumentException("댓글을 찾을 수 없습니다") }

        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        if (commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
            throw IllegalArgumentException("이미 좋아요한 댓글입니다")
        }

        val like = CommentLike(
            comment = comment,
            user = user
        )
        commentLikeRepository.save(like)

        return CommentLikeResponse(
            commentId = commentId,
            likeCount = commentLikeRepository.countByCommentId(commentId),
            liked = true
        )
    }

    fun removeLike(commentId: Long, userId: Long): CommentLikeResponse {
        commentRepository.findById(commentId)
            .orElseThrow { IllegalArgumentException("댓글을 찾을 수 없습니다") }

        val like = commentLikeRepository.findByCommentIdAndUserId(commentId, userId)
            ?: throw IllegalArgumentException("좋아요 기록을 찾을 수 없습니다")

        commentLikeRepository.delete(like)

        return CommentLikeResponse(
            commentId = commentId,
            likeCount = commentLikeRepository.countByCommentId(commentId),
            liked = false
        )
    }

    @Transactional(readOnly = true)
    fun isLiked(commentId: Long, userId: Long): Boolean {
        return commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)
    }

    @Transactional(readOnly = true)
    fun getLikeCount(commentId: Long): Long {
        return commentLikeRepository.countByCommentId(commentId)
    }
}
