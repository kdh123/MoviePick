package com.dhkim.data.tv.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvCreditsDto(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)

@Serializable
data class Cast(
    val adult: Boolean,
    val character: String,
    @SerialName("credit_id")
    val creditId: String,
    val gender: Int,
    val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    val name: String,
    val order: Int,
    @SerialName("original_name")
    val originalName: String,
    val popularity: Double,
    @SerialName("profile_path")
    val profilePath: String?
)

@Serializable
data class Crew(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    val popularity: Double,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("credit_id")
    val creditId: String,
    val department: String,
    val job: String
)