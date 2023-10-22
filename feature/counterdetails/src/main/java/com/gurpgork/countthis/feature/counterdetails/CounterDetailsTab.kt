package com.gurpgork.countthis.feature.counterdetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import com.gurpgork.countthis.core.designsystem.component.Layout
import com.gurpgork.countthis.core.model.data.Increment
import com.gurpgork.countthis.core.ui.LocalCountThisDateFormatter


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailsTab(
    increments: List<Increment>,
    selectedIds: Set<Long>,
    onRowLongClick: (Long) -> Unit,
    onRowClick: (Long) -> Unit,
) {
//    val selectedItems = remember { mutableStateListOf<Increment>() }
// TODO group increments by date
//  val groups = increments.groupBy { (it.date.toLocalDate() ) }
//CollapsableLazyColumn(sections = listOf(
//    CollapsableSection()
//)
//)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = LocalScaffoldPadding.current,
//        reverseLayout = 
    ) {
        items(increments.reversed()) { item ->
            IncrementRow(
                increment = item,
                modifier = Modifier
                    .fillParentMaxWidth()
                    .combinedClickable(
                        onLongClick = { onRowLongClick(item.id) },
                        onClick = { onRowClick(item.id) },
                    )
//                    .clickable {
//                        if (selectedItems.contains(item)) {
//                            selectedItems.remove(item)
//                        } else {
//                            selectedItems.add(item)
//                        }
//                    }
                    .background(
                        if (selectedIds.contains(item.id)) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    )
            )
            HorizontalDivider()
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
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // TODO could put date with multiple increments here
            //  then add another text indented with each time of that date
            val date = LocalCountThisDateFormatter.current
                .formatMediumDateTime(increment.incrementDate)
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = increment.increment.toString(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


