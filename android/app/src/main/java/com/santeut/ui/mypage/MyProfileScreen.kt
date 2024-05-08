package com.santeut.ui.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Healing
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.santeut.R

@Composable
fun MyProfileScreen(userViewModel: UserViewModel = hiltViewModel()) {

    val myProfile by userViewModel.myProfile.observeAsState()

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Row {
                AsyncImage(
                    model = (myProfile?.userProfile ?: R.drawable.logo),
                    contentDescription = "프로필 사진"
                )
                Column {
                    Text(text = ("Lv. " + myProfile?.userNickname))
                    Text(text = ("포인트 " + myProfile?.userTierPoint + "P"))
                }
            }
        }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = 5.dp,
        ) {
            Row {
                Icon(
                    imageVector = Icons.Filled.DirectionsWalk,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(24.dp)
                )
                Text(text = "총 " + myProfile?.userDistance + "m 걸었어요!")
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = 5.dp,
        ) {
            Row {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(24.dp)
                )
                Text(text = "" + myProfile?.userMoveTime + "분 동안 걸었어요!")
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = 5.dp,
        ) {
            Row {
                Icon(
                    imageVector = Icons.Outlined.Healing,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(24.dp)
                )
                Text(text = ("지금까지 등반을 " + myProfile?.userHikingCount + "번 완료했어요!"))
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = 5.dp,
        ) {
            Row {
                Icon(
                    imageVector = Icons.Filled.Hiking,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(24.dp)
                )
                Text(text = ("" + myProfile?.userHikingMountain + "개의 산을 정복했어요!"))
            }
        }

    }

}