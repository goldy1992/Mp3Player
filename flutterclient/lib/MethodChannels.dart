library flutterclient.method_channels;

import 'dart:collection';
import 'package:yaml/yaml.dart';
import "dart:io";
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


Future<void> main() async {
  try {
    String yamlFile = await _loadYamlAsset();
    YamlMap yaml = loadYaml(yamlFile);
    Map<String, dynamic> methodChannel = Map<String, dynamic>.from(yaml[_METHOD_CHANNEL]);
    _channelPrefix = methodChannel[_PREFIX];
    _channelSuffixMap = Map<String, String>.from(methodChannel[_SUFFIXES]);

    connectionChannel = new ConnectionCallback(getChannelName(_channelSuffixMap[_CONNECTION]));
    subscriptionChannel = new SubscriptionCallback(getChannelName(_channelSuffixMap[_SUBSCRIPTION]));
    requestChannel = new RequestChannel(getChannelName(_channelSuffixMap[_REQUEST]), connectionChannel, subscriptionChannel);
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
            subscriber.onConnected(call.arguments[0]);
          }
        }
        break;
        // TODO: implement methodCall
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
            for (MediaSubscriber s in mediaSubscribers) {
              s.onChildrenLoaded(id, children);
            }
          }
        }
        break;
    // TODO: implement methodCall
    }

  }
}


class RequestChannel {

  MethodChannel _methodChannel;

  SubscriptionCallback _subscriptionCallback;

  ConnectionCallback _connectionCallback;

  RequestChannel(String name, ConnectionCallback connectionCallback, SubscriptionCallback subscriptionCallback) {
    this._methodChannel = new MethodChannel(name);
    this._connectionCallback = connectionCallback;
    this._subscriptionCallback = subscriptionCallback;
  }

  void subscribe(MediaSubscriber subscriber, String id) {
    _subscriptionCallback.subscribe(subscriber, id);
    _methodChannel.invokeMethod("subscribe", id);
  }

  void connect(ConnectionSubscriber connectionSubscriber) {
    _connectionCallback.subscribe(connectionSubscriber);
    _methodChannel.invokeMethod("connect");
  }

}

abstract class ConnectionSubscriber extends Subscriber {
    void onConnected(String roodId);
}

abstract class MediaSubscriber extends Subscriber {
    void onChildrenLoaded(String id, String children);
}

abstract class Subscriber {}

