import 'package:flutter/material.dart';

class MediaPlayer extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _MediaPlayerState();
}

class _MediaPlayerState extends State<MediaPlayer> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Song Name"),
        leading: IconButton(icon: Icon(Icons.arrow_back),
                            onPressed: () {Navigator.pop(context);}),
      ),
    );
  }
}
