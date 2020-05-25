import 'package:json_annotation/json_annotation.dart';

part 'rootitem.g.dart';

@JsonSerializable(explicitToJson: true)
class RootItems {
    List<RootItem> rootItems;

    RootItems(this.rootItems);

    factory RootItems.fromJson(Map<String, dynamic> json) => _$RootItemsFromJson(json);

    Map<String, dynamic> toJson() => _$RootItemsToJson(this);
}


@JsonSerializable(explicitToJson: true)
class RootItem {

  RootItem(this.id, this.title, this.description);

  String id;
  String title;
  String description;
  /// A necessary factory constructor for creating a new User instance
  /// from a map. Pass the map to the generated `_$UserFromJson()` constructor.
  /// The constructor is named after the source class, in this case, User.
  factory RootItem.fromJson(Map<String, dynamic> json) => _$RootItemFromJson(json);

  /// `toJson` is the convention for a class to declare support for serialization
  /// to JSON. The implementation simply calls the private, generated
  /// helper method `_$UserToJson`.
  Map<String, dynamic> toJson() => _$RootItemToJson(this);

}