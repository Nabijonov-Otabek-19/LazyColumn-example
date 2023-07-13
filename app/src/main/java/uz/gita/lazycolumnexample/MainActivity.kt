package uz.gita.lazycolumnexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import kotlinx.coroutines.flow.distinctUntilChanged
import uz.gita.lazycolumnexample.ui.theme.LazyColumnExampleTheme
import uz.gita.lazycolumnexample.utils.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyColumnExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {

    val firstListState = rememberLazyListState()
    val secondListState = rememberLazyListState()

    val index = remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        LazyRow(state = firstListState) {
            items(firstList.size) {
                Card(modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        index.value = it
                    }) {
                    Text(text = firstList[it], modifier = Modifier.padding(8.dp), fontSize = 18.sp)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.dp)
                .weight(1f),
            state = secondListState
        ) {
            items(secondList) { item ->
                Card(modifier = Modifier.padding(8.dp)) {
                    Text(text = item, modifier = Modifier.padding(8.dp), fontSize = 18.sp)
                }
            }
        }
    }


    LaunchedEffect(index.value) {
        secondListState.animateScrollToItem(index = index.value)
    }

// Sync the scrolling of the two lists
    LaunchedEffect(secondListState) {
        snapshotFlow { secondListState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                firstListState.animateScrollToItem(index)
            }
    }
}