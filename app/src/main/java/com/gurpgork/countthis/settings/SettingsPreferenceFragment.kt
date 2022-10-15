package com.gurpgork.countthis.settings

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.net.toUri
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.gurpgork.countthis.R
import com.gurpgork.countthis.extensions.resolveThemeColor

class SettingsPreferenceFragment : PreferenceFragmentCompat(){

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<Preference>("privacy_policy")?.setOnPreferenceClickListener {
            context?.let { context ->
                CustomTabsIntent.Builder()
                    .setColorScheme(context.resolveThemeColor(android.R.attr.colorPrimary))
//                    .setToolbarColor(context.resolveThemeColor(android.R.attr.colorPrimary))
                    .build()
                    .launchUrl(context, getString(R.string.privacy_policy_url).toUri())
            }
            true
        }

        findPreference<Preference>("version")?.apply {
            val pkgManager: PackageManager = context.packageManager
            val pkgInfo = pkgManager.getPackageInfo(context.packageName, 0)
            summary = getString(
                R.string.settings_app_version_summary,
                pkgInfo.versionName,
                PackageInfoCompat.getLongVersionCode(pkgInfo)
            )
        }
    }

}