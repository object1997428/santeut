package com.santeut.ui.mypage

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.outlined.Healing
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.MyProfileResponse

@Composable
fun MyProfileScreen(userViewModel: UserViewModel = hiltViewModel()) {
    val myProfile by userViewModel.myProfile.observeAsState()

    LaunchedEffect(Unit) {
        userViewModel.getMyProfile()
    }

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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 첫번째 Column: 원형 프로필 사진
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        ) {
            AsyncImage(
                model = myProfile?.userProfile ?: R.drawable.logo,
                contentDescription = "프로필 사진",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 두번째 Column: 닉네임
        Text(
            text = "${myProfile?.userNickname}",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 세번째 Column: 레벨과 포인트
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Lv. ${myProfile?.userTierName}",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
            Text(
                text = "포인트 ${myProfile?.userTierPoint}P",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProfileStats(myProfile: MyProfileResponse?) {
    StatCard(
        icon = Icons.AutoMirrored.Filled.DirectionsWalk,
        contentDescription = "걸음",
        statText = "총 ${myProfile?.userDistance ?: 0}m 걸었어요!"
    )
    StatCard(
        icon = Icons.Outlined.Timer,
        contentDescription = "시간",
        statText = "${myProfile?.userMoveTime ?: 0}분 동안 걸었어요!"
    )
    StatCard(
        icon = Icons.Outlined.Healing,
        contentDescription = "등산",
        statText = "지금까지 등반을 ${myProfile?.userHikingCount ?: 0}번 완료했어요!"
    )
    StatCard(
        icon = Icons.Filled.Hiking,
        contentDescription = "산 정복",
        statText = "${myProfile?.userHikingMountain ?: 0}개의 산을 정복했어요!"
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
