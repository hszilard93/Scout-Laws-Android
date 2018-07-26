package com.b4kancs.scoutlaws.views.settings

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.preference.DialogPreference
import android.util.AttributeSet
import com.b4kancs.scoutlaws.R

/**
 * Created by hszilard on 17-Jul-18.
 * Time picker preference for the Settings menu.
 * Based on https://github.com/jakobulbrich/preferences-demo/blob/master/app/src/main/java/de/jakobulbrich/preferences/TimePreference.java
 */
class TimePreference
@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.preferenceStyle,
        defStyleRes: Int = defStyleAttr
) : DialogPreference(context, attrs, defStyleAttr, defStyleRes) {

    /* Save to SharedPreference in minutes after midnight */
    var time: Int = 0
        set(time) {
            field = time
            persistInt(time)
        }

    private val dialogLayoutResId = R.layout.pref_dialog_time

    /* Called when a Preference is being inflated and the default value attribute needs to be read */
    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        return a!!.getInt(index, 0)
    }

    /* Returns the layout resource that is used as the content View for the dialog */
    override fun getDialogLayoutResource(): Int {
        return dialogLayoutResId
    }

    /* Implement this to set the initial value of the Preference */
    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        time = if (restorePersistedValue)
            getPersistedInt(time)
        else
            defaultValue as Int
    }
}