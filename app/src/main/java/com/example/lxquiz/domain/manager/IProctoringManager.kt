package com.example.lxquiz.domain.manager

import kotlinx.coroutines.flow.Flow

sealed class ProctoringEvent {
    object MultipleFacesDetected : ProctoringEvent()
    object NoFaceDetected : ProctoringEvent()
    object UserLookingAway : ProctoringEvent()
    object ExcessiveNoiseDetected : ProctoringEvent()
    object ExternalDisplayDetected : ProctoringEvent()
    object ExternalInputDetected : ProctoringEvent()
    data class AbnormalSensorReading(val sensorName: String) : ProctoringEvent()
}

interface IProctoringManager {
    /**
     * Starts camera-based proctoring (Face and Gaze tracking).
     */
    fun startCameraProctoring()

    /**
     * Starts audio-based proctoring (Noise detection).
     */
    fun startAudioProctoring()

    /**
     * Starts sensor-based proctoring (Light, Proximity).
     */
    fun startSensorProctoring()

    /**
     * Stops all active proctoring.
     */
    fun stopProctoring()

    /**
     * Flow of suspicious events detected during the session.
     */
    val proctoringEvents: Flow<ProctoringEvent>
}
