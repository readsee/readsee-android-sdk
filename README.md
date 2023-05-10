# Readsee Android SDK



## Feature

*   [x] Create/initialize profile in readsee
*   [x] Tracking event
*   [x] Tracking profile
*   [x] Logout
*   [x] Save fcm token to readsee
*   [x] Receive push notification from readsee
*   [x] User can make their own implementation of FirebaseMessagingService, but they need to call TokenHandler.handle(token) to send firebase token to readsee server, and NotificationHandler.handle(remoteMessage) if user want the readsee sdk to handle the notification
*   [ ] Custom notification small icon
*   [ ] Custom notification builder (next step), can set the action for each notification
*   [ ] Custom notification channel created by user

## Note

*   Handling permission for POST\_NOTIFICATION handled by user app

Download
--------

```kotlin
implementation("io.readsee:readsee-sdk:0.1.0")
```

## Getting started

```kotlin
  // Init client & setup readsee api
val tracker = ReadseeClient.config(this, "API Key goes here")
    .createApi()

```
