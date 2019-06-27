import 'package:flutter/material.dart';
import 'package:chianEducation/page/HomePage.dart';
import 'package:chianEducation/page/SplashPage.dart';
import 'package:chianEducation/page/LoginAndRegisterPage.dart';

class AppRoutes {
  static const String SPLASH_PAGE = '/'; //启动页
  static const String HOME_PAGE = '/main'; //首页
  static const String LOGIN_REGISTER_PAGE='/login8register';//登录注册页

  static Map<String, WidgetBuilder> getRoutes() {
    var route={
      SPLASH_PAGE:(context)=>SplashPage(),
      HOME_PAGE:(context)=>HomePage(title: '首页'),
      LOGIN_REGISTER_PAGE:(context)=>LoginAndRegisterPage()
    };
    return route;
  }
}
