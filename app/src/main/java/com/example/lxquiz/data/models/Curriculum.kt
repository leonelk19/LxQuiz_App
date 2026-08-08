package com.example.lxquiz.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val id: String = "",
    val name: String = "",
    val flagEmoji: String = ""
)

@Serializable
enum class EducationLevel {
    PRIMARY, SECONDARY, UNIVERSITY
}

@Serializable
data class SubLevel(
    val id: String,
    val name: String,
    val sections: List<EducationSection> = emptyList()
)

@Serializable
data class EducationSection(
    val id: String,
    val name: String
)

@Serializable
data class Subject(
    val id: String = "",
    val name: String = "",
    val icon: String = "",
    val description: String = "",
    val field: String = "" // Technical, Sciences, Commercial, Arts
)

@Serializable
data class UserProfile(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val countryId: String = "",
    val educationLevel: EducationLevel? = null,
    val subLevelId: String = "",
    val sectionId: String = "",
    val selectedSubjects: List<String> = emptyList(),
    val points: Int = 0,
    val badges: List<String> = emptyList(),
    val streak: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
