# 04/26(목)
## android studio
1. 기본 test app 구성
    - @Composable : 뷰의 개념으로 실제 screen에 보여지는 부분을 지정한다.
    - @Preview : 빌드되기 전 간이적으로 보여주는 창(미리보기를 위해 정의 되어 있는 screen창을 지정한다)
    - setContent : 컴파일 후 빌드 되어 보여지는 실제로 작동하는 영역

2. Compose의 구성(Column, Row, Box)
    - Column : 세로열의 구성으로 실제 보여지는 부분의 형태를 세로로 구성하고 보여준다.
    - Row : 행의 구성으로 실제 보여지는 부분의 형태를 가로로 구성하고 보여준다.
    - Box : Layout 상 여러 위제를 겹처서 놓을 수 있는 구성이다. 
3. modifier
    - Compose UI 요소에 동작을 장식하거나 추가하는 순서 있고 변경할 수 없는 컬렉션
    - EX)padding, backgroundcolor....
4. dp, sp
    - dp : (Density-Independent Pixels)는 UI 레이아웃을 정의할 때 레이아웃 치수나 위치를 지정하기 위해 사용하는 단위이다. 픽셀 독립 단위이며 화면의 크기가 달라도 동일한 비율로 보여주기 위해 안드로이드에서 정의한 단위이며 큰 화면, 작은 화면에 상관없이 같은 크기로 나타난다.
    - sp : (Scale-Independent Pixels)는 UI 레이아웃을 정의할 때 텍스트의 크기를 지정하기 위해 사용하는 단위이다.
    - 안드로이드 설정 화면에서 사용자는 안드로이스 시스템 전체에서 보여지는 텍스트의 크기를 선택하여 설정할 수 있는데 SP는 해당 설정에 영향을 받는다. 예를 들어, 시스템 설정에서 텍스트 크기를
"최대 크케"로 설정하게 되면 UI 레이아웃을 정의할 때 SP로 크기를 지정해놓은 TextView의 텍스트 크기가 영향을 받아 커진다. 반면에 TextView의 텍스트 크기를 DP로 설정하게 되면 시스템 설정의 텍스트 크기 값의 변화에 상관없이 일정한 크기를 유지한다.


# 04/27(금)
## 지도 구현하기

1. google map 
    - 호환성이 좋고 docs에 api 설명이 잘 되어 있다.
    - 우리가 필요한 산에 대한 길 안내가 잘 안되어 있어 사용하기 힘들거 같다.
    - ![Alt text](image.png)
    - metadata에 구글관련 api의 형식에 맞게 작성한 뒤 local.properties에 api키를 숨겨 보안을 강화할 수 있다.
   - app 단위 gradle에서 implementation의 버전을 잘 확인하고 호환되는 기종과 맞추는게 중요하다.

2. naver map
    - 초기 naver 의 지도는 compose의 형식을 지원하지 않았지만 현재는 라이브러리로 제공되고 있어 편리하게 사용할 수 있다.\
    - gradle에서 버전을 잘 확인하고 
    implementation ("io.github.fornewid:naver-map-location:21.0.1")
    implementation ("io.github.fornewid:naver-map-compose:1.7.0")
    implementation("com.naver.maps:map-sdk:3.18.0")
    settings.gradle에서 확인해야 한다.
    maven("https://naver.jfrog.io/artifactory/maven/")
    maven("https://repository.map.naver.com/archive/maven")

- 기본 코드에서 compose의 형태로 바꾸는 과정이 생각보다 쉽지 않아 계속 시도해 봐야 할거 같다.