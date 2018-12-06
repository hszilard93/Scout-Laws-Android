package com.b4kancs.scoutlaws.logger

/**
 * Created by hszilard on 06-Dec-18.
 */
class Logger {
    companion object {
        private var logger: LoggerI? = null

        fun setUpLogger(logger: LoggerI) {
            if (this.logger == null)
                this.logger = logger
        }

        @JvmStatic
        fun log(level: Int, log_tag: String?, message: String) {
            if (logger != null)
                logger?.log(level, log_tag, message)
        }
    }
}

interface LoggerI {
    fun log(level: Int, log_tag: String?, message: String)
}
