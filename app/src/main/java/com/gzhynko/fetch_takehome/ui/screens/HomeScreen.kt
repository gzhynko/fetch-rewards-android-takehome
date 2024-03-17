package com.gzhynko.fetch_takehome.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gzhynko.fetch_takehome.R
import com.gzhynko.fetch_takehome.model.HiringDataEntry
import com.gzhynko.fetch_takehome.ui.FetchTakehomeUiState
import com.gzhynko.fetch_takehome.ui.HiringDataListState
import java.util.*

/**
 * Renders the Home screen.
 *
 * @param fetchTakehomeUiState the UI state used to determine what's being rendered
 * @param hiringDataListState the state of the hiring data list used to control scroll
 * @param modifier an optional base Modifier
 */
@Composable
fun HomeScreen(
    fetchTakehomeUiState: FetchTakehomeUiState,
    hiringDataListState: HiringDataListState,
    modifier: Modifier = Modifier,
) {
    when (fetchTakehomeUiState) {
        is FetchTakehomeUiState.LoadingData -> LoadingScreen(modifier = modifier.fillMaxSize())
        is FetchTakehomeUiState.Success -> ResultScreen(
            fetchTakehomeUiState.groupedData,
            hiringDataListState,
            modifier.fillMaxWidth()
        )
        is FetchTakehomeUiState.Error -> ErrorScreen( modifier = modifier.fillMaxSize())
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
private fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

/**
 * Renders the retrieved hiring data in a list with listId headers.
 */
@OptIn(ExperimentalFoundationApi::class) // sticky header requires the experimental foundation API
@Composable
private fun ResultScreen(groupedData: SortedMap<Int, List<HiringDataEntry>>, hiringDataListState: HiringDataListState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxHeight().fillMaxWidth(),
            state = hiringDataListState.listState
        ) {
            // store the current list position to then store positions of the listId headers
            // this will be used to scroll the list to the listId chosen in the app drawer
            var listPosition = 0

            groupedData.forEach { (listId, data) ->
                stickyHeader { // add a fancy sticky header to inform user of the displayed items' listIds
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .fillMaxWidth()
                    ) {
                        Column (
                            modifier = Modifier.padding(25.dp, 0.dp)
                        ) {
                            Text(
                                text = "${stringResource(R.string.list_id_text)} $listId",
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Divider()
                    }
                }
                hiringDataListState.listIdPositions[listId] = listPosition
                listPosition++ // header counts as list item

                items(items = data, itemContent = {item ->
                    Column(
                        modifier = Modifier.padding(15.dp, 10.dp)
                    ) {
                        Text((item.name ?: "null"))
                    }
                })
                listPosition += data.size // increment by the number of list items we just added
            }
        }
    }
}
