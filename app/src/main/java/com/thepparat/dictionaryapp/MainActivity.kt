package com.thepparat.dictionaryapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thepparat.dictionaryapp.feature_dictionary.presentation.BWebView
import com.thepparat.dictionaryapp.feature_dictionary.presentation.WordInfoItem
import com.thepparat.dictionaryapp.feature_dictionary.presentation.WordInfoViewModel
import com.thepparat.dictionaryapp.ui.theme.DictionaryAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryAppTheme {
                val viewModel: WordInfoViewModel = viewModel()
                val state = viewModel.state.value
                val scaffoldState = rememberScaffoldState()
                var url by remember {
                    mutableStateOf("")
                }

                LaunchedEffect(key1 = true) {
                    viewModel.eventFlow.collectLatest { event ->
                        when (event) {
                            is WordInfoViewModel.UIEvent.ShowSnackbar -> {

                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = event.message
                                )
                            }
                        }
                    }
                }
                Scaffold(
                    scaffoldState = scaffoldState
                ) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            TextField(
                                value = viewModel.searchQuery.value,
                                onValueChange = viewModel::onSearch,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text = "Search...")
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(state.wordInfoItems.size) { i ->
                                    val wordInfo = state.wordInfoItems[i]
                                    if(i > 0) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    WordInfoItem(wordInfo = wordInfo){
                                        url = it
                                    }
                                    if(i < state.wordInfoItems.size - 1) {
                                        Divider()
                                    }
                                }
                            }
                        }
                        if(state.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                        if (url.isNotEmpty()){
                            BWebView(url)
                        }
                    }
                }
            }
        }
    }
}