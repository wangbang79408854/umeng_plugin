import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:umeng_plugin/umeng_plugin.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  Future<void> initUmeng() async {
    var success = await UmengPlugin.init(androidAppKey: "123123123",logEnable: true);
    print("umeng init $success");
  }

  Future<void> loginEvent() async {
    await UmengPlugin.event("login");
  }

  Future<void> mapEvent() async {
    var mapData = new Map<String, String>();
    mapData["name"] = "testName";
    mapData["value"] = "testValue";
    print("序列化参数 $mapData");
    await UmengPlugin.eventMap("mapEvent", mapData);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              FlatButton(
                child: Text("initUmeng"),
                onPressed: initUmeng,
              ),
              FlatButton(
                child: Text("打点记录一个login事件"),
                onPressed: loginEvent,
              ),
              FlatButton(
                child: Text("打点记录一个Map事件"),
                onPressed: mapEvent,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
