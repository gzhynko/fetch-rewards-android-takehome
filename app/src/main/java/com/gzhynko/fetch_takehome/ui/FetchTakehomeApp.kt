package com.gzhynko.fetch_takehome.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gzhynko.fetch_takehome.R
import com.gzhynko.fetch_takehome.ui.screens.DrawerScreen
import com.gzhynko.fetch_takehome.ui.screens.HomeScreen
import kotlinx.coroutines.launch

/**
 * Stores data related to the hiring data list.
 *
 * @param listState the native LazyListState used to control scroll position of the hiring data list
 * @param listIdPositions the map of listIds and their corresponding positions within the hiring data list
 */
data class HiringDataListState (val listState: LazyListState, val listIdPositions: HashMap<Int, Int>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FetchTakehomeApp() {
    // init the app view model
    val fetchTakehomeViewModel: FetchTakehomeViewModel = viewModel()

    // init states and the app coroutine scope used by drawer-homescreen interactions
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val hiringDataListState = HiringDataListState(rememberLazyListState(), HashMap())
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerScreen(
                    fetchTakehomeViewModel.fetchTakehomeUiState,
                    drawerState,
                    hiringDataListState,
                    coroutineScope,
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(stringResource(R.string.top_bar_title))
                    },
                    navigationIcon = {
                        // drawer toggle button
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Open drawer")
                        }
                    },
                    actions = {
                        // refresh button
                        IconButton(onClick = { fetchTakehomeViewModel.getHiringData() }) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Refresh the list contents"
                            )
                        }
                    },
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                HomeScreen(
                    fetchTakehomeViewModel.fetchTakehomeUiState,
                    hiringDataListState
                )
            }
        }
    }
}
