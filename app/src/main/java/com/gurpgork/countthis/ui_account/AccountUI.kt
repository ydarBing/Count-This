package com.gurpgork.countthis.ui_account

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Surface
//import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gurpgork.countthis.R

@Composable
fun AccountUI(
    openSettings: () -> Unit,
) {
    AccountUI(
        viewModel = hiltViewModel(),
        openSettings = openSettings,
    )
}

@Composable
internal fun AccountUI(
    viewModel: AccountUIViewModel,
    openSettings: () -> Unit,
) {
//    val viewState by rememberStateWithLifecycle(viewModel.state)

//    val loginLauncher = rememberLauncherForActivityResult(
//        viewModel.buildLoginActivityResult()
//    ) { result ->
//        if (result != null) {
//            viewModel.onLoginResult(result)
//        }
//    }

    AccountUI(
//        viewState = viewState,
        openSettings = openSettings,
//        login = { loginLauncher.launch(Unit) },
//        logout = { viewModel.logout() }
    )
}

@Composable
internal fun AccountUi(
//    viewState: AccountUIViewState,
    openSettings: () -> Unit,
//    login: () -> Unit,
//    logout: () -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        // FIXME: Force the dialog to wrap the content. Need to work out why
        // this doesn't work automatically
        modifier = Modifier.heightIn(min = 200.dp),
    ) {
        Column {
            AppAction(
                label = stringResource(R.string.settings_title),
                icon = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings_title),
                onClick = openSettings,
            )
        }
    }
}

@Composable
private fun AppAction(
    label: String,
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 48.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        Image(
            imageVector = icon,
            contentDescription = contentDescription,
//            colorFilter = ColorFilter.tint(foregroundColor())
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}