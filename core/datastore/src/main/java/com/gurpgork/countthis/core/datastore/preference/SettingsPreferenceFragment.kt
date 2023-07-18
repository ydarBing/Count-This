
package com.gurpgork.countthis.core.datastore.preference

//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.preference.PreferenceCategory
//import android.preference.PreferenceFragment
//import android.preference.SwitchPreference
//import androidx.browser.customtabs.CustomTabsIntent
//import androidx.core.content.pm.PackageInfoCompat
//import androidx.core.net.toUri
//import com.gurpgork.countthis.R
//
//class SettingsPreferenceFragment : PreferenceFragment() {
//    //PreferenceFragmentCompat(){
//    internal var buttonIncrement: Boolean? = null
//        set(value) {
//            val pref = findPreference("pref_button_increment") as? SwitchPreference
//                ?: throw IllegalStateException()
//            field = value
//        }
//
//    internal var dynamicColors: Boolean? = null
//        set(value) {
//            val pref = findPreference("pref_dynamic_colors") as? SwitchPreference
//                ?: throw IllegalStateException()
//            field = value
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        addPreferencesFromResource(R.xml.preferences)
//
//        findPreference("privacy_policy")?.setOnPreferenceClickListener {
//            context?.let { context ->
//                CustomTabsIntent.Builder()
////                    .setColorScheme(context.resolveThemeColor(android.R.attr.colorPrimary))
////                    .setToolbarColor(context.resolveThemeColor(android.R.attr.colorPrimary))
//                    .build()
//                    .launchUrl(context, getString(R.string.privacy_policy_url).toUri())
//            }
//            true
//        }
//
//        findPreference("version")?.apply {
//            val pkgManager: PackageManager = context.packageManager
//            val pkgInfo = pkgManager.getPackageInfo(context.packageName, 0)
//            summary = getString(
//                R.string.settings_app_version_summary,
//                pkgInfo.versionName,
//                PackageInfoCompat.getLongVersionCode(pkgInfo)
//            )
//        }
//
//        if (Build.VERSION.SDK_INT < 31) {
//            val category = findPreference("pref_category_ui") as PreferenceCategory?
////            val prefToRemove = findPreference("pref_dynamic_colors")
////            category?.removePreference(prefToRemove)
//            category?.removePreference(findPreference("pref_dynamic_colors"))
//        }
//    }
////    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
////        setPreferencesFromResource(R.xml.preferences, rootKey)
////
////        findPreference<Preference>("privacy_policy")?.setOnPreferenceClickListener {
////            context?.let { context ->
////                CustomTabsIntent.Builder()
//////                    .setColorScheme(context.resolveThemeColor(android.R.attr.colorPrimary))
//////                    .setToolbarColor(context.resolveThemeColor(android.R.attr.colorPrimary))
////                    .build()
////                    .launchUrl(context, getString(R.string.privacy_policy_url).toUri())
////            }
////            true
////        }
////
////        findPreference<Preference>("version")?.apply {
////            val pkgManager: PackageManager = context.packageManager
////            val pkgInfo = pkgManager.getPackageInfo(context.packageName, 0)
////            summary = getString(
////                R.string.settings_app_version_summary,
////                pkgInfo.versionName,
////                PackageInfoCompat.getLongVersionCode(pkgInfo)
////            )
////        }
////
////        if(Build.VERSION.SDK_INT < 31){
////            val category = findPreference("pref_category_ui") as PreferenceCategory?
//////            val prefToRemove = findPreference("pref_dynamic_colors")
//////            category?.removePreference(prefToRemove)
////            category?.removePreferenceRecursively("pref_dynamic_colors")
////        }
////    }
//
//}
