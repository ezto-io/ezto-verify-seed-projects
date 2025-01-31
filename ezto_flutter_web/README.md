# Handling Vendor SDK Integration in Flutter Web

## Overview
When integrating a vendor SDK in a Flutter project that supports both mobile and web, you may encounter issues if the SDK does not support Flutter web. This guide provides a solution using **JavaScript interoperability** to ensure that the vendor SDK can be used on web.

## Approach
Since the vendor SDK does not support Flutter web, we use **Flutter's JS interop (`dart:js` or `dart:js_util`)** to communicate with the vendor's web SDK directly.

## Steps to Implement

### 1. Add the Vendor Web SDK to `index.html`
Modify `web/index.html` to include the vendor SDK and the required css files:

```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/ezto-io/web-push@latest/eztoverify.min.css">
<script src="https://cdn.socket.io/4.7.2/socket.io.min.js"></script>
<script src="https://cdn.jsdelivr.net/gh/ezto-io/web-push@latest/eztoverify.min.js" async></script>
```

This ensures that the eztoverify's JavaScript SDK and socket.io's js is available when running the Flutter web app.

---

### 2. Use `dart:js_util` and `JS()` to Call JavaScript Functions
Since Flutter does not natively support JavaScript SDKs, we use `dart:js_util` and `@JS()` annotation to interact with the vendor SDK.

#### Define JavaScript Interop
Create a new file to define jsInterop `ezto-interop.dart`:

```dart
import 'package:js/js.dart';

@JS('eztoverify')
@JS() // Specifies that we're binding to a global JavaScript object.
class eztoverify {
  external factory eztoverify(); // Constructor for the class.
  external void request(Object jsObj1, Object jsObj2, Function callback);
}
```

This code:
- Uses `@JS()` annotation to bind the `eztoverify` global object.

---

### 3. Call the Function in `main.dart`
Modify `login.dart` to call the web SDK when running on the web.

```dart
import 'dart:html' as html;
import 'vendor_sdk_web.dart';

void login() {
  void handleWebLogin() {
    var request = jsify({
      'request': {
        'user': {
          'metadata': {},
        },
      },
    });

    var transactionConfig = jsify({
      'api': '<your_transaction_url>',
      'apiVersion': "1",
    });
    // Instantiate the SDK class
    var sdk = eztoverify();
    sdk.request(request, transactionConfig, allowInterop(handleCallback));
  }
}
```
- Calls the `request()` function with two JavaScript objects and a callback function.
- Uses `jsify()` to convert Dart maps into JavaScript objects.
- Uses `allowInterop()` to allow Dart functions to be passed into JavaScript as callbacks.

---

## 4. Build and Run the Web Application
Use the following commands to build and test your Flutter web application:

```sh
flutter clean
flutter build web
flutter run -d chrome
```

---

## Summary
- The **vendor web SDK is loaded** via `index.html`.
- **JavaScript interop (`dart:js_util` and `@JS()`)** is used to communicate with the web SDK.

This method allows seamless integration of JavaScript-based SDKs into your Flutter web application.

---

### Need Help?
If you have any issues, feel free to reach out!

