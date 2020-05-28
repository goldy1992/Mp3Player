library flutterclient.method_channels;

import 'dart:collection';
import 'dart:convert';
import 'dart:ffi';
import 'package:flutterclient/rootitem.dart';
import 'package:flutterclient/songs.dart';
import 'package:random_string/random_string.dart';
import 'package:yaml/yaml.dart';
import 'package:flutter/services.dart' show MethodCall, MethodChannel, rootBundle;

const _APP_PROPERTIES_ASSET = "assets/AppProperties.yml";
const _METHOD_CHANNEL = "method-channel";
const _CONNECTION = "connection";
const _REQUEST = "request";
const _SUBSCRIPTION = "subscription";
const _PREFIX = "prefix";
const _SUFFIXES = "suffixes";

String _channelPrefix;
Map<String, String> _channelSuffixMap;

RequestChannel requestChannel;
SubscriptionCallback subscriptionChannel;
ConnectionCallback connectionChannel;


void loadMethodChannels() async {
  try {
    String yamlFile = await _loadYamlAsset();
    YamlMap yaml = loadYaml(yamlFile);
    Map<String, dynamic> methodChannel = Map<String, dynamic>.from(yaml[_METHOD_CHANNEL]);
    _channelPrefix = methodChannel[_PREFIX];
    _channelSuffixMap = Map<String, String>.from(methodChannel[_SUFFIXES]);

    connectionChannel = new ConnectionCallback(getChannelName(_channelSuffixMap[_CONNECTION]));
    subscriptionChannel = new SubscriptionCallback(getChannelName(_channelSuffixMap[_SUBSCRIPTION]));
    requestChannel = new RealRequestChannel(getChannelName(_channelSuffixMap[_REQUEST]), connectionChannel, subscriptionChannel);
  }
  catch(ex) {
    print("error: $ex");
  }
}

void loadFakeMethodChannels() async {
  try {
    String yamlFile = await _loadYamlAsset();
    YamlMap yaml = loadYaml(yamlFile);
    Map<String, dynamic> methodChannel = Map<String, dynamic>.from(yaml[_METHOD_CHANNEL]);
    _channelPrefix = methodChannel[_PREFIX];
    _channelSuffixMap = Map<String, String>.from(methodChannel[_SUFFIXES]);

    connectionChannel = new ConnectionCallback(getChannelName(_channelSuffixMap[_CONNECTION]));
    subscriptionChannel = new SubscriptionCallback(getChannelName(_channelSuffixMap[_SUBSCRIPTION]));
    requestChannel = new FakeRequestChannel(connectionChannel, subscriptionChannel);
  }
  catch(ex) {
    print("error: $ex");
  }
}

String getChannelName(String suffix) {
  return _channelPrefix + suffix;
}

Future<String> _loadYamlAsset() async {
  return await rootBundle.loadString(_APP_PROPERTIES_ASSET);
}

abstract class CallbackBase {

  MethodChannel _channel;

  Future<dynamic> methodCall(MethodCall call);

  CallbackBase(String name) {
    this._channel = new MethodChannel(name);
    this._channel.setMethodCallHandler(methodCall);
  }
}

class ConnectionCallback extends CallbackBase {

  List<ConnectionSubscriber> _subscribers = List();

  ConnectionCallback(String name) : super(name);

  void subscribe(ConnectionSubscriber subscriber) {
    _subscribers.add(subscriber);
  }

  void unsubscribe(ConnectionSubscriber subscriber) {
    _subscribers.remove(subscriber);
  }

  @override
  Future<dynamic> methodCall(MethodCall call) {
    switch (call.method) {
      case "onConnected":
      {
        print("onConnected called");
        for (ConnectionSubscriber subscriber in _subscribers) {
          subscriber.onConnected(call.arguments);
        }
      }
      break;
      default: break;
    }
  }
}

class SubscriptionCallback extends CallbackBase {

  Map<String, Set<MediaSubscriber>> _subscribers = HashMap();

  SubscriptionCallback(String name) : super(name);

  void subscribe(MediaSubscriber subscriber, String id) {
    if (_subscribers.containsKey(id)) {
      _subscribers[id].add(subscriber);
    } else {
      Set<MediaSubscriber> idSubscribers = HashSet();
      idSubscribers.add(subscriber);
      _subscribers.putIfAbsent(id, () => idSubscribers);
    }
  }

  void unsubscribe(MediaSubscriber subscriber) {
    for (Set<MediaSubscriber> s in _subscribers.values) {
      Iterator<MediaSubscriber> it = s.iterator;
      while(it.moveNext()) {
        if (subscriber == it.current) {
          s.remove(it.current);
        }
      }
    }

    _subscribers.remove(subscriber);
  }

  @override
  Future<dynamic> methodCall(MethodCall call) {
    switch (call.method) {
      case "onChildrenLoaded":
        {
          print("onChildrenLoaded called");
          String id = call.arguments["id"];
          String children = call.arguments["children"];

          Set<MediaSubscriber> mediaSubscribers = _subscribers[id];
          if (mediaSubscribers != null) {
            for (MediaSubscriber subscriber in mediaSubscribers) {
              subscriber.onChildrenLoaded(id, children);
            }
          }
        }
        break;
    }
  }
}

abstract class RequestChannel {
  SubscriptionCallback _subscriptionCallback;

  ConnectionCallback _connectionCallback;

  RequestChannel(this._connectionCallback, this._subscriptionCallback);

  void subscribe(MediaSubscriber subscriber, String id) {
    _subscriptionCallback.subscribe(subscriber, id);
  }

  void connect(ConnectionSubscriber connectionSubscriber) {
    _connectionCallback.subscribe(connectionSubscriber);
  }

}

class RealRequestChannel extends RequestChannel {

  MethodChannel _methodChannel;

  RealRequestChannel(String name, ConnectionCallback connectionCallback, SubscriptionCallback subscriptionCallback)
  : super(connectionCallback, subscriptionCallback){
    this._methodChannel = new MethodChannel(name);
  }

  @override
  void subscribe(MediaSubscriber subscriber, String id) {
    super.subscribe(subscriber, id);
    _methodChannel.invokeMethod("subscribe", id);
  }

  @override
  void connect(ConnectionSubscriber connectionSubscriber) {
    super.connect(connectionSubscriber);
    _methodChannel.invokeMethod("connect");
  }
}

class MockData {
  static const String rootId = "rootId";
  static const String songsId = "songsId";
  static const String foldersId = "foldersId";

  static final RootItem songs = RootItem(songsId, "Songs", null);
  static final RootItem folders = RootItem(foldersId, "Folders", null);

  static final RootItems rootItems = new RootItems([songs, folders]);

  static final Song song1 = new Song("song1", "aSong 1", "Artist1", 241231, null);
  static final Song song2 = new Song("song2", "bSong 2", "Artist1", 123131, null);
  static final Song song3 = new Song("song3", "cSong 3", "Artist2", 952231, null);
  static final Song song4 = new Song("song4", "dSong 4", "Artist5", 241231, null);
  static final Song song5 = new Song("song5", "eSong 5", "Artist6", 123131, null);
  static final Song song6 = new Song("song6", "fSong 6", "Artist7", 952231, null);

  static final Songs songList = new Songs(createSongs());

  static List<Song> createSongs() {
    List<Song> songs = List();
    for (int i = 1; i <= 100; i++) {
      Song song = Song(randomString(10), randomString(10).toLowerCase(), randomString(10), int.parse(randomNumeric(6)), null);
      songs.add(song);
    }
  Iterator<Song> songIt = SplayTreeSet.from(songs, (Song s1, Song s2) {return s1.title.compareTo(s2.title);}).iterator;
    List<Song> toReturn = List();
    while(songIt.moveNext()) {
      toReturn.add(songIt.current);
    }
    return toReturn;
  }
}

class FakeRequestChannel extends RequestChannel {

  static const String rootId = "rootId";
  static const String songsId = "songsId";
  static const String foldersId = "foldersId";

  FakeRequestChannel(ConnectionCallback connectionCallback, SubscriptionCallback subscriptionCallback)
      : super(connectionCallback, subscriptionCallback) {
    print("root items: ${MockData.rootItems}");
  }


  @override
  void connect(ConnectionSubscriber connectionSubscriber) {
    super.connect(connectionSubscriber);
    MethodCall methodCall = new MethodCall("onConnected", rootId);
    _connectionCallback.methodCall(methodCall);
  }

  @override
  void subscribe(MediaSubscriber subscriber, String id) {
    super.subscribe(subscriber, id);

    switch(id) {
      case rootId:
        Map<String, dynamic> arguments = HashMap();
        arguments.putIfAbsent("id", () => rootId);
        arguments.putIfAbsent("children", () => jsonEncode(MockData.rootItems));
        MethodCall methodCall = new MethodCall("onChildrenLoaded", arguments);
        _subscriptionCallback.methodCall(methodCall);
        break;
      case songsId:
        Map<String, String> arguments = HashMap();
        arguments.putIfAbsent("id", () => songsId);
        arguments.putIfAbsent("children", () => json.encode(MockData.songList));
        MethodCall methodCall = new MethodCall("onChildrenLoaded", arguments);
        _subscriptionCallback.methodCall(methodCall);
        break;
      default: return;
    }
  }
}


abstract class ConnectionSubscriber extends Subscriber {
    void onConnected(String roodId);
}

abstract class MediaSubscriber extends Subscriber {
    void onChildrenLoaded(String id, String children);
}

abstract class Subscriber {}

