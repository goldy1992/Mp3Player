import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutterclient/MethodChannels.dart';
import 'package:flutterclient/metadata.dart';
import 'dart:convert';
import 'package:intl/intl.dart';
import 'package:flutter/material.dart';
import 'package:flutter_absolute_path/flutter_absolute_path.dart';
import 'package:flutterclient/songs.dart';
import 'package:provider/provider.dart';

class SongList extends StatelessWidget {
  SongList({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ListView(
        padding: const EdgeInsets.all(8), children: getChildren(context));
  }

  List<Widget> getChildren(BuildContext context) {
    List<Widget> toReturn = List();
    List<Song> songs = Provider.of<Songs>(context).songs;
    if (songs.length <= 0) {
      toReturn.add(Flexible(
          child: Container(
        height: 50,
        color: Colors.amber[600],
        child: Center(child: Text('Entry: ')),
      )));
    } else {
      for (Song m in songs) {
        toReturn.add(Container(
            padding: EdgeInsets.all(3),
            child: Row(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.center,
                mainAxisSize: MainAxisSize.max,
                children: [
                  Column(children: <Widget>[
                    FutureBuilder<Image>(
                      future: _getMockImage(),
                      builder: (BuildContext context,
                          AsyncSnapshot<Image> snapshot) {
                        if (snapshot.hasData) {
                          return Center(child: snapshot.data);
                        } else
                          return Center();
                      },
                    ),
                  ]),
                  Expanded(
                      child: Container(
                          padding: EdgeInsets.only(left: 3, right: 3),
                          child: Column(children: <Widget>[
                            Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              children: <Widget>[
                                Container(
                                    padding: EdgeInsets.all(2),
                                    child: Text(
                                      '${m.title}',
                                      textAlign: TextAlign.left,
                                      style:
                                          Theme.of(context).textTheme.bodyText1,
                                    ))
                              ],
                            ),
                            Row(
                                mainAxisAlignment: MainAxisAlignment.start,
                                children: <Widget>[
                                  Container(
                                      padding: EdgeInsets.all(2),
                                      child: Text(
                                        '${m.artist}',
                                        textAlign: TextAlign.left,
                                        style: Theme.of(context)
                                            .textTheme
                                            .bodyText2,
                                      ))
                                ])
                          ]))),
                  Column(
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children: <Widget>[
                        Text('${_formatTime(m.duration)}',
                            textAlign: TextAlign.end)
                      ])
                ])));
      }
    }

    return toReturn;
  }

  final DateFormat dateFormat = new DateFormat.ms();

  String _formatTime(int time) {
    return dateFormat.format(new DateTime.fromMillisecondsSinceEpoch(time));
  }

  Future<Image> _getMockImage() async {
    return Image(image: AssetImage('assets/AlbumArtSmall.jpg'));
  }

  Future<Image> _getImage(String path) async {
    if (path == null) {
      return null;
    }
    Image toReturn;
    try {
      String absoluteBath = await FlutterAbsolutePath.getAbsolutePath(path);
      File file = new File(absoluteBath);
      toReturn = Image.file(file);
    } catch (ex) {
      print(ex);
    }
    return toReturn;
  }
}

class Songs extends ChangeNotifier implements MediaSubscriber {
  final RequestChannel _requestChannel;
  String _id;

  List<Song> songs = List();

  Songs(this._requestChannel, this._id) : super() {
    print("subscribing to $_id");
    this._requestChannel.subscribe(this, this._id);
  }

  @override
  void onChildrenLoaded(String id, String children) {
    print("id: $id\nchildren: $children");

    Map<String, dynamic> jsonCode = json.decode(children);

    List<Song> songsList = List();
    for (dynamic d in jsonCode["songs"]) {
      Song metadata = Song.fromJson(d);
      songsList.add(metadata);
    }
    this.songs = songsList;
    notifyListeners();
  }
}
