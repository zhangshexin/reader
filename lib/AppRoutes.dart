import 'package:flutter/material.dart';
import 'package:chianEducation/page/HomePage.dart';
import 'package:chianEducation/page/SplashPage.dart';
import 'package:chianEducation/page/LoginAndRegisterPage.dart';
import 'package:chianEducation/page/special/SpecialEditPage.dart';

class AppRoutes {
  static const String SPLASH_PAGE = '/'; //启动页
  static const String HOME_PAGE = '/main'; //首页
  static const String LOGIN_REGISTER_PAGE='/login8register';//登录注册页
  static const String SPECIAL_EDIT_PAGE='/specialedit';

  static Map<String, WidgetBuilder> getRoutes() {
    var route={
      SPLASH_PAGE:(context)=>SplashPage(),
      HOME_PAGE:(context)=>HomePage(title: '首页'),
      LOGIN_REGISTER_PAGE:(context)=>LoginAndRegisterPage(),
      SPECIAL_EDIT_PAGE:(context)=>SpecialEditPage(),
    };
    return route;
  }
}
