package com.gurpgork.countthis.feature.counterdetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import com.gurpgork.countthis.core.designsystem.component.Layout
import com.gurpgork.countthis.core.ui.LocalCountThisDateFormatter
import com.gurpgork.countthis.core.model.data.Increment


@Composable
internal fun DetailsTab(increments: List<Increment>) {
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
                .formatMediumDateTime(increment.date)
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


