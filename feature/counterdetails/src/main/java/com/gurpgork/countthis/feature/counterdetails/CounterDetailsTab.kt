package com.gurpgork.countthis.feature.counterdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import com.gurpgork.countthis.core.designsystem.component.Layout
import com.gurpgork.countthis.core.model.data.Increment
import com.gurpgork.countthis.core.ui.LocalCountThisDateFormatter


@Composable
fun DetailsTab(increments: List<Increment>) {
// TODO group increments by date
//  val groups = increments.groupBy { (it.date.toLocalDate() ) }
//CollapsableLazyColumn(sections = listOf(
//    CollapsableSection()
//)
//)
//    if(!increments.isEmpty())
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = LocalScaffoldPadding.current,
//        reverseLayout = 
    ){
        items(increments.reversed()){ item ->
            IncrementRow(
                increment = item,
                modifier = Modifier.fillParentMaxWidth()
                // TODO ability to delete or change location of increment from here
//                    .clickable { onIncrementClick(item.id) }
            )
            Divider()
        }
    }
}

@Composable
fun IncrementRow(increment: Increment, modifier: Modifier) {
    Row(
        modifier = modifier
            .heightIn(min = 48.dp)
            .wrapContentHeight(Alignment.CenterVertically)
            .padding(horizontal = Layout.bodyMargin, vertical = Layout.gutter),
    ){
        Column(modifier = Modifier.weight(1f)) {
            // TODO could put date with multiple increments here
            //  then add another text indented with each time of that date
            val date = LocalCountThisDateFormatter.current
                .formatMediumDateTime(increment.incrementDate)
            Text(text = date,
                style = MaterialTheme.typography.bodyMedium)
        }

        Text(text = increment.increment.toString(),
            style = MaterialTheme.typography.bodyMedium)
    }
}


@Composable
fun Header(increment: Increment, modifier: Modifier) {


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CounterDetailsAppBar(
    backgroundColor: Color,
    navigateUp: () -> Unit,
    elevation: Dp,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cd_close),
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(),
        modifier = modifier
    )
}


