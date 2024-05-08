package com.santeut.ui.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.outlined.Healing
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.MyProfileResponse

@Composable
fun MyProfileScreen(userViewModel: UserViewModel = hiltViewModel()) {
    val myProfile by userViewModel.myProfile.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        ProfileHeader(myProfile)
        Text(text = "나의 등산 기록")
        ProfileStats(myProfile)
    }
}

@Composable
fun ProfileHeader(myProfile: MyProfileResponse?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = myProfile?.userProfile ?: R.drawable.logo,
            contentDescription = "프로필 사진",
            modifier = Modifier
                .size(100.dp)
                .padding(end = 16.dp)
        )
        Column {
            Text(
                text = "Lv. ${myProfile?.userNickname}",
                style = MaterialTheme.typography.h6
            )
            Text(
                text = "포인트 ${myProfile?.userTierPoint}P",
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun ProfileStats(myProfile: MyProfileResponse?) {
    StatCard(
        icon = Icons.Filled.DirectionsWalk,
        contentDescription = "걸음",
        statText = "총 ${myProfile?.userDistance}m 걸었어요!"
    )
    StatCard(
        icon = Icons.Outlined.Timer,
        contentDescription = "시간",
        statText = "${myProfile?.userMoveTime}분 동안 걸었어요!"
    )
    StatCard(
        icon = Icons.Outlined.Healing,
        contentDescription = "등산",
        statText = "지금까지 등반을 ${myProfile?.userHikingCount}번 완료했어요!"
    )
    StatCard(
        icon = Icons.Filled.Hiking,
        contentDescription = "산 정복",
        statText = "${myProfile?.userHikingMountain}개의 산을 정복했어요!"
    )
}

@Composable
fun StatCard(icon: ImageVector, contentDescription: String, statText: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 16.dp)
            )
            Text(
                text = statText,
                style = MaterialTheme.typography.body1
            )
        }
    }
}
