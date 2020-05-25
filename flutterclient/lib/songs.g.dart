// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'songs.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Songs _$SongsFromJson(Map<String, dynamic> json) {
  return Songs(
    (json['songs'] as List)
        ?.map(
            (e) => e == null ? null : Song.fromJson(e as Map<String, dynamic>))
        ?.toList(),
  );
}

Map<String, dynamic> _$SongsToJson(Songs instance) => <String, dynamic>{
      'songs': instance.songs,
    };

Song _$SongFromJson(Map<String, dynamic> json) {
  return Song(
    json['id'] as String,
    json['title'] as String,
    json['artist'] as String,
    json['duration'] as int,
    json['albumArtPath'] as String,
  );
}

Map<String, dynamic> _$SongToJson(Song instance) => <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'artist': instance.artist,
      'duration': instance.duration,
      'albumArtPath': instance.albumArtPath,
    };
