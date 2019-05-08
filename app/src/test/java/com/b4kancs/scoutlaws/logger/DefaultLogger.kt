package com.b4kancs.scoutlaws.logger

import android.util.Log

/**
 * Created by b4kan on 06-Dec-18.
 */
class DefaultLogger : LoggerI {

    override fun log(level: Int, log_tag: String?, message: String) {
        when (level) {
            Log.VERBOSE -> Log.v(log_tag, message)
            Log.DEBUG -> Log.d(log_tag, message)
            Log.INFO -> Log.i(log_tag, message)
            Log.WARN -> Log.w(log_tag, message)
            Log.ERROR -> Log.e(log_tag, message)
        }
    }
}
