import 'dart:io';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:chianEducation/widget/SkipDownTimeProgress.dart';
import 'package:chianEducation/Api.dart';
import 'package:chianEducation/common/StringUtil.dart';

/**
 * 启动页
 */
class SplashPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new _SplashPageState();
  }
}

class _SplashPageState extends State<SplashPage>
    implements OnSkipClickListener {
  var welcomeImageUrl = '';

  @override
  void initState() {
    super.initState();
    _getWelcomImage();
    _delayedGoHomePage();
  }

  @override
  Widget build(BuildContext context) {
    return new Stack(
      alignment: Alignment.bottomCenter,
      children: <Widget>[
        new Container(
          color: Colors.white,
          child: new Image.network(
            welcomeImageUrl,
            fit: BoxFit.cover,
          ),
          constraints: new BoxConstraints.expand(),
        ),
        new Image.asset(
          'images/logo.jpg',
          fit: BoxFit.fitWidth,
        ),
        new Container(
          child: Align(
            alignment: Alignment.topRight,
            child: new Container(
              padding: const EdgeInsets.only(top: 30.0, right: 20),
              child: new SkipDownTimeProgress(
                Colors.red,
                22.0,
                new Duration(seconds: 5),
                new Size(25.0, 25.0),
                skipText: "跳过",
                onSkipClickListener: this,
              ),
            ),
          ),
        )
      ],
    );
  }

  /**
   * 加载启动图从网络
   */
  _getWelcomImage() async {
    try {
      var url = Api.makeUrl('services/app_ad_cover.json', null);
      var httpClient = new HttpClient();
      var request = await httpClient.getUrl(Uri.parse(url));
      var response = await request.close();
      if (response.statusCode == HttpStatus.ok) {
        var json = await response.transform(utf8.decoder).join();
        List data = jsonDecode(json);
        String cover;
        for (var item in data) {
          cover = item['field_app_ad_cover'];
          if (cover != null && cover.isNotEmpty) {
            cover = StringUtil.getSrcImagePath(cover);
            break;
          }
        }

        print('cover======$cover');
        setState(() {
          welcomeImageUrl = cover;
        });
      }
    } catch (e) {
      print('网络请求错误：$e');
    }
  }

  _delayedGoHomePage() {
    Future.delayed(new Duration(seconds: 5), () {
      _goHomePage();
    });
  }

  /**
   * 去首页
   */
  _goHomePage() {
    Navigator.pushNamedAndRemoveUntil(
        context, '/main', (Route<dynamic> route) => false);
  }

  @override
  void onSkipClick() {
    _goHomePage();
  }
}
