package com.example.lxquiz.data.repository

import com.example.lxquiz.data.models.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CognitiveDiagnosticRepository @Inject constructor() {

    // Sample concept graph knowledge base
    private val conceptGraph = mapOf(
        "photosynthesis" to Concept(
            id = "photosynthesis",
            name = "Photosynthesis",
            dependencies = listOf("sunlight", "chlorophyll", "carbon_dioxide", "chemical_reactions"),
            relatedConcepts = listOf("climate", "agriculture", "economics"),
            misconceptions = listOf("sunlight_is_food"),
            gradeLevel = "High School"
        ),
        "chemical_reactions" to Concept(
            id = "chemical_reactions",
            name = "Chemical Reactions",
            dependencies = listOf("matter_conservation", "atoms_and_molecules"),
            relatedConcepts = listOf("energy_transfer"),
            misconceptions = listOf("matter_disappears"),
            gradeLevel = "Middle School"
        )
    )

    fun analyzeExplanation(topicId: String, explanationText: String): ExplanationAnalysis {
        val topicKey = topicId.lowercase().replace(" ", "_")
        val concept = conceptGraph[topicKey] ?: Concept(
            id = topicId,
            name = topicId,
            dependencies = listOf("core_principles", "underlying_mechanism", "key_concepts")
        )
        val lowerText = explanationText.trim().lowercase()

        val strong = mutableListOf<String>()
        val weak = mutableListOf<String>()
        val untested = mutableListOf<String>()
        val misconceptions = mutableListOf<String>()

        val words = lowerText.split("\\s+".toRegex()).filter { it.isNotBlank() }

        // Check dependencies mentioned or missed flexible token matching
        for (dep in concept.dependencies) {
            val depClean = dep.replace("_", " ")
            val depTokens = depClean.split(" ").filter { it.length > 3 }
            val isMentioned = lowerText.contains(depClean) || lowerText.contains(dep) || depTokens.any { lowerText.contains(it) }

            if (isMentioned) {
                strong.add(depClean.replaceFirstChar { it.uppercase() })
            } else {
                untested.add(depClean.replaceFirstChar { it.uppercase() })
            }
        }

        // Check misconceptions
        for (misc in concept.misconceptions) {
            if (lowerText.contains(misc.replace("_", " ")) || lowerText.contains(misc)) {
                misconceptions.add(misc.replace("_", " "))
            }
        }

        // Rubric Level determination based on depth and reasoning (0-5)
        val rubricLevel = when {
            lowerText.isBlank() -> 0
            words.size >= 25 -> 5
            words.size >= 20 -> 4
            words.size >= 10 -> 3
            words.size >= 4 -> 2
            else -> 1
        }

        val feedback = when (rubricLevel) {
            0 -> "No voice or text explanation provided. Explanation is required to evaluate cognitive understanding."
            1 -> "Superficial recall detected. Expand on why this choice is correct."
            2 -> "Basic reasoning provided. Connecting key mechanisms and principles improves mastery."
            3 -> "Good reasoned explanation! Primary concepts correctly linked."
            4 -> "Strong conceptual grasp demonstrated with solid reasoning depth."
            else -> "Mastery level explanation with thorough conceptual linkage!"
        }

        return ExplanationAnalysis(
            strongConcepts = strong,
            weakConcepts = weak,
            untestedConcepts = untested,
            misconceptions = misconceptions,
            rubricLevel = rubricLevel,
            feedback = feedback
        )
    }

    fun calculateKnowledgeState(analysis: ExplanationAnalysis): KnowledgeState {
        val mastery = (analysis.strongConcepts.size.toDouble() / (analysis.strongConcepts.size + analysis.untestedConcepts.size + 1.0)) * 100.0
        val reasoning = analysis.rubricLevel * 20.0
        val transferability = if (analysis.rubricLevel >= 4) 85.0 else 50.0
        val confidence = 75.0
        val misconceptionRisk = if (analysis.misconceptions.isNotEmpty()) 80.0 else 10.0

        val uq = ((mastery * 0.3) + (reasoning * 0.3) + (transferability * 0.2) + ((100 - misconceptionRisk) * 0.2)).toInt()

        return KnowledgeState(
            mastery = mastery,
            reasoning = reasoning,
            transferability = transferability,
            confidence = confidence,
            misconceptionRisk = misconceptionRisk,
            understandingQuotient = uq.coerceIn(0, 100)
        )
    }

    fun discoverForgottenDependency(currentTopicId: String): ForgottenConceptReport? {
        val concept = conceptGraph[currentTopicId] ?: return null
        val forgottenDepId = concept.dependencies.firstOrNull() ?: return null
        val forgottenConcept = conceptGraph[forgottenDepId] ?: Concept(id = forgottenDepId, name = "Prerequisite Concept")

        return ForgottenConceptReport(
            currentTopicId = currentTopicId,
            forgottenDependencyId = forgottenDepId,
            forgottenDependencyName = forgottenConcept.name,
            originalGradeOrYear = forgottenConcept.gradeLevel,
            diagnosticQuestion = Question(
                id = "diag_$forgottenDepId",
                text = "Diagnostic Check: What happens when reactants or prerequisites in '${forgottenConcept.name}' become limiting?",
                options = listOf("Increase", "Decrease", "Stay Constant", "Double"),
                correctAnswerIndex = 1,
                explanation = "Understanding ${forgottenConcept.name} is vital for mastery of current topics.",
                subjectId = currentTopicId,
                difficulty = "Diagnostic"
            )
        )
    }
}
