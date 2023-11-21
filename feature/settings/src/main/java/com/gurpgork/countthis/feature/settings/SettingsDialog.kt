package com.gurpgork.countthis.feature.settings

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.pm.PackageInfoCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurpgork.countthis.core.designsystem.theme.CtTheme
import com.gurpgork.countthis.core.designsystem.theme.supportsDynamicTheming
import com.gurpgork.countthis.core.model.data.DarkThemeConfig
import com.gurpgork.countthis.core.ui.TrackScreenViewEvent


@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
) {
    SettingsDialog(
        onDismiss = onDismiss,
        viewModel = hiltViewModel(),
    )
}

@Composable
internal fun SettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsViewState by viewModel.settingsViewState.collectAsStateWithLifecycle()
    SettingsDialog(
        onDismiss = onDismiss,
        settingsViewState = settingsViewState,
        onChangeButtonIncrementPreference = viewModel::updateButtonIncrementPreference,
        onChangeDynamicColorPreference = viewModel::updateDynamicColorPreference,
        onChangeDarkThemeConfig = viewModel::updateDarkThemeConfig,
        onChangeCrashAnalyticsPreference = viewModel::updateCrashAnalyticsPreference,
    )
}

@Composable
internal fun SettingsDialog(
    settingsViewState: SettingsViewState,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onDismiss: () -> Unit,
    onChangeButtonIncrementPreference: (useButtonIncrement: Boolean) -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onChangeCrashAnalyticsPreference: (enableCrashAnalytics: Boolean) -> Unit,
) {
    val configuration = LocalConfiguration.current

    /**
     * usePlatformDefaultWidth = false is use as a temporary fix to allow
     * height recalculation during recomposition. This, however, causes
     * Dialog's to occupy full width in Compact mode. Therefore max width
     * is configured below. This should be removed when there's fix to
     * https://issuetracker.google.com/issues/221643630
     */
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            HorizontalDivider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                when (settingsViewState) {
                    SettingsViewState.Loading -> {
                        Text(
                            text = stringResource(R.string.loading),
                            modifier = Modifier.padding(vertical = 16.dp),
                        )
                    }

                    is SettingsViewState.Success -> {
                        SettingsPanel(
                            settings = settingsViewState.settings,
                            supportDynamicColor = supportDynamicColor,
                            onChangeButtonIncrementPreference = onChangeButtonIncrementPreference,
                            onChangeDynamicColorPreference = onChangeDynamicColorPreference,
                            onChangeDarkThemeConfig = onChangeDarkThemeConfig,
                        )
                        HorizontalDivider(Modifier.padding(top = 8.dp))
                        LinksPanel(
                            settingsViewState.settings.useCrashAnalytics,
                            onChangeCrashAnalyticsPreference,
                        )
                    }
                }
            }
            TrackScreenViewEvent(screenName = "Settings")
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.dialog_dismiss),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}

@Composable
internal fun SettingsPanel(
    settings: UserEditableSettings,
    supportDynamicColor: Boolean,
    onChangeButtonIncrementPreference: (useButtonIncrement: Boolean) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
) {
    SettingsDialogSectionTitle(text = stringResource(R.string.settings_button_increment_title))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.settings_button_increment_yes),
            selected = settings.useButtonIncrement,
            onClick = { onChangeButtonIncrementPreference(true) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.settings_button_increment_no),
            selected = !settings.useButtonIncrement,
            onClick = { onChangeButtonIncrementPreference(false) },
        )
    }
    SettingsDialogSectionTitle(text = stringResource(R.string.settings_theme_title))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.settings_theme_system_default),
            selected = settings.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
            onClick = { onChangeDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.settings_theme_light),
            selected = settings.darkThemeConfig == DarkThemeConfig.LIGHT,
            onClick = { onChangeDarkThemeConfig(DarkThemeConfig.LIGHT) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.settings_theme_dark),
            selected = settings.darkThemeConfig == DarkThemeConfig.DARK,
            onClick = { onChangeDarkThemeConfig(DarkThemeConfig.DARK) },
        )
    }
    if (supportDynamicColor) {
        SettingsDialogSectionTitle(text = stringResource(R.string.settings_dynamic_color_title))
        Column(Modifier.selectableGroup()) {
            SettingsDialogThemeChooserRow(
                text = stringResource(R.string.settings_dynamic_color_yes),
                selected = settings.useDynamicColor,
                onClick = { onChangeDynamicColorPreference(true) },
            )
            SettingsDialogThemeChooserRow(
                text = stringResource(R.string.settings_dynamic_color_no),
                selected = !settings.useDynamicColor,
                onClick = { onChangeDynamicColorPreference(false) },
            )
        }
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
private fun LinksPanel(
    crashAnalyticsEnabled: Boolean,
    onChangeCrashAnalyticsPreference: (enableCrashAnalytics: Boolean) -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 16.dp),
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                Checkbox(
                    checked = crashAnalyticsEnabled,
                    onCheckedChange = onChangeCrashAnalyticsPreference
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "Send Analytics (crashes/usage)"
                )
            }
            Row {
                TextLink(
                    text = stringResource(R.string.privacy_policy),
                    url = PRIVACY_POLICY_URL, // R.string.privacy_policy_url
                )
            }
            Row {
                Text(
                    text = stringResource(R.string.settings_app_version),
                )
                Spacer(Modifier.width(16.dp))


                val appInfo = getAppVersion(LocalContext.current)

                if (appInfo != null) {
                    Text(
                        text = stringResource(
                            R.string.settings_app_version_summary,
                            appInfo.versionName, appInfo.versionNumber
                        )
                    )
                }
            }
        }
    }
}

data class AppVersion(
    val versionName: String,
    val versionNumber: Long,
)

fun getAppVersion(
    context: Context,
): AppVersion? {
    return try {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }
        AppVersion(
            versionName = packageInfo.versionName,
            versionNumber = PackageInfoCompat.getLongVersionCode(packageInfo),
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
private fun TextLink(text: String, url: String) {
    val launchResourceIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val context = LocalContext.current
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                ContextCompat.startActivity(context, launchResourceIntent, null)
            },
    )
}

@Preview
@Composable
private fun PreviewSettingsDialog() {
    CtTheme {
        SettingsDialog(
            settingsViewState = SettingsViewState.Success(
                UserEditableSettings(
                    useButtonIncrement = false,
                    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    useDynamicColor = false,
                    useCrashAnalytics = true,
                ),
            ),
            onDismiss = {},
            onChangeButtonIncrementPreference = {},
            onChangeDynamicColorPreference = {},
            onChangeDarkThemeConfig = {},
            onChangeCrashAnalyticsPreference = {},
        )
    }
}

@Preview
@Composable
private fun PreviewSettingsDialogLoading() {
    CtTheme {
        SettingsDialog(
            settingsViewState = SettingsViewState.Loading,
            onDismiss = {},
            onChangeButtonIncrementPreference = {},
            onChangeDynamicColorPreference = {},
            onChangeDarkThemeConfig = {},
            onChangeCrashAnalyticsPreference = {},
        )
    }
}

/* ktlint-disable max-line-length */
private const val PRIVACY_POLICY_URL = "https://github.com/ydarBing/CountThis/privacypolicy"