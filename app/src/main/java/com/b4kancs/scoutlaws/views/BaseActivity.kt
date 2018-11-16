package com.b4kancs.scoutlaws.views

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.b4kancs.scoutlaws.getBaseContextWithCurrentLocale

/**
 * Created by hszilard on 16-Nov-18.
 */
open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            super.attachBaseContext(getBaseContextWithCurrentLocale(newBase!!))
        else
            super.attachBaseContext(newBase)
    }
}