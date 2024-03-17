package com.gzhynko.fetch_takehome.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gzhynko.fetch_takehome.R
import com.gzhynko.fetch_takehome.model.HiringDataEntry
import com.gzhynko.fetch_takehome.ui.FetchTakehomeUiState
import com.gzhynko.fetch_takehome.ui.HiringDataListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * Renders the contents of the drawer.
 *
 * @param fetchTakehomeUiState the UI state used to determine what's being rendered
 * @param drawerState the DrawerState of the app drawer used to interact with it
 * @param hiringDataListState the state of the hiring data list used to control scroll
 * @param coroutineScope the coroutine scope used to invoke drawer and list interactions
 */
@Composable
fun DrawerScreen(
    fetchTakehomeUiState: FetchTakehomeUiState,
    drawerState: DrawerState,
    hiringDataListState: HiringDataListState,
    coroutineScope: CoroutineScope
) {
    when (fetchTakehomeUiState) {
        // only show the result screen when data has been successfully retrieved
        is FetchTakehomeUiState.Success -> ResultScreen(
            fetchTakehomeUiState.groupedData,
            drawerState,
            hiringDataListState,
            coroutineScope
        )
        else -> {}
    }
}

/**
 * Displays drawer items for each listId in the data.
 *
 * Upon pressing a drawer item, the list is scrolled to the beginning of the corresponding listId.
 */
@Composable
private fun ResultScreen(groupedData: SortedMap<Int, List<HiringDataEntry>>, drawerState: DrawerState, hiringDataListState: HiringDataListState, coroutineScope: CoroutineScope) {
    Text(
        modifier = Modifier.padding(15.dp, 10.dp),
        text = stringResource(R.string.drawer_jump_to_label),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7F)
    )
    groupedData.forEach { (listId, _) ->
        NavigationDrawerItem(
            modifier = Modifier.padding(10.dp, 0.dp),
            label = { Text(text = "${stringResource(R.string.list_id_text)} $listId") },
            selected = false,
            onClick = {
                val listIdPosition = hiringDataListState.listIdPositions[listId]
                // make sure this listId is in the positions array (null value should normally never happen)
                if (listIdPosition != null) {
                    coroutineScope.launch {
                        hiringDataListState.listState.scrollToItem(listIdPosition)
                        drawerState.close()
                    }
                }
            }
        )
    }
}
