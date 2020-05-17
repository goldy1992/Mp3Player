
import 'package:flutter/material.dart';
import 'package:flutterclient/MethodChannels.dart';

abstract class TabBody extends StatefulWidget {

}


abstract class _TabBodyState extends State<TabBody> implements MediaSubscriber {

  String id;
  RequestChannel requestChannel;

  _TabBodyState(this.id, this.requestChannel);
}


class _SongsTabBody extends _TabBodyState {
  _SongsTabBody(String id, RequestChannel requestChannel) : super(id, requestChannel);

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    throw UnimplementedError();
  }

  @override
  void onChildrenLoaded(String id, String children) {
    // TODO: implement onChildrenLoaded
  }

}
