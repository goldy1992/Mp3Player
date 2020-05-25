
import 'dart:io';

import 'package:flutterclient/MethodChannels.dart';
import 'package:flutterclient/metadata.dart';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_absolute_path/flutter_absolute_path.dart';
import 'package:flutterclient/songs.dart';
import 'package:provider/provider.dart';


class SongList extends StatelessWidget  {

  SongList({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ListView(
        padding: const EdgeInsets.all(8),
        children: getChildren(context)
    );
  }

  List<Widget> getChildren(BuildContext context) {
    List<Widget> toReturn = List();
    List<Song> songs = Provider.of<Songs>(context).songs;
    if (songs.length <= 0) {
      toReturn.add(Flexible(
          child: Container(
            height: 50,
            color: Colors.amber[600],
            child: Center(child:  Text('Entry: ')),
          )));
    } else {
      for (Song m in songs) {
        toReturn.add(
            Container(
                height: 50,
                color: Theme.of(context).backgroundColor,
                child: Row(
                    children: [
                      FutureBuilder<Image> (
                        future: _getImage(m.albumArtPath),
                        builder: (BuildContext context, AsyncSnapshot<Image> snapshot) {
                          if (snapshot.hasData) {
                            return Center( child: snapshot.data );
                          }
                          else return Center();
                        },),

                      Container(
                          child: Column(
                              children: <Widget>[
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.start,
                                  children: <Widget>[
                                    Text('${m.title}',
                                      textAlign: TextAlign.left,
                                    )],
                                ),
                                Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    children: <Widget>[
                                      Text(
                                        '${m.artist}',
                                        textAlign: TextAlign.left,)])
                              ]
                          ))]
             )));
      }
    }

    return toReturn;
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
    } catch(ex) {
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

