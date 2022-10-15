package com.gurpgork.countthis.settings

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : FragmentActivity() {//ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment = SettingsPreferenceFragment()
//        deprecated
//        fragmentManager.beginTransaction()
//            .replace(android.R.id.content, fragment)
//            .commit()

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .commit()
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}