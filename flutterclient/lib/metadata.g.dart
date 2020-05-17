// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'metadata.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Metadata _$MetadataFromJson(Map<String, dynamic> json) {
  return Metadata(
    json['id'] as String,
    json['title'] as String,
    json['artist'] as String,
    json['duration'] as int,
    json['albumArtPath'] as String,
  );
}

Map<String, dynamic> _$MetadataToJson(Metadata instance) => <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'artist': instance.artist,
      'duration': instance.duration,
      'albumArtPath': instance.albumArtPath,
    };
