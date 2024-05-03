package com.santeut.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santeut.designsystem.theme.SanteutTheme
import com.santeut.R

@Composable
fun HomeScreen(

) {
    Log.d("Home Screen", "Loading")
//    val viewModel = viewModel<HomeViewModel>()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        item {
            HomeTopBar(
                onClickAlert = {},
                onClickChatting = {}
            )
        }
        item {
            HomeSearchBar(
                onSearchTextChanged = {},
                onClickSearch = {},
                onClickMap = {}
            )
        }
        item {
            PopMountainCard()
        }
        item {
            MyGuildCard()
        }
        item {
            WeeklyHikingCard()
        }
        item {
            HomeCommunityCard()
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    SanteutTheme {
        HomeScreen()
    }
}

@Composable
fun HomeTopBar(
    onClickAlert: () -> Unit,
    onClickChatting: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(12.dp)
            .background(color = Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                contentScale = ContentScale.FillWidth,
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = "산뜻",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            imageVector = Icons.Default.AddAlert,
            contentDescription = "알림",
            modifier = Modifier
                .clickable { onClickAlert() }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Image(
            imageVector = Icons.Default.Comment,
            contentDescription = "채팅",
            modifier = Modifier
                .padding(10.dp)
                .clickable { onClickChatting() }
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun HomeSearchBar(
    onSearchTextChanged: (String) -> Unit,
    onClickSearch: () -> Unit,
    onClickMap: () -> Unit
) {
    val text = ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            textStyle = TextStyle(fontSize = 12.sp),
            value = "",
            onValueChange = { text ->
                onSearchTextChanged(text)
            },
            placeholder = {
                Text(
                    text = "어느 산을 찾으시나요?"
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done // 완료 액션 지정
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF678C40),
                unfocusedContainerColor = Color(0xFFFBF9ED),
                focusedBorderColor = Color(0xFF678C40),
                focusedContainerColor = Color(0xFFFBF9ED),
            ),
            shape = RoundedCornerShape(20.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = Color.Black,
                    modifier = Modifier.clickable { onClickSearch() }
                )
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            imageVector = Icons.Default.Map,
            contentDescription = "지도",
            modifier = Modifier.clickable { onClickMap() }
        )
        Spacer(modifier = Modifier.width(6.dp))
    }
}

@Composable
fun PopMountainCard(

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(224.dp)
            .padding(12.dp, 12.dp, 12.dp, 0.dp),
    ) {
        Text(
            text = "지금 인기있는 산?",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .height(40.dp),
        )
        LazyRow(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            items(4) { index ->
                MountainCard()
            }
        }
    }
}

@Composable
fun MountainCard(

) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(160.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 8.dp, 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(20.dp))
                    .clip(shape = RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "한라산",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "1111m 제주",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }
            Text(
                text = "4개 코스",
                fontSize = 12.sp,
                color = Color.LightGray,
                modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    }
}

@Composable
fun MyGuildCard(

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(204.dp)
            .padding(12.dp, 0.dp, 12.dp, 0.dp)
    ) {
        Row(
            modifier = Modifier
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "내 동호회",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "전체보기",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.LightGray
            )
        }
        LazyRow(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
        ) {
            items(3) { index ->
                HomeGuildItem(
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun HomeGuildItem(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 8.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(140.dp)
                .width(200.dp)
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(20.dp))
                .clip(shape = RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "싸피 산악회",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun WeeklyHikingCard(

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(12.dp, 12.dp, 12.dp, 0.dp)
    ) {
        Row(
            modifier = Modifier
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "주간 등산 일정",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "전체보기",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.LightGray
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(24.dp))
                .clip(shape = RoundedCornerShape(24.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFe5dd90))
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Row {
                        Image(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "달력"
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "2024년 5월 5일",
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "한라산 싸피 산악회",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(24.dp, 0.dp, 0.dp, 0.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeCommunityCard(

) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(Color.LightGray, shape = RectangleShape)
    ) {
        Image(
            painter = painterResource(R.drawable.home_community_background),
            contentDescription = "home_community_background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
        )
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = "커뮤니티",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
            ) {
                items(4) { index ->
                    CommunityItem()
                }
            }
        }
    }
}

@Composable
fun CommunityItem(

) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(140.dp)
            .padding(0.dp, 0.dp, 8.dp, 0.dp)
            .background(color = Color.White)
            .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .height(160.dp)
        ) {
            Text(
                text = "산악회 회원 모집",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "다른 사람들과 함께 산행을 즐겨보세요, 다른 사람들과 함께 산행을 즐겨보세요, 다른 사람들과 함께 산행을 즐겨보세요, 다른 사람들과 함께 산행을 즐겨보세요",
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}