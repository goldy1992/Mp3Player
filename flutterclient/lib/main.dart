// Copyright 2018 The Flutter team. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:async';

import 'package:flutterclient/AppProperties.dart';
import 'package:flutterclient/MethodChannels.dart';
import 'package:flutterclient/RootItem.dart';
import 'dart:convert';


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


//Future<void> methodCall(MethodCall call) async {
//  print("called:" + call.method);
//
//  switch(call.method) {
//    case "onConnected":
//      {
//        print("onConnected called");
//        connection.setState(ConnectionState.CONNECTED);
//        makeNativeCall("SUBSCRIBE_ROOT");
//      }
//      break;
//    case "onReceiveTabNames":
//      {
//        try {
//          List<String> tabs = List<String>.from(call.arguments);
//
//          onReceiveTabNames(tabs);
//        } catch(ex) {
//          print("Exception: $ex");
//        }
//      }
//      break;
//  }
//}

void onReceiveTabNames(List<String> tabN) async {
  myApp.__myAppState.setTabs(tabN);
}


class MyApp extends StatefulWidget {



  _MyAppState __myAppState = _MyAppState(requestChannel);

  MyApp({Key key}) : super(key: key);

  @override
  _MyAppState createState() => __myAppState;
}


class _MyAppState extends State<MyApp> implements ConnectionSubscriber, MediaSubscriber {

  RequestChannel requestChannel;

  _MyAppState(RequestChannel requestChannel) {
    this.requestChannel = requestChannel;
  }

  Map<String, StatefulWidget> tabs = HashMap();


  @override
  void initState() {
    super.initState();
    requestChannel.connect(this);
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

  @override
  void onChildrenLoaded(String id, String children) {

    RootItem rootItem = RootItem.fromJson(json.decode(children))
    // TODO: implement onChildrenLoaded
  }


  @override
  void onConnected(String rootId) {
    subscriptionChannel.subscribe(this, rootId);
  }
}
