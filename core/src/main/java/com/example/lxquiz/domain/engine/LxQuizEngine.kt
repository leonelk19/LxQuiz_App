package com.example.lxquiz.domain.engine

import com.example.lxquiz.data.models.*
import javax.inject.Inject
import javax.inject.Singleton

data class SessionConceptState(
    val currentTopic: String = "",
    val strongConcepts: Set<String> = emptySet(),
    val weakConcepts: Set<String> = emptySet(),
    val untestedConcepts: Set<String> = emptySet(),
    val misconceptions: Set<String> = emptySet()
)

data class CognitiveAnalysisResult(
    val updatedState: SessionConceptState,
    val rubricLevel: Int,
    val feedback: String,
    val nextQuestion: Question
)

@Singleton
class LxQuizEngine @Inject constructor() {

    // Dependency Graph Knowledge Base mapping primary topics to prior prerequisite concepts
    private val dependencyGraph = mapOf(
        "photosynthesis" to listOf(
            Concept("cell_structure", "Cell Structure & Chloroplasts", listOf("cells"), gradeLevel = "Middle School"),
            Concept("energy_transfer", "Energy Conversion & Transfer", listOf("thermodynamics"), gradeLevel = "Middle School"),
            Concept("chemical_reactions", "Chemical Reactions & Reactants", listOf("matter_conservation"), gradeLevel = "Middle School"),
            Concept("carbon_cycle", "Carbon Cycle & Gas Exchange", listOf("ecology"), gradeLevel = "High School")
        ),
        "chemical_reactions" to listOf(
            Concept("matter_conservation", "Law of Conservation of Mass", emptyList(), gradeLevel = "Middle School"),
            Concept("atomic_structure", "Atoms and Bonding", emptyList(), gradeLevel = "Middle School")
        )
    )

    fun processAnswerAndExplanation(
        topicId: String,
        currentQuestion: Question,
        selectedOptionIndex: Int,
        explanationText: String,
        currentState: SessionConceptState
    ): CognitiveAnalysisResult {
        val lowerExplanation = explanationText.lowercase().trim()
        val topicKey = topicId.lowercase().replace(" ", "_")

        val dependencies = dependencyGraph[topicKey] ?: listOf(
            Concept("cell_structure", "Cell Structure & Chloroplasts", listOf("cells"), gradeLevel = "Middle School"),
            Concept("energy_transfer", "Energy Conversion & Transfer", listOf("thermodynamics"), gradeLevel = "Middle School"),
            Concept("chemical_reactions", "Chemical Reactions & Reactants", listOf("matter_conservation"), gradeLevel = "Middle School"),
            Concept("carbon_cycle", "Carbon Cycle & Gas Exchange", listOf("ecology"), gradeLevel = "High School")
        )

        val newStrong = currentState.strongConcepts.toMutableSet()
        val newWeak = currentState.weakConcepts.toMutableSet()
        val newUntested = currentState.untestedConcepts.toMutableSet()
        val newMisconceptions = currentState.misconceptions.toMutableSet()

        val isCorrect = selectedOptionIndex == currentQuestion.correctAnswerIndex

        // Extract concepts from speech-to-text / typed explanation
        for (dep in dependencies) {
            val depNameLower = dep.name.lowercase()
            val depTokens = depNameLower.split(" ", "_", "&", "-").filter { it.length > 3 }
            val isMentioned = depTokens.any { lowerExplanation.contains(it) } || 
                    lowerExplanation.contains(depNameLower) || 
                    lowerExplanation.contains(dep.id.replace("_", " "))

            if (isCorrect && (isMentioned || lowerExplanation.length > 15)) {
                newStrong.add(dep.name)
                newUntested.remove(dep.name)
            } else if (explanationText.isNotBlank()) {
                if (isMentioned) {
                    newStrong.add(dep.name)
                } else {
                    newWeak.add(dep.name)
                }
            } else {
                newUntested.add(dep.name)
            }
        }

        // Misconception detection
        if (lowerExplanation.contains("eat sunlight") || lowerExplanation.contains("sunlight is food")) {
            newMisconceptions.add("sunlight_is_food_misconception")
        }

        // Calculate rubric level (0-5)
        val wordCount = lowerExplanation.split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
        val rubricLevel = when {
            lowerExplanation.isBlank() -> 0
            wordCount >= 25 -> 5
            wordCount >= 20 -> 4
            wordCount >= 10 -> 3
            wordCount >= 4 -> 2
            else -> 1
        }

        val updatedState = SessionConceptState(
            currentTopic = topicId,
            strongConcepts = newStrong,
            weakConcepts = newWeak,
            untestedConcepts = newUntested,
            misconceptions = newMisconceptions
        )

        // Select or generate the next best independent question targeting weak or untested prerequisite dependencies
        val nextQuestion = generateNextDependencyQuestion(topicId, updatedState)

        val feedback = when (rubricLevel) {
            0 -> "No voice explanation provided. Explanation is required to evaluate underlying reasoning."
            1 -> "Surface recall detected. Connecting underlying concepts like cell structure improves mastery."
            2 -> "Basic reasoning provided. Try linking energy conversion and chemical reactions."
            3 -> "Solid explanation connecting primary concepts!"
            4 -> "Deep reasoning demonstrated across prerequisite knowledge nodes."
            else -> "Exceptional transferable understanding across cross-subject concepts!"
        }

        return CognitiveAnalysisResult(
            updatedState = updatedState,
            rubricLevel = rubricLevel,
            feedback = feedback,
            nextQuestion = nextQuestion
        )
    }

    private fun generateNextDependencyQuestion(topicId: String, state: SessionConceptState): Question {
        // Target weak concept first, then untested concept
        val targetConcept = state.weakConcepts.firstOrNull() 
            ?: state.untestedConcepts.firstOrNull() 
            ?: "Cell Structure"

        return when {
            targetConcept.contains("cell", ignoreCase = true) -> Question(
                id = "dep_cell_${System.currentTimeMillis()}",
                text = "Which plant cell organelle contains chlorophyll and serves as the site for light absorption?",
                options = listOf("Chloroplast", "Mitochondrion", "Vacuole", "Ribosome"),
                correctAnswerIndex = 0,
                explanation = "Chloroplasts contain chlorophyll required for light absorption in photosynthesis.",
                subjectId = topicId,
                difficulty = "Prerequisite Check"
            )
            targetConcept.contains("chemical", ignoreCase = true) || targetConcept.contains("reaction", ignoreCase = true) -> Question(
                id = "dep_chem_${System.currentTimeMillis()}",
                text = "What happens to the rate of reaction when one essential reactant becomes limited in a closed system?",
                options = listOf("Reaction rate decreases", "Reaction rate doubles", "Reaction rate remains constant", "Mass increases"),
                correctAnswerIndex = 0,
                explanation = "Limiting reactants restrict product formation in chemical reactions.",
                subjectId = topicId,
                difficulty = "Dependency Diagnostic"
            )
            targetConcept.contains("carbon", ignoreCase = true) -> Question(
                id = "dep_carbon_${System.currentTimeMillis()}",
                text = "A greenhouse owner increases carbon dioxide concentration. What is the most direct effect?",
                options = listOf("Photosynthesis rate increases up to saturation", "Photosynthesis stops completely", "Chlorophyll disappears", "Plant stops absorbing water"),
                correctAnswerIndex = 0,
                explanation = "Carbon dioxide is a primary reactant in the carbon fixation stage.",
                subjectId = topicId,
                difficulty = "Transferability Diagnostic"
            )
            else -> Question(
                id = "dep_energy_${System.currentTimeMillis()}",
                text = "How does light energy transform during the light-dependent reactions of photosynthesis?",
                options = listOf("Converted to chemical energy (ATP/NADPH)", "Converted to thermal energy only", "Destroyed during reaction", "Transformed into nitrogen"),
                correctAnswerIndex = 0,
                explanation = "Light energy converts into chemical energy stored in chemical bonds.",
                subjectId = topicId,
                difficulty = "Diagnostic"
            )
        }
    }
}
