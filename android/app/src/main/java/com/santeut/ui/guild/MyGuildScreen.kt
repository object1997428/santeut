package com.santeut.ui.guild

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyGuildScreen(
    navController: NavController
) {
    val pages = listOf("동호회", "소모임")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold() {
        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                }
            ) {
                pages.forEachIndexed { index, title ->
                    Tab(
                        text = { androidx.compose.material.Text(text = title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                count = pages.size,
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> MyGuildListScreen()
                    1 -> PartyListScreen()
                    else -> androidx.compose.material.Text("Unknown page")
                }
            }
        }
    }
}