package com.example.lxquiz.domain.alexa

import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class AlexaToolDefinition(
    val name: String,
    val description: String,
    val parameters: Map<String, String>
)

@Serializable
data class AlexaToolResult(
    val success: Boolean,
    val message: String,
    val toolName: String = "",
    val requestPayload: String = "",
    val data: String? = null
)

@Singleton
class AlexaAgentManager @Inject constructor() {

    suspend fun processVoiceCommand(command: String): AlexaToolResult {
        val lower = command.trim().lowercase()

        return when {
            // General AI Assistant Conversations
            lower == "hi" || lower == "hello" || lower == "hey" || lower == "greetings" -> {
                AlexaToolResult(
                    success = true,
                    message = "Hello! Welcome back to LxQuiz.\nHow can I help you today?",
                    toolName = "greeting"
                )
            }
            lower.contains("how are you") -> {
                AlexaToolResult(
                    success = true,
                    message = "I'm doing well and ready to help you learn, compete, and improve your ranking.",
                    toolName = "statusCheck"
                )
            }
            lower.contains("who are you") || lower.contains("what are you") -> {
                AlexaToolResult(
                    success = true,
                    message = "I am Lx Assistant, your AI assistant specialized for competitive learning on LxQuiz. I can help you create challenges, start quiz battles, view leaderboards, generate practice quizzes, and track your progress.",
                    toolName = "identity"
                )
            }
            lower.contains("thank") -> {
                AlexaToolResult(
                    success = true,
                    message = "You're very welcome! Let me know whenever you're ready for your next quiz or battle.",
                    toolName = "gratitude"
                )
            }
            lower == "help" || lower.contains("what can you do") -> {
                AlexaToolResult(
                    success = true,
                    message = "I can help you create challenges, start quiz battles, view leaderboards, generate practice quizzes, and track your progress. Try saying: 'Challenge Harry to a science battle', 'Generate a math quiz', or 'Show rankings'.",
                    toolName = "help"
                )
            }

            // LxQuiz Specialized AI Assistant Actions
            lower.contains("challenge") || lower.contains("battle") -> {
                val opponent = when {
                    lower.contains("challenge ") -> {
                        val after = lower.substringAfter("challenge ")
                        val namePart = if (after.contains(" to ")) after.substringBefore(" to ") else after.substringBefore(" in ")
                        namePart.trim().split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                    }
                    lower.contains("against ") -> {
                        lower.substringAfter("against ").trim().split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                    }
                    else -> "Harry"
                }

                val subject = when {
                    lower.contains("science") -> "Science"
                    lower.contains("math") -> "Mathematics"
                    lower.contains("physics") -> "Physics"
                    lower.contains("chemistry") -> "Chemistry"
                    lower.contains("biology") -> "Biology"
                    lower.contains("history") -> "History"
                    lower.contains("literature") -> "Literature"
                    lower.contains("computer") -> "Computer Science"
                    else -> "General Knowledge"
                }

                val payload = """
                    {
                      "tool": "createChallenge",
                      "arguments": {
                        "opponent": "$opponent",
                        "category": "$subject"
                      }
                    }
                """.trimIndent()

                AlexaToolResult(
                    success = true,
                    message = "Creating a $subject Challenge against $opponent. Launching battle arena...",
                    toolName = "createChallenge",
                    requestPayload = payload,
                    data = "$subject:$opponent"
                )
            }

            lower.contains("leaderboard") || lower.contains("ranking") || lower.contains("rank") -> {
                val payload = """
                    {
                      "tool": "getLeaderboard",
                      "arguments": {
                        "scope": "global"
                      }
                    }
                """.trimIndent()

                AlexaToolResult(
                    success = true,
                    message = "Opening Leaderboard. You are currently ranked #1 with 1,250 XP! Keep competing to maintain your streak.",
                    toolName = "getLeaderboard",
                    requestPayload = payload,
                    data = "rank_1"
                )
            }

            lower.contains("tournament") -> {
                AlexaToolResult(
                    success = true,
                    message = "You have 2 active tournaments available: National Science Bowl and Global Math Arena. Navigating to tournaments...",
                    toolName = "getTournaments",
                    data = "tournaments"
                )
            }

            lower.contains("daily") || lower.contains("streak") -> {
                AlexaToolResult(
                    success = true,
                    message = "Your daily streak is 5 days! Complete today's UNESCO MIL Challenge to earn +50 XP and extend your streak.",
                    toolName = "getDailyChallenge",
                    data = "daily_challenge"
                )
            }

            lower.contains("generate") || lower.contains("practice") || lower.contains("quiz") -> {
                val subject = when {
                    lower.contains("science") -> "Science"
                    lower.contains("math") || lower.contains("calc") || lower.contains("algebra") -> "Mathematics"
                    lower.contains("physics") -> "Physics"
                    lower.contains("chemistry") -> "Chemistry"
                    lower.contains("biology") -> "Biology"
                    lower.contains("history") -> "History"
                    lower.contains("literature") || lower.contains("english") -> "Literature"
                    lower.contains("computer") || lower.contains("tech") -> "Computer Science"
                    else -> "General Knowledge"
                }
                
                val payload = """
                    {
                      "tool": "generatePracticeQuiz",
                      "arguments": {
                        "category": "$subject",
                        "count": 10
                      }
                    }
                """.trimIndent()

                AlexaToolResult(
                    success = true,
                    message = "Generating a 10-question $subject quiz tailored to your curriculum. Launching quiz...",
                    toolName = "generatePracticeQuiz",
                    requestPayload = payload,
                    data = subject
                )
            }

            // Intelligent Fallback (never say "Wrong prompt")
            else -> {
                AlexaToolResult(
                    success = false,
                    message = "I'm not sure I understood that.\n\nI can help you create challenges, start quiz battles, view leaderboards, generate practice quizzes, and track your progress.",
                    toolName = "fallback"
                )
            }
        }
    }
}
