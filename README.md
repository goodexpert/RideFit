# RideFit

> 버튼 하나로 탑승 안내를 시작하세요. 기사님이 작동하면 승객에게 음성으로 안내합니다.

택시 기사님이 버튼 하나만 누르면 TTS 음성이 승객에게 자동으로 서비스를 안내합니다.  
서비스 품질을 일정하게 유지하고, 별점 참여율을 높이며, 계좌 안내 시 말실수를 없앱니다.

---

## 주요 기능

- **탑승 안내 시작** — 버튼 하나로 승객 맞이 음성 안내 재생
- **드라이브 모드** — 조용히 / 빠른 이동 / 안전 운행 / 미디어 4가지 모드, 모드별 환영 멘트 TTS 재생
- **계좌 이체 안내** — 저장된 계좌 정보를 숫자 하나씩 또박또박 TTS로 읽어줌
- **운행 완료** — 별점 참여 유도 음성 안내
- **긴급 상황 녹화** — 길게 눌러 영상 녹화 시작 (포그라운드 서비스)
- **다크 모드** — 시스템 설정 기반 자동 전환 (야간 운행 최적화)
- **40–60대 기사님 최적화 UI** — 최소 80dp 버튼, 12sp 이상 폰트, WCAG AAA 대비율

---

## 시작하기

### 요구사항

- Android Studio Meerkat 이상
- JDK 17 이상
- `google-services.json` — Firebase 콘솔에서 다운로드 후 `app/` 에 배치

### 빌드

```bash
# 디버그 APK
./gradlew assembleDebug

# 릴리즈 APK
./gradlew assembleRelease

# 릴리즈 AAB (Google Play)
./gradlew bundleRelease
```

### 테스트

```bash
# 단위 테스트 + 스냅샷 검증
./gradlew testDebugUnitTest

# 스냅샷 재촬영
./gradlew testDebugUnitTest -Proborazzi.test.record=true

# 기기 연결 후 E2E 테스트
./gradlew connectedDebugAndroidTest

# 정적 분석
./gradlew detekt
```

---

## Fastlane

```bash
# 단위 테스트 + detekt
bundle exec fastlane android test

# Firebase App Distribution (QA)
bundle exec fastlane android distribute_debug

# Firebase App Distribution (베타)
bundle exec fastlane android distribute_beta

# Google Play internal 트랙 배포 (versionCode 자동 증가)
bundle exec fastlane android deploy_internal

# internal → production 10% 롤아웃
bundle exec fastlane android deploy_production
```

#### 필요한 환경 변수

| 변수 | 용도 |
|---|---|
| `FIREBASE_SERVICE_ACCOUNT_JSON_PATH` | Firebase 서비스 계정 JSON 경로 |
| `GOOGLE_PLAY_JSON_KEY_PATH` | Google Play API 키 JSON 경로 |
| `FIREBASE_TESTER_GROUPS` | 배포 대상 그룹 (기본값: `qa-team` / `beta-testers`) |

---

## 디자인 원칙

40–60대 택시 기사님을 위해 **가독성과 터치 정확도**를 최우선으로 설계했습니다.

| 항목 | 기준 |
|---|---|
| 최소 버튼 높이 | 80dp |
| 드라이브 모드 카드 | 88dp 이상 |
| 최소 폰트 크기 | 12sp |
| 낮 모드 대비율 | 14.1:1 (WCAG AAA) |
| 밤 모드 대비율 | 16.8:1 (WCAG AAA) |
| 긴급 버튼 | 길게 누르기 500ms (오작동 방지) |

---

## 라이선스

Copyright © 2025 Seongwuk Park. All rights reserved.
