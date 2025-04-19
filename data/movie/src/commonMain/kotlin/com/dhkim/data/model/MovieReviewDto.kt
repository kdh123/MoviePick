package com.dhkim.data.model

import com.dhkim.common.Review
import com.dhkim.common.toDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieReviewDto(
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
            createdAt = createdAt.toDate(),
            content = content,
            rating = authorDetails.rating ?: 0.0,
            profilePath = if (authorDetails.avatarPath != null) {
                "https://image.tmdb.org/t/p/original${authorDetails.avatarPath}"
            } else {
                null
            }
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