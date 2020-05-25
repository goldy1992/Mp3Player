// Copyright 2018 The Flutter team. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:collection';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutterclient/MethodChannels.dart';
import 'package:flutterclient/medialist.dart';
import 'package:flutterclient/rootitem.dart';
import 'package:provider/provider.dart';
import 'package:provider/single_child_widget.dart';

MyApp myApp = MyApp();

Future<void> main() async {
  try {
    WidgetsFlutterBinding.ensureInitialized();
    await loadFakeMethodChannels();

    runApp(myApp);
  }
  catch (ex)
  {
    print("error: $ex");
  }
}

class MyApp extends StatefulWidget {

  _MyAppState __myAppState = _MyAppState(requestChannel);

  MyApp({Key key}) : super(key: key);

  @override
  _MyAppState createState() => __myAppState;
}


class _MyAppState extends State<MyApp> implements ConnectionSubscriber, MediaSubscriber {

  RequestChannel requestChannel;

  final List<SingleChildWidget> _providers = List();

  _MyAppState(RequestChannel requestChannel) {
    this.requestChannel = requestChannel;
  }

  Map<RootItem, Widget> tabs = HashMap();

  @override
  void initState() {
    super.initState();
    requestChannel.connect(this);
  }

  void setTabs(Map<RootItem, Widget> tabs) {
    this.setState(() {this.tabs = tabs;});
  }

  int getTabLength() {
    return tabs.length <= 0 ? 1 : tabs.length;
  }

  List<Widget> getTabs() {
    print("current tab name: ${tabs.keys}");
    if (tabs.keys.length == 0) {
      return <Widget>[Tab(icon: CircularProgressIndicator())];
    }

    List<Widget> list = new List<Widget>();

    Iterator<RootItem> tabNames = tabs.keys.iterator;

    while(tabNames.moveNext()) {
      list.add(Tab(text: tabNames.current.title));
  }
    return list;
  }

  List<Widget> getTabChildren () {
    print("getting tab children");
    if (tabs.values.length == 0) {
      return <Widget>[Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Center(
            child: Container(
              height: 40,
              width: 20,
              margin: EdgeInsets.all(5),
              child: CircularProgressIndicator(
                strokeWidth: 2.0,
                valueColor: AlwaysStoppedAnimation(Colors.white),
              ),
            ),
          ),
        ],
      )
      ];
    }
    return tabs.values.toList();
  }

  @override
  Widget build(BuildContext context) {
    return _providers.isEmpty ?
        buildMaterialApp()
        : MultiProvider(
        providers: _providers,
      child: buildMaterialApp()
    );
  }

  MaterialApp buildMaterialApp() {
    return MaterialApp(
      home: DefaultTabController(
        length: getTabLength(),
        child: Scaffold(
          appBar: AppBar(
            title: Text('MP3 Player'),
            leading: new IconButton(icon: Icon(Icons.menu)),
                actions: <Widget>[
                  new IconButton(icon: Icon(Icons.search), onPressed: null)
                ],
                bottom: TabBar(
                    tabs: getTabs()
                ),
              ),
              body: TabBarView(
                  children: getTabChildren()
              )
          )
      ),
    );
  }

  @override
  void onChildrenLoaded(String id, String children) {
    print("id: $id\nchildren: $children");
    Map<String, dynamic> jsonCode = json.decode(children);
    List<RootItem> rootItems = List();
    for (dynamic d in jsonCode["rootItems"]) {
      RootItem rootItem = RootItem.fromJson(d);
      rootItems.add(rootItem);
    }

    Map<RootItem, Widget> map = new HashMap();
    for (RootItem r in rootItems) {
      print("title = ${r.title}");
      if ("Songs" == r.title) {
        setState(() {
          Songs songs = new Songs(requestChannel, r.id);
          SongList songList = new SongList(key: UniqueKey());
          _providers.add(ChangeNotifierProvider(create: (context) => songs));
          map.putIfAbsent(r, () =>  songList);
        });

      } else {
        map.putIfAbsent(r, () =>  Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Center(
              child: Container(
                height: 50,
                width: 50,
                margin: EdgeInsets.all(5),
                child: CircularProgressIndicator(
                  strokeWidth: 3.0,
                  valueColor: AlwaysStoppedAnimation(Theme
                      .of(context)
                      .primaryColor),
                ),
              ),
            )
          ]
        ));
      }
    }
    print("map: $map");
    setTabs(map);
  }


  @override
  void onConnected(String rootId) {
    print("subscribing to root id: $rootId");
    requestChannel.subscribe(this, rootId);
  }

}
