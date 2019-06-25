import 'package:shared_preferences/shared_preferences.dart';

///preference工具类
class PreferenceUtil{
  ///用户信息的key
  static const String KEY_USER_JSON='key_user_json';
  ///存储数据，json
  setPrefString(String key, String value) async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    preferences.setString(key, value);
  }
  ///根据key取也对应的json数据
  getPrefString(String key) async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    return preferences.get(key);
  }
  ///清空数据
  cleanAll() async{
    SharedPreferences preferences = await SharedPreferences.getInstance();
    preferences.clear();
  }
}

