package uz.gita.lazycolumnexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import kotlinx.coroutines.flow.distinctUntilChanged
import uz.gita.lazycolumnexample.ui.theme.LazyColumnExampleTheme
import uz.gita.lazycolumnexample.utils.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyColumnExampleTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SyncTwoLazyList()
                }
            }
        }
    }
}

@Composable
fun SyncTwoLazyList() {

    val firstListState = rememberLazyListState()
    val secondListState = rememberLazyListState()

    var currentIndex by rememberSaveable { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        LazyRow(state = firstListState) {
            items(firstList.size) {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { currentIndex = it },
                    shape = RoundedCornerShape(16),
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentIndex == it) Color.Blue else Color.Red
                    )
                ) {
                    Text(
                        text = firstList[it],
                        modifier = Modifier.padding(8.dp),
                        fontSize = 18.sp,
                        color = if (currentIndex == it) Color.White else Color.Black
                    )
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
                    Text(
                        text = item,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }

    // when firstListItem clicked, secondList will be scrolled
    // to index which clicked
    LaunchedEffect(currentIndex) {
        secondListState.animateScrollToItem(index = currentIndex)
    }

// Sync the scrolling of the two lists
    LaunchedEffect(secondListState) {
        snapshotFlow { secondListState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                currentIndex = index
                firstListState.animateScrollToItem(index)
            }
    }
}