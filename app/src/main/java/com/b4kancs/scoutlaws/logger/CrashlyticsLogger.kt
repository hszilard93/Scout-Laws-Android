package com.b4kancs.scoutlaws.logger

/**
 * Created by hszilard on 06-Dec-18.
 */
class CrashlyticsLogger : LoggerI {

    override fun log(level: Int, log_tag: String?, message: String) {
        com.crashlytics.android.Crashlytics.log(level, log_tag, message)
    }
}
