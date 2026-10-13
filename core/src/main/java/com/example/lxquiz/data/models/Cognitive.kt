package com.example.lxquiz.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Concept(
    val id: String = "",
    val name: String = "",
    val dependencies: List<String> = emptyList(),
    val relatedConcepts: List<String> = emptyList(),
    val misconceptions: List<String> = emptyList(),
    val gradeLevel: String = ""
)

@Serializable
data class KnowledgeState(
    val mastery: Double = 0.0,
    val reasoning: Double = 0.0,
    val transferability: Double = 0.0,
    val confidence: Double = 0.0,
    val misconceptionRisk: Double = 0.0,
    val understandingQuotient: Int = 0
)

@Serializable
data class ExplanationAnalysis(
    val strongConcepts: List<String> = emptyList(),
    val weakConcepts: List<String> = emptyList(),
    val untestedConcepts: List<String> = emptyList(),
    val misconceptions: List<String> = emptyList(),
    val rubricLevel: Int = 0, // 0 to 5
    val feedback: String = ""
)

@Serializable
data class ForgottenConceptReport(
    val currentTopicId: String = "",
    val forgottenDependencyId: String = "",
    val forgottenDependencyName: String = "",
    val originalGradeOrYear: String = "",
    val diagnosticQuestion: Question? = null
)
