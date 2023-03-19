package com.gurpgork.countthis.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() { //AppCompatActivity() { //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment = SettingsPreferenceFragment()
//        deprecated but support fragment manager   ListPreference is broken...
        fragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .commit()

//        actionBar?.setDisplayHomeAsUpEnabled(true)
//        actionBar?.setDisplayShowHomeEnabled(true)

//        supportFragmentManager
//            .beginTransaction()
//            .replace(android.R.id.content, fragment)
//            .commit()
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}