import 'package:json_annotation/json_annotation.dart';

part 'songs.g.dart';

@JsonSerializable()
class Songs {
  List<Song> songs;

  Songs(this.songs);

  factory Songs.fromJson(Map<String, dynamic> json) => _$SongsFromJson(json);

  Map<String, dynamic> toJson() => _$SongsToJson(this);

}



@JsonSerializable()
class Song {

  Song(this.id, this.title, this.artist, this.duration, this.albumArtPath);

  String id;
  String title;
  String artist;
  int duration;
  String albumArtPath;

  /// A necessary factory constructor for creating a new User instance
  /// from a map. Pass the map to the generated `_$UserFromJson()` constructor.
  /// The constructor is named after the source class, in this case, User.
  factory Song.fromJson(Map<String, dynamic> json) => _$SongFromJson(json);

  /// `toJson` is the convention for a class to declare support for serialization
  /// to JSON. The implementation simply calls the private, generated
  /// helper method `_$UserToJson`.
  Map<String, dynamic> toJson() => _$SongToJson(this);
}