// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'rootitem.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

RootItems _$RootItemsFromJson(Map<String, dynamic> json) {
  return RootItems(
    (json['rootItems'] as List)
        ?.map((e) =>
            e == null ? null : RootItem.fromJson(e as Map<String, dynamic>))
        ?.toList(),
  );
}

Map<String, dynamic> _$RootItemsToJson(RootItems instance) => <String, dynamic>{
      'rootItems': instance.rootItems?.map((e) => e?.toJson())?.toList(),
    };

RootItem _$RootItemFromJson(Map<String, dynamic> json) {
  return RootItem(
    json['id'] as String,
    json['title'] as String,
    json['description'] as String,
  );
}

Map<String, dynamic> _$RootItemToJson(RootItem instance) => <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'description': instance.description,
    };
