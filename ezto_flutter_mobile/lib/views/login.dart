
import 'dart:convert';
import 'dart:js_util';
import 'dart:io';
import 'dart:typed_data';

import 'package:eztoverify/controller/ezto_verify.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:hive/hive.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:http/http.dart' as http;

import '../js/ezto-interop.dart';

class MyLogin extends StatefulWidget {
  const MyLogin({Key? key}) : super(key: key);

  @override
  _MyLoginState createState() => _MyLoginState();
}

class _MyLoginState extends State<MyLogin> {
  bool isChecked = false;
  TextEditingController email = TextEditingController();
  TextEditingController password = TextEditingController();


  late Box box1;

  @override
  void initState() {
    //
    super.initState();
    createBox();

  }

  Future<String> createTransaction() async {
    var url = Uri.https('getbit.eztovrfy.com', 'auth/realms/getbit/protocol/openid-connect/transaction');

    http.Response response = await http.post(url, headers: {
      HttpHeaders.authorizationHeader: "Basic MzhkOTdjYTItOWMyYS00ZjdmLTllZTQtZmE4MWE2ZWQ0OTUzOkZFWDBSVlBycldCalM1UUNIclRmRmpTQmRiYkpBejlGaFNLa1Rha1RIRDhjZFZSZk9CUzBRMENzdWdjTnFIWko=",
      "api-version": "1",
      HttpHeaders.contentTypeHeader: "application/x-www-form-urlencoded"
    });
    Map value = jsonDecode(response.body);
    String qrBase64 = value["qr"];
    return qrBase64.split(" ")[1];
  }

  void createBox() async {
    box1 = await Hive.openBox('logininfo');
    getdata();
  }
  void getdata()async {
    if (box1.get('email') != null) {
      email.text = box1.get('email');
      isChecked = true;
      setState(() {

      });
    }
    if (box1.get('password') != null) {
      password.text = box1.get('password');
      isChecked = true;
      setState(() {

      });
    }
  }


  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(
        image: DecorationImage(
            image: AssetImage('assets/login.png'), fit: BoxFit.cover),
      ),
      child: Scaffold(
        backgroundColor: Colors.transparent,
        body: Stack(
          children: [
            Container(),
            Container(
              padding: EdgeInsets.only(left: 35, top: 130),
              child: Text(
                'Ezto - Flutter\nIntegration',
                style: TextStyle(color: Colors.white, fontSize: 33),
              ),
            ),
            SingleChildScrollView(
              child: Container(
                padding: EdgeInsets.only(
                    top: MediaQuery
                        .of(context)
                        .size
                        .height * 0.5),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Container(
                      margin: EdgeInsets.only(left: 35, right: 35),
                      child: Column(
                        children: [
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Text(
                                'Sign in',
                                style: TextStyle(
                                    fontSize: 27, fontWeight: FontWeight.w700),
                              ),
                              CircleAvatar(
                                radius: 30,
                                backgroundColor: Color(0xff4c505b),
                                child: IconButton(
                                    color: Colors.white,
                                    onPressed: () {
                                      login();
                                    },
                                    icon: Icon(
                                      Icons.arrow_forward,
                                    )),
                              )
                            ],
                          ),
                          SizedBox(
                            height: 40,
                          ),
                        ],
                      ),
                    )
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  void handleMobileLogin() async {
    String qrString = await createTransaction();
    Uint8List qrBytes = base64Decode(qrString);
    EztoVerify().qrFromBytes(bytes: qrBytes);
  }

  void handleWebLogin() {
    var request = jsify({
      'request': {
        'user': {
          'metadata': {},
        },
      },
    });

    var transactionConfig = jsify({
      'api': 'https://getbit.eztovrfy.com/auth/realms/api/ezto_web_push/bcb652f0-17da-4eca-8cda-05baee1c7406/getbit/register',
      'apiVersion': "1",
    });
    // Instantiate the SDK class
    var sdk = eztoverify();
    sdk.request(request, transactionConfig, handleCallback);
  }

  void login() async {
    if (kIsWeb) {
      handleWebLogin();
    } else {
      handleMobileLogin();
    }
  }
}