package com.gurpgork.countthis.core.designsystem.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gurpgork.countthis.core.designsystem.icon.CtIcons


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CtTopAppBar(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    appBarState: CtAppBarState,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = appBarState.title,
                maxLines = 1,
//                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee()
            )},
        navigationIcon = { appBarState.navigationIcon.invoke() },
        actions = { appBarState.actions.invoke(this) },
        colors = colors,
        modifier = modifier.testTag("CtTopAppBar"),
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CountThisAppBarPreview() {
    CtTopAppBar(
        appBarState = CtAppBarState(
            title = stringResource(id = android.R.string.untitled),
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = CtIcons.Add,
                        contentDescription = "Navigation Icon",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = CtIcons.Remove,
                        contentDescription = "Action Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = CtIcons.Remove,
                        contentDescription = "Action Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
        )
    )
}

