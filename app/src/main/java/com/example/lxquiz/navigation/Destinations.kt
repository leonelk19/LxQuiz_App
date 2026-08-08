package com.example.lxquiz.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    data object Welcome : Destination
    
    @Serializable
    data object Login : Destination
    
    @Serializable
    data object SignUp : Destination
    
    @Serializable
    data class CurriculumSelection(
        val countryId: String? = null, 
        val level: String? = null
    ) : Destination
    
    @Serializable
    data object Home : Destination
    
    @Serializable
    data object FieldSelection : Destination
    
    @Serializable
    data class SubjectSelection(val field: String) : Destination
    
    @Serializable
    data class Loading(val subject: String) : Destination
    
    @Serializable
    data object Quiz : Destination
    
    @Serializable
    data object Result : Destination
    
    @Serializable
    data object Review : Destination
    
    @Serializable
    data object Analytics : Destination
    
    @Serializable
    data object Challenge : Destination
    
    @Serializable
    data object Competition : Destination
    
    @Serializable
    data object BattleInstructions : Destination
    
    @Serializable
    data object BattleRoom : Destination
    
    @Serializable
    data object Leaderboard : Destination
    
    @Serializable
    data object Profile : Destination
    
    @Serializable
    data object Assistant : Destination
}
