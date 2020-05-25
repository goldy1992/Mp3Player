// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'rootitem.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

RootItem _$RootItemFromJson(Map<String, dynamic> json) {
  return RootItem(
    json['id'] as String,
    json['title'] as String,
    json['description'] as String,
  );
}

Map<String, dynamic> _$RootItemToJson(RootItem instance) => <String, dynamic>{
      'id': instance._id,
      'title': instance.title,
      'description': instance.description,
    };
