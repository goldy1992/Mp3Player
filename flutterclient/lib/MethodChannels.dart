library flutterclient.method_channels;

import 'dart:collection';
import 'dart:convert';
import 'package:flutterclient/AppProperties.dart';
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
SubscriptionChannel subscriptionChannel;
ConnectionChannel connectionChannel;


Future<void> main() async {
  try {
    String yamlFile = await _loadYamlAsset();
    YamlMap yaml = loadYaml(yamlFile);
    Map<String, dynamic> methodChannel = Map<String, dynamic>.from(yaml[_METHOD_CHANNEL]);
    _channelPrefix = methodChannel[_PREFIX];
    _channelSuffixMap = Map<String, String>.from(methodChannel[_SUFFIXES]);

    connectionChannel = new ConnectionChannel(getChannelName(_channelSuffixMap[_CONNECTION]));
    requestChannel = new RequestChannel(getChannelName(_channelSuffixMap[_REQUEST]));
    subscriptionChannel = SubscriptionChannel(requestChannel, getChannelName(_channelSuffixMap[_SUBSCRIPTION]));
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

abstract class ChannelBase {

  MethodChannel _channel;

  Future<dynamic> methodCall(MethodCall call);

  ChannelBase(String name) {
    this._channel = new MethodChannel(name);
    this._channel.setMethodCallHandler(methodCall);
  }
}


class ConnectionChannel extends ChannelBase {

  List<ConnectionSubscriber> _subscribers = List();

  ConnectionChannel(String name) : super(name);

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
            subscriber.onConnected();
          }
        }
        break;
        // TODO: implement methodCall
    }

  }
}


class SubscriptionChannel extends ChannelBase {

  Map<String, Set<MediaSubscriber>> _subscribers = HashMap();
  RequestChannel _requestChannel;

  SubscriptionChannel(RequestChannel requestChannel, String name) : super(name) {
    this._requestChannel = requestChannel;
  }

  void subscribe(MediaSubscriber subscriber, String id) {
    if (_subscribers.containsKey(id)) {
      _subscribers[id].add(subscriber);
    } else {
      Set<MediaSubscriber> idSubscribers = HashSet();
      idSubscribers.add(subscriber);
      _subscribers.putIfAbsent(id, () => idSubscribers);
    }
    _requestChannel.subscribe(subscriber, id);
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
          String id = call.arguments[0];
          String children = call.arguments[1];

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

  RequestChannel(String name) {
    this._methodChannel = new MethodChannel(name);
  }

  void subscribe(MediaSubscriber subscriber, String id) {
    _methodChannel.invokeMethod("subscribe", id);
  }

  @override
  Future methodCall(MethodCall call) {
    // TODO: implement methodCall
    throw UnimplementedError();
  }

}

abstract class ConnectionSubscriber extends Subscriber {
    void onConnected();
}

abstract class MediaSubscriber extends Subscriber {
    void onChildrenLoaded(String id, String children);
}

abstract class Subscriber {}

