package com.project.backendapi.domain.dto

data class NeighborPostDTO(
    val id: Long,
    val title: String
)

data class NeighborsResponse(
    val previousPost: NeighborPostDTO?,
    val nextPost: NeighborPostDTO?
)
