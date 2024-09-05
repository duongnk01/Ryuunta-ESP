package ryuunta.iot.ryuuntaesp.utils

import java.util.Timer
import java.util.TimerTask

class TimeUtils {
    private var timer: Timer = Timer()
    private var timeTask: TimerTask? = null
    private var timeElapsed = 0

    fun startTimer(maxTime: Int, onRun: (timeRemaining: Int) -> Unit, onFinish: () -> Unit) {
        timeTask = object : TimerTask() {
            override fun run() {
                onRun(maxTime - timeElapsed)
                if (timeElapsed == maxTime) {
                    onFinish()
                    onCancel()
                }
                timeElapsed++
            }

        }
        timer.schedule(timeTask, 0, 1000)
    }

    fun onCancel() {
        timer.cancel()
        timer.purge()
        timeTask?.cancel()
    }
}