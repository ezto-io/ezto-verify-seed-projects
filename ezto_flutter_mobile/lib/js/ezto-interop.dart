
import 'dart:developer';
import 'package:js/js.dart';

@JS() // Specifies that we're binding to a global JavaScript object.
class eztoverify {
  external factory eztoverify(); // Constructor for the class.
  external void request(Object jsObj1, Object jsObj2, Function callback);
}

void handleCallback(dynamic response) {
  log('Callback response: $response');
}