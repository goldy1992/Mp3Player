// Copyright 2018 The Flutter team. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import 'package:flutterclient/AppProperties.dart';
import 'package:flutterclient/MethodChannels.dart';


MyApp myApp = MyApp();

Future<void> main() async {
  try {
    WidgetsFlutterBinding.ensureInitialized();

     runApp(myApp);
  }
  catch (ex)
  {
    print("error: $ex");
  }
}

Connection connection = new Connection();


Future<void> methodCall(MethodCall call) async {
  print("called:" + call.method);

  switch(call.method) {
    case "onConnected":
      {
        print("onConnected called");
        connection.setState(ConnectionState.CONNECTED);
        makeNativeCall("SUBSCRIBE_ROOT");
      }
      break;
    case "onReceiveTabNames":
      {
        try {
          List<String> tabs = List<String>.from(call.arguments);

          onReceiveTabNames(tabs);
        } catch(ex) {
          print("Exception: $ex");
        }
      }
      break;
  }
}

void onReceiveTabNames(List<String> tabN) async {
  myApp.__myAppState.setTabs(tabN);
}



void makeNativeCall(String methodName) async {
  platform.invokeMethod(methodName);
}

class MyApp extends StatefulWidget {

  _MyAppState __myAppState = _MyAppState();

  MyApp({Key key}) : super(key: key);

  @override
  _MyAppState createState() => __myAppState;
}

enum ConnectionState {
  NOT_CONNECTED,
  CONNECTED,
  SUSPENDED
}

class Connection {
  var connectionState = ConnectionState.NOT_CONNECTED;

  ConnectionState getState() {
    return connectionState;
  }

  void setState(ConnectionState connectionState) {
    this.connectionState = connectionState;
  }
}

class _MyAppState extends State<MyApp> {

  List<String> tabs = List();

  @override
  void initState() {
    super.initState();
    platform.setMethodCallHandler(methodCall);
    //makeNativeCall("CONNECT");
  }

  void setTabs(List<String> tabs) {
    this.setState(() {this.tabs = tabs;});
  }

  int getTabLength() {
    return tabs.length <= 0 ? 1 : tabs.length;
  }

  List<Widget> getTabs() {
    if (tabs.length == 0) {
      return <Widget>[Tab(icon: CircularProgressIndicator())];
    }

    List<Widget> list = new List<Widget>();

    for(var i = 0; i < tabs.length; i++){
      Tab tab = Tab(text: tabs[i]);
      list.add(tab);
    }
    return list;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: DefaultTabController(
          length: getTabLength(),
          child: Scaffold(
            appBar: AppBar(
              title: Text('Waiting for tabs'),
              leading: new IconButton(icon: Icon(Icons.menu)),
              actions: <Widget>[
                new IconButton(icon: Icon(Icons.search), onPressed: null)
              ],
              bottom: TabBar(
                tabs: getTabs()
              ),
            ),
          )
      ),
    );
  }
}
