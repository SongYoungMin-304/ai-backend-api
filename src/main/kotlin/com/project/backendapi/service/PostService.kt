package com.project.backendapi.service

import com.project.backendapi.domain.dto.CreatePostRequest
import com.project.backendapi.domain.dto.PostResponse
import com.project.backendapi.domain.entity.Post
import com.project.backendapi.domain.repository.PostRepository
import com.project.backendapi.domain.repository.PostLikeRepository
import com.project.backendapi.domain.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
    private val postLikeRepository: PostLikeRepository,
    private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    fun getPosts(pageable: Pageable, userId: Long? = null): Page<PostResponse> {
        println("📌 getPosts: userId = $userId")
        return postRepository.findAllByOrderByCreatedAtDesc(pageable)
            .map { post ->
                val likeCount = postLikeRepository.countByPostId(post.id!!)
                val isLiked = userId?.let { postLikeRepository.existsByPostIdAndUserId(post.id!!, it) } ?: false
                println("📌 Post ${post.id}: likeCount=$likeCount, isLiked=$isLiked, userId=$userId")

                PostResponse(
                    id = post.id!!,
                    title = post.title,
                    content = post.content,
                    author = UserSimpleResponse(
                        id = post.author!!.id!!,
                        username = post.author!!.username,
                        profileImage = post.author!!.profileImage
                    ),
                    createdAt = post.createdAt,
                    updatedAt = post.updatedAt,
                    viewCount = post.viewCount,
                    commentCount = post.comments.size,
                    likeCount = likeCount,
                    isLiked = isLiked
                )
            }
    }

    @Transactional(readOnly = true)
    fun getPostById(id: Long, userId: Long? = null): PostResponse {
        println("📌 getPostById: id=$id, userId=$userId")
        val post = postRepository.findById(id).orElseThrow {
            IllegalArgumentException("게시글을 찾을 수 없습니다")
        }

        post.viewCount++
        postRepository.save(post)

        val likeCount = postLikeRepository.countByPostId(post.id!!)
        val isLiked = userId?.let { postLikeRepository.existsByPostIdAndUserId(post.id!!, it) } ?: false
        println("📌 Post ${post.id}: likeCount=$likeCount, isLiked=$isLiked, userId=$userId")

        return PostResponse(
            id = post.id!!,
            title = post.title,
            content = post.content,
            author = UserSimpleResponse(
                id = post.author!!.id!!,
                username = post.author!!.username,
                profileImage = post.author!!.profileImage
            ),
            createdAt = post.createdAt,
            updatedAt = post.updatedAt,
            viewCount = post.viewCount,
            commentCount = post.comments.size,
            likeCount = likeCount,
            isLiked = isLiked
        )
    }

    fun createPost(authorId: Long, request: CreatePostRequest): PostResponse {
        val author = userRepository.findById(authorId).orElseThrow {
            IllegalArgumentException("사용자를 찾을 수 없습니다")
        }

        val post = Post(
            title = request.title,
            content = request.content,
            author = author
        )

        val savedPost = postRepository.save(post)

        return PostResponse(
            id = savedPost.id!!,
            title = savedPost.title,
            content = savedPost.content,
            author = UserSimpleResponse(
                id = savedPost.author!!.id!!,
                username = savedPost.author!!.username,
                profileImage = savedPost.author!!.profileImage
            ),
            createdAt = savedPost.createdAt,
            updatedAt = savedPost.updatedAt,
            viewCount = savedPost.viewCount,
            commentCount = savedPost.comments.size
        )
    }

    fun deletePost(postId: Long, userId: Long) {
        val post = postRepository.findById(postId).orElseThrow {
            IllegalArgumentException("게시글을 찾을 수 없습니다")
        }

        if (post.author!!.id != userId) {
            throw IllegalArgumentException("삭제 권한이 없습니다")
        }

        postRepository.delete(post)
    }
}

data class UserSimpleResponse(
    val id: Long,
    val username: String,
    val profileImage: String?
)
