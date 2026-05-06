package com.project.backendapi.service

import com.project.backendapi.domain.dto.CreatePostRequest
import com.project.backendapi.domain.dto.NeighborPostDTO
import com.project.backendapi.domain.dto.NeighborsResponse
import com.project.backendapi.domain.dto.PostResponse
import com.project.backendapi.domain.dto.UserSimpleResponse
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
    fun getPosts(pageable: Pageable, userId: Long? = null, category: com.project.backendapi.domain.entity.Category? = null): Page<PostResponse> {
        println("📌 getPosts: userId = $userId, category = $category")
        val posts = if (category != null) {
            postRepository.findByCategoryOrderByCreatedAtDesc(category, pageable)
        } else {
            postRepository.findAllByOrderByCreatedAtDesc(pageable)
        }
        
        return posts.map { post ->
            val likeCount = postLikeRepository.countByPostId(post.id!!)
            val liked = userId?.let { postLikeRepository.existsByPostIdAndUserId(post.id!!, it) } ?: false
            println("📌 Post ${post.id}: likeCount=$likeCount, liked=$liked, userId=$userId")

            PostResponse(
                id = post.id!!,
                title = post.title,
                content = post.content,
                imageUrl = post.imageUrl,
                category = post.category,
                author = UserSimpleResponse.from(post.author!!),
                createdAt = post.createdAt,
                updatedAt = post.updatedAt,
                viewCount = post.viewCount,
                commentCount = post.comments.size,
                likeCount = likeCount,
                liked = liked
            )
        }
    }

    @Transactional
    fun getPostById(id: Long, userId: Long? = null): PostResponse {
        println("📌 getPostById: id=$id, userId=$userId")
        val post = postRepository.findById(id).orElseThrow {
            IllegalArgumentException("게시글을 찾을 수 없습니다")
        }

        println("📌 Before increment: viewCount=${post.viewCount}")
        post.viewCount++
        println("📌 After increment: viewCount=${post.viewCount}")

        val likeCount = postLikeRepository.countByPostId(post.id!!)
        val liked = userId?.let { postLikeRepository.existsByPostIdAndUserId(post.id!!, it) } ?: false
        println("📌 Post ${post.id}: likeCount=$likeCount, liked=$liked, userId=$userId")

        return PostResponse(
            id = post.id!!,
            title = post.title,
            content = post.content,
            imageUrl = post.imageUrl,
            category = post.category,
            author = UserSimpleResponse.from(post.author!!),
            createdAt = post.createdAt,
            updatedAt = post.updatedAt,
            viewCount = post.viewCount,
            commentCount = post.comments.size,
            likeCount = likeCount,
            liked = liked
        )
    }

    fun createPost(authorId: Long, request: CreatePostRequest, imageUrl: String? = null): PostResponse {
        val author = userRepository.findById(authorId).orElseThrow {
            IllegalArgumentException("사용자를 찾을 수 없습니다")
        }

        val post = Post(
            title = request.title,
            content = request.content,
            imageUrl = imageUrl,
            category = request.category,
            author = author
        )

        val savedPost = postRepository.save(post)

        return PostResponse(
            id = savedPost.id!!,
            title = savedPost.title,
            content = savedPost.content,
            imageUrl = savedPost.imageUrl,
            category = savedPost.category,
            author = UserSimpleResponse.from(savedPost.author!!),
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

    @Transactional(readOnly = true)
    fun getPreviousPost(currentPostId: Long): NeighborPostDTO? {
        return postRepository.findPreviousPost(currentPostId)
    }

    @Transactional(readOnly = true)
    fun getNextPost(currentPostId: Long): NeighborPostDTO? {
        return postRepository.findNextPost(currentPostId)
    }

    @Transactional(readOnly = true)
    fun getNeighborPosts(currentPostId: Long): NeighborsResponse {
        // 게시글 존재 여부 확인
        postRepository.findById(currentPostId).orElseThrow {
            IllegalArgumentException("게시글을 찾을 수 없습니다")
        }

        val previousPost = postRepository.findPreviousPost(currentPostId)
        val nextPost = postRepository.findNextPost(currentPostId)

        return NeighborsResponse(
            previousPost = previousPost,
            nextPost = nextPost
        )
    }
}
