package com.b4kancs.scoutlaws.logger

import android.util.Log
import com.b4kancs.scoutlaws.App
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

/**
 * Created by hszilard on 06-Dec-18.
 */
class Logger {

    companion object {
        var logger: LoggerI? = null
            set(value) {
                field = value
                field?.log(Log.INFO, "Logger.Companion", "Logger changed to: $field")
            }

        @JvmStatic
        fun log(level: Int, logTag: String?, message: String) = logger?.log(level, logTag, message)
    }
}

interface LoggerI {
    fun log(level: Int, logTag: String?, message: String)
}

class CrashlyticsLogger : LoggerI {

    init {
        Fabric.with(App.getInstance(), Crashlytics())
    }

    override fun log(level: Int, logTag: String?, message: String) =
            if (level >= 5)
                Crashlytics.logException(Throwable("level: $level; logtag: $logTag; error: $message"))
            else
                Crashlytics.log(level, logTag, message)
}

class DefaultLogger : LoggerI {
    override fun log(level: Int, logTag: String?, message: String) {
        val l: (String, String) -> Int = when (level) {
            Log.VERBOSE -> Log::v
            Log.DEBUG -> Log::d
            Log.INFO -> Log::i
            Log.WARN -> Log::w
            Log.ERROR -> Log::e
            Log.ASSERT -> Log::wtf
            else -> throw IllegalArgumentException("Illegal log level: $level")
        }
        l(logTag ?: "", message)
    }
}
