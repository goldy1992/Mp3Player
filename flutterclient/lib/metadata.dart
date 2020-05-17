import 'package:json_annotation/json_annotation.dart';

part 'metadata.g.dart';

@JsonSerializable()
class Metadata {

  Metadata(this.id, this.title, this.artist, this.duration, this.albumArtPath);

  String id;
  String title;
  String artist;
  int duration;
  String albumArtPath;

  /// A necessary factory constructor for creating a new User instance
  /// from a map. Pass the map to the generated `_$UserFromJson()` constructor.
  /// The constructor is named after the source class, in this case, User.
  factory Metadata.fromJson(Map<String, dynamic> json) => _$MetadataFromJson(json);

  /// `toJson` is the convention for a class to declare support for serialization
  /// to JSON. The implementation simply calls the private, generated
  /// helper method `_$UserToJson`.
  Map<String, dynamic> toJson() => _$MetadataToJson(this);
}