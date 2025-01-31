import 'dart:developer';

import 'package:eztoverify/controller/ezto_verify.dart';
import 'package:eztoverify/enums/push_support.dart';
import 'package:eztoverify/ezto_verify_app.dart';
import 'package:eztoverify/model/force_update_model.dart';
import 'package:eztoverify/model/get_token_model.dart';
import 'package:flutter/material.dart';
import 'package:hive_flutter/hive_flutter.dart';

import 'views/login.dart';

void main() async {
  await Hive.initFlutter();
  runApp()
  runApp(
      EztoVerifyApp(
        forceUpdate: const ForceUpdate(
            android: UpgradeConfig(shouldForceUpdate: true),
            ios: UpgradeConfig(shouldForceUpdate: true)
          // App force update need call this
        ),
        onPermissionDenied: (deniedPermission) async {
          // Handle permission denial
          return true;
        },
        onResult: (String reqId, String authReqId) async {
          // Handle successful authentication result
          log("Got result from transaction $authReqId");
        },
        onPushTokenRequest: () async {
          return GetTokenModel(
            // Provide FCM token
            pushToken: "token",
            pushType: PushSupport.firebase,
          );
        },
        onError: (reason) {
          // Handle SDK error
        },
        onClose: (reason) {
          // Handle transaction closure
        },
        onTransactionRequest: (data, isFromDeeplink) async {
          // For deeplinks
          EztoVerify(
            // encryptionKey: key,
          ).onPushReceived(
              data: data);
        },
        child: MaterialApp(
          debugShowCheckedModeBanner: false,
          home: MyLogin(),
          routes: {
            'login': (context) => MyLogin(),
          },
        ),
      )
  );
}
