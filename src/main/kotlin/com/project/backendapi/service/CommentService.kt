package com.project.backendapi.service

import com.project.backendapi.domain.dto.CommentResponse
import com.project.backendapi.domain.dto.CreateCommentRequest
import com.project.backendapi.domain.entity.Comment
import com.project.backendapi.domain.repository.CommentRepository
import com.project.backendapi.domain.repository.CommentLikeRepository
import com.project.backendapi.domain.repository.PostRepository
import com.project.backendapi.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentService(
    private val commentRepository: CommentRepository,
    private val commentLikeRepository: CommentLikeRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    fun getCommentsByPostId(postId: Long, userId: Long? = null): List<CommentResponse> {
        println("📌 getCommentsByPostId: postId=$postId, userId=$userId")
        postRepository.findById(postId)
            .orElseThrow { IllegalArgumentException("게시글을 찾을 수 없습니다") }

        val comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
            .filter { it.parentComment == null }

        return comments.map { comment ->
            val response = CommentResponse.from(comment)
            val likeCount = commentLikeRepository.countByCommentId(comment.id!!)
            val liked = userId?.let { commentLikeRepository.existsByCommentIdAndUserId(comment.id!!, it) } ?: false
            println("📌 Comment ${comment.id}: likeCount=$likeCount, liked=$liked, userId=$userId")

            response.copy(
                likeCount = likeCount,
                liked = liked,
                replies = comment.replies.map { reply ->
                    val replyResponse = CommentResponse.from(reply)
                    val replyLikeCount = commentLikeRepository.countByCommentId(reply.id!!)
                    val replyLiked = userId?.let { commentLikeRepository.existsByCommentIdAndUserId(reply.id!!, it) } ?: false
                    replyResponse.copy(likeCount = replyLikeCount, liked = replyLiked)
                }
            )
        }
    }

    fun createComment(postId: Long, authorId: Long, request: CreateCommentRequest): CommentResponse {
        val post = postRepository.findById(postId)
            .orElseThrow { IllegalArgumentException("게시글을 찾을 수 없습니다") }

        val author = userRepository.findById(authorId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        val comment = Comment(
            content = request.content,
            post = post,
            author = author,
            parentComment = null
        )

        val saved = commentRepository.save(comment)
        return CommentResponse.from(saved)
    }

    fun createReply(parentCommentId: Long, authorId: Long, request: CreateCommentRequest): CommentResponse {
        val parentComment = commentRepository.findById(parentCommentId)
            .orElseThrow { IllegalArgumentException("댓글을 찾을 수 없습니다") }

        val author = userRepository.findById(authorId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        val reply = Comment(
            content = request.content,
            post = parentComment.post,
            author = author,
            parentComment = parentComment
        )

        val saved = commentRepository.save(reply)
        return CommentResponse.from(saved)
    }

    fun deleteComment(commentId: Long, userId: Long): Unit {
        val comment = commentRepository.findById(commentId)
            .orElseThrow { IllegalArgumentException("댓글을 찾을 수 없습니다") }

        if (comment.author!!.id != userId) {
            throw IllegalArgumentException("삭제 권한이 없습니다")
        }

        commentRepository.delete(comment)
    }
}
