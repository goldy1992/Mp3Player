library flutterclient.method_channels;

import 'dart:collection';
import 'dart:convert';
import 'package:flutterclient/AppProperties.dart';
import 'package:yaml/yaml.dart';
import "dart:io";
import 'package:flutter/services.dart' show MethodChannel, rootBundle;

const _connect = "connect";
const _subscribe = "subscribe";
const _request = "request";
const _APP_PROPERTIES_ASSET = "assets/AppProperties.yml";
const _METHOD_CHANNEL = "method-channel";
const _CONNECTION = "connection";
const _REQUEST = "request";
const _SUBSCRIPTION = "subscription";
const _PREFIX = "prefix";
const _TYPES = "types";
const _NAMES = "names";

String _CHANNEL_PREFIX;
Set<String> _CHANNEL_TYPES;
Map<String, String> _CHANNEL_NAME_SUFFIX_MAP;

Map<String, MethodChannel> method_channel_map = HashMap();

MethodChannel connection_channel;
MethodChannel request_channel;
MethodChannel subscription_channel;

Future<void> main() async {
  try {
    String yamlFile = await _loadYamlAsset();
    YamlMap yaml = loadYaml(yamlFile);
    Map<String, dynamic> methodChannel = Map<String, dynamic>.from(yaml[_METHOD_CHANNEL]);
    _CHANNEL_PREFIX = methodChannel[_PREFIX];
    _CHANNEL_TYPES = Set.from(methodChannel[_TYPES]);
    _CHANNEL_NAME_SUFFIX_MAP = Map<String, String>.from(methodChannel[_NAMES]);

    method_channel_map.putIfAbsent(_CHANNEL_TYPES., () => null)
  }
  catch(ex) {
    print("error: $ex");
  }
}

MethodChannel _createMethodChannel(String suffix) {
  String channelName = _CHANNEL_PREFIX + suffix;
  return MethodChannel(channelName);
}

Future<String> _loadYamlAsset() async {
  return await rootBundle.loadString("assets/AppProperties.yml");
}