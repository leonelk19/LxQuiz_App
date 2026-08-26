package com.example.lxquiz.core.security

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.lxquiz.domain.manager.IProctoringManager
import com.example.lxquiz.domain.manager.ProctoringEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class ProctoringManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : IProctoringManager, SensorEventListener {

    private val _proctoringEvents = MutableSharedFlow<ProctoringEvent>(extraBufferCapacity = 10)
    override val proctoringEvents: SharedFlow<ProctoringEvent> = _proctoringEvents.asSharedFlow()

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    override fun startCameraProctoring() {
        // ML Kit implementation would be here
    }

    override fun startAudioProctoring() {
        // Audio analysis implementation would be here
    }

    override fun startSensorProctoring() {
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun stopProctoring() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_LIGHT -> {
                    // HARD-CODED: Detect sudden flash or screen brightness from another device
                    if (it.values[0] > 2000) { 
                        _proctoringEvents.tryEmit(ProctoringEvent.AbnormalSensorReading("Excessive Light"))
                    }
                }
                Sensor.TYPE_ACCELEROMETER -> {
                    // HARD-CODED: Detect if the phone is being passed around or hidden
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    val acceleration = sqrt((x * x) + (y * y) + (z * z))
                    if (acceleration > 25f) { // High movement threshold
                        _proctoringEvents.tryEmit(ProctoringEvent.AbnormalSensorReading("Suspicious Movement"))
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
