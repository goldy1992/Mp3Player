import 'dart:convert';
import 'package:yaml/yaml.dart';
import "dart:io";
import 'package:flutter/services.dart' show rootBundle;

const connect = "connect";
const subscribe = "subscribe";
const request = "request";
const APP_PROPERTIES_ASSET = "assets/AppProperties.yml";
const METHOD_CHANNEL = "method-channel";
const CONNECTION = "connection";
const REQUEST = "request";
const SUBSCRIPTION = "subscription";
const PREFIX = "prefix";
const TYPES = "types";
const NAMES = "names";

class AppProperties {

  String _CHANNEL_PREFIX;
  Set<String> _CHANNEL_TYPES;
  Map<String, String> _CHANNEL_NAME_MAP;
  
  
  Future<void> init() async {
    try {
      String yamlFile = await loadAsset();
      YamlMap yaml = loadYaml(yamlFile);
      Map<String, dynamic> methodChannel = Map<String, dynamic>.from(yaml[METHOD_CHANNEL]);
      _CHANNEL_PREFIX = methodChannel[PREFIX];
      _CHANNEL_TYPES = Set.from(methodChannel[TYPES]);
      _CHANNEL_NAME_MAP = Map<String, String>.from(methodChannel[NAMES]);
    }
    catch(ex) {
      print("error: $ex");
    }
  }


}