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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gzhynko.fetch_takehome.ui.screens.DrawerScreen
import com.gzhynko.fetch_takehome.ui.screens.HomeScreen
import kotlinx.coroutines.launch

data class HiringDataListState (val listState: LazyListState, val listIdPositions: HashMap<Int, Int>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FetchTakehomeApp() {
    val fetchTakehomeViewModel: FetchTakehomeViewModel = viewModel()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val listState = HiringDataListState(rememberLazyListState(), HashMap())
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerScreen(
                    fetchTakehomeViewModel.fetchTakehomeUiState,
                    drawerState,
                    listState,
                    coroutineScope,
                )
            }
        }
    ) {
        // Screen content
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text("Hiring Data")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "")
                        }
                    },
                    actions = {
                        IconButton(onClick = { fetchTakehomeViewModel.getHiringData() }) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Localized description"
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
                    listState
                )
            }
        }
    }
}
