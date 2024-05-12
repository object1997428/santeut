import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.santeut.ui.community.PostViewModel
import com.santeut.ui.navigation.top.CreateTopBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreatePostScreen(
    navController: NavController,
    postViewModel: PostViewModel,
    postType: Char
) {
    val focusManager = LocalFocusManager.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val postCreationSuccess by postViewModel.postCreationSuccess.observeAsState()

    Scaffold() {
        CreateTopBar(navController, "글쓰기",
            onWriteClick = {
                postViewModel.createPost(title, content, postType, 1)
            })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp)  // 탑바에 의해 제목이 가려지는 것을 방지
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("제목") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, Color(0XFF678C40)),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) })
            )
            TextField(
                value = content,
                onValueChange = { content = it },
                placeholder = { Text("내용") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.White)
                    .border(1.dp, Color(0XFF678C40)),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }
    }

    LaunchedEffect(postCreationSuccess) {
        if (postCreationSuccess == true) {
            // 확인용 로그 추가
            Log.d("CreatePostScreen", "Navigating to postTips ")
            navController.navigate("postTips")
        }
    }
}
