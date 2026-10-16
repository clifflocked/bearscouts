// representation of the json for a game
class Game {
  Game({
    required this.specVersion,
    required this.name,
    required this.rules,
  });

  final int specVersion;

  final String name;

  final String rules;
  
  static Game fromJSON(Map<String, Object?> json){
    return switch(json){
      {
        'specVersion':  final int specVersion,
        'name': final String name,
        'rules': final String rules,
      } =>
        Game(
          specVersion: specVersion,
          name: name,
          rules: rules
        ),
      _ =>
        throw FormatException("the JSON didnt work right :(, json=$json"),
    };
  }

  //TODO: add @override for toString

}