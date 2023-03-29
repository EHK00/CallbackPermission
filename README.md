# Callback permission

android runtime permission 요청을 간단한 콜백 구조로 제공합니다.

자세한 사용법은 sample을 참고해주세요.

## 사용 방법

### 기본 설정

Activity 또는 Fragment class 내에서 CallbackPermission 인스턴스를 생성합니다.

CallbackPermission은 내부에서 ActivityResultLauncher를 사용하기 떄문에 onCreate 이전에 인스턴스 생성이 이뤄져야합니다.

```kotlin
class TestActivity : AppCompatActivity() {
    private val callbackPermission: CallbackPermission = callbackPermission()
}
```

### 권한 요청

`fun requestPermission(vararg permissions: String, callback: PermissionCheckCallBack)`을 호출하여 Runtime
permission을 요청합니다.

callback으로 허용되지 않은 권한 목록을 받습니다.

```kotlin
callbackPermission.requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION) { deniedPermissions ->
    Snackbar.make(binding.root, deniedPermissions.toString(), Snackbar.LENGTH_LONG).show()
}
```

### 권한 허용 여부 체크

`fun hasPermissions(vararg permissions: String): Boolean`을 호출하여 varargs로 주어진 권한 모두가 허용됐는지 체크합니다.

```kotlin
val isPermitted = callbackPermission.hasPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
```

## TBD

library를 mavencentral로 배포 