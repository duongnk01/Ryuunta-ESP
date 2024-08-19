package ryuunta.iot.ryuuntaesp.utils

import android.util.Log

object RLog {
    fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    fun w(tag: String, msg: String) {
        Log.w(tag, msg)
    }
}