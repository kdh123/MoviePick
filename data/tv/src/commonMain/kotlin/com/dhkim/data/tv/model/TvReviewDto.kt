package com.dhkim.data.tv.model

import com.dhkim.common.Review
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvReviewDto(
    val id: Int,
    val page: Int,
    val results: List<ReviewResult>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class ReviewResult(
    val author: String,
    @SerialName("author_details")
    val authorDetails: AuthorDetails,
    val content: String,
    @SerialName("created_at")
    val createdAt: String,
    val id: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val url: String
) {
    fun toReview(): Review {
        return Review(
            id = id,
            author = author,
            createdAt = createdAt,
            content = content,
            rating = authorDetails.rating ?: 0.0,
        )
    }
}

@Serializable
data class AuthorDetails(
    @SerialName("avatar_path")
    val avatarPath: String?,
    val name: String,
    val rating: Double?,
    val username: String
)