
import 'dart:io';

import 'package:flutterclient/MethodChannels.dart';
import 'package:flutterclient/metadata.dart';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_absolute_path/flutter_absolute_path.dart';
import 'package:provider/provider.dart';


class SongList extends StatelessWidget  {

  SongList({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ListView(
        padding: const EdgeInsets.all(8),
        children: []//getChildren(context)
    );
  }

  List<Widget> getChildren(BuildContext context) {
    List<Widget> toReturn = List();
    List<Metadata> metadataList = Provider.of<Songs>(context).songs;
    if (metadataList.length <= 0) {
      toReturn.add(Flexible(
          child: Container(
            height: 50,
            color: Colors.amber[600],
            child: Center(child:  Text('Entry: ')),
          )));
    } else {
      for (Metadata m in metadataList) {
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

  List<Metadata> songs = List();

  Songs(this._requestChannel, this._id) : super() {
    print("subscribing to $_id");
    this._requestChannel.subscribe(this, this._id);
  }

  @override
  void onChildrenLoaded(String id, String children) {
    print("id: $id\nchildren: $children");

    List<dynamic> jsonCode = json.decode(children);

    List<Metadata> metadataList = List();
    for (dynamic d in jsonCode) {
      Metadata metadata = Metadata.fromJson(d);
      metadataList.add(metadata);
    }
    this.songs = metadataList;
    notifyListeners();
  }
}

