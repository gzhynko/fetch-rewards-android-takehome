package com.gzhynko.fetch_takehome.ui.screens

import androidx.compose.material3.DrawerState
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.gzhynko.fetch_takehome.model.HiringDataEntry
import com.gzhynko.fetch_takehome.ui.FetchTakehomeUiState
import com.gzhynko.fetch_takehome.ui.HiringDataListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun DrawerScreen(
    fetchTakehomeUiState: FetchTakehomeUiState,
    drawerState: DrawerState,
    listState: HiringDataListState,
    coroutineScope: CoroutineScope
) {
    when (fetchTakehomeUiState) {
        is FetchTakehomeUiState.Success -> ResultScreen(
            fetchTakehomeUiState.groupedData,
            drawerState,
            listState,
            coroutineScope
        )
        else -> {}
    }
}


@Composable
private fun ResultScreen(groupedData: SortedMap<Int, List<HiringDataEntry>>, drawerState: DrawerState, listState: HiringDataListState, coroutineScope: CoroutineScope) {
    groupedData.forEach { (listId, _) ->
        NavigationDrawerItem(
            label = { Text(text = "List ID $listId") },
            selected = false,
            onClick = {
                val listIdPosition = listState.listIdPositions[listId]
                if (listIdPosition != null) {
                    coroutineScope.launch {
                        listState.listState.scrollToItem(listIdPosition)
                        drawerState.close()
                    }
                }
            }
        )
    }
}
