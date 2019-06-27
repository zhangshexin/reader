import 'package:flutter/material.dart';
import 'dart:io';
import 'dart:convert';
import 'package:chianEducation/Api.dart';
import 'package:chianEducation/common/pre_util.dart';

///登录和注册页
class LoginAndRegisterPage extends StatefulWidget {
  final String title;

  LoginAndRegisterPage({Key key, this.title}) : super(key: key);

  @override
  _LoginAndRegisterPageState createState() => _LoginAndRegisterPageState();
}

class _LoginAndRegisterPageState extends State<LoginAndRegisterPage> {
  final _formkey = GlobalKey<FormState>(); //
  String _email, _password;
  bool _isObscure = true;
  Color _eyeColor;

  bool _isLogin = true;

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      child: Scaffold(
          body: Form(
        key: _formkey,
        child: body,
      )),
      onWillPop: () {
        //不让退出
        debugPrint('不让退出------');
      },
    );
  }

  ///登录界面
  ListView get body {
    return ListView(
      padding: EdgeInsets.symmetric(horizontal: 22.0),
      children: <Widget>[
        SizedBox(
          height: kToolbarHeight,
        ),
        _buildTitle(),
        _buildTitleLine(),
        SizedBox(height: 70.0),
        _buildEmailField(),
        SizedBox(
          height: 30.0,
        ),
        _buildPasswordTextFiedl(context),
        _buildForgetPasswordText(context),
        SizedBox(
          height: 60.0,
        ),
        _buildLoginButton(context),
        SizedBox(
          height: 30.0,
        ),
        _buildRegisterText(context)
      ],
    );
  }

  Align _buildRegisterText(BuildContext context) {
    return Align(
      alignment: Alignment.center,
      child: Padding(
        padding: EdgeInsets.only(top: 10.0),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(_isLogin ? '没有账号？' : ''),
            GestureDetector(
              child: Text(
                _isLogin ? '点击注册' : '去登录',
                style: TextStyle(color: Colors.green),
              ),
              onTap: () {
                //TODO ---
                print('切换为注册界面$_isLogin');
                _switchUi();
              },
            )
          ],
        ),
      ),
    );
  }

  Align _buildLoginButton(BuildContext context) {
    return Align(
      child: SizedBox(
        height: 45.0,
        width: 270.0,
        child: RaisedButton(
          child: Text(
            _isLogin ? '登录' : '注册',
            style: TextStyle(color: Colors.white),
          ),
          color: Colors.black,
          onPressed: () {
            if (_formkey.currentState.validate()) {
              _formkey.currentState.save();
              //TODO 执行登录/注册
              print('email:$_email , assword:$_password');
              _doLoginOrRegister();
            }
          },
          shape: StadiumBorder(side: BorderSide()),
        ),
      ),
    );
  }

  ///忘记密码
  Padding _buildForgetPasswordText(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 8.0),
      child: Align(
        alignment: Alignment.centerRight,
        child: FlatButton(
            onPressed: () {},
            child: Text(
              '忘记密码',
              style: TextStyle(fontSize: 14.0, color: Colors.grey),
            )),
      ),
    );
  }

  ///输入密码
  TextFormField _buildPasswordTextFiedl(BuildContext context) {
    return TextFormField(
      onSaved: (String value) => _password = value,
      obscureText: _isObscure,
      validator: (String value) {
        if (value.isEmpty) {
          return '请输入密码';
        }
      },
      decoration: InputDecoration(
          labelText: '密码',
          suffixIcon: IconButton(
              icon: Icon(
                Icons.remove_red_eye,
                color: _eyeColor,
              ),
              onPressed: () {
                setState(() {
                  _isObscure = !_isObscure;
                  _eyeColor = _isObscure
                      ? Colors.grey
                      : Theme.of(context).iconTheme.color;
                });
              })),
    );
  }

  ///账号输入框
  TextFormField _buildEmailField() {
    return TextFormField(
      decoration: InputDecoration(labelText: '输入账号'),
      validator: (String value) {
        if (value.isEmpty) {
          return '账号不能为空';
        }
      },
      onSaved: (String value) => _email = value,
    );
  }

  Padding _buildTitleLine() {
    return Padding(
      padding: EdgeInsets.only(left: 12.0, top: 4.0),
      child: Align(
        alignment: Alignment.bottomLeft,
        child: Container(
          color: Colors.black,
          width: 40.0,
          height: 2.0,
        ),
      ),
    );
  }

  Padding _buildTitle() {
    return Padding(
      padding: EdgeInsets.all(8.0),
      child: Text(
        _isLogin ? '登录' : '注册',
        style: TextStyle(fontSize: 42.0),
      ),
    );
  }

  _switchUi() {
    setState(() {
      _isLogin = !_isLogin;
      _formkey.currentState.reset();
    });
  }

  ///执行登录/注册
  _doLoginOrRegister() async {
    Uri uri = _isLogin
        ? Api.loginPost(_email, _password)
        : Api.registerPost(_email, _password);
    try {
      var httpClient = new HttpClient();
      debugPrint('访问的url:$uri');
      var request = await httpClient.postUrl(uri);
      var response = await request.close();
      if(response.statusCode==HttpStatus.ok){
        var json=await response.transform(utf8.decoder).join();
        debugPrint('json数据：$json');
        //处理并保存一下数据
        Map<String,dynamic> map=jsonDecode(json);
        if(map['code']==200){
          //保存
          String userInfo=map['result'].toString();
          debugPrint('userinfo====$userInfo');
          await PreferenceUtil().setPrefString(PreferenceUtil.KEY_USER_JSON, userInfo);
        }else{
          String msg=map['msg'];
          _showSnackBar(context, msg);
        }
      }else{
        debugPrint('凉凉了^^^^');
      }
    } catch (e) {
      _showSnackBar(context, '网络出现异常，稍后再试');
      print('出错了啊====$e');
    }
  }

  _showSnackBar(BuildContext context,String msg){
    if(msg==null||msg.isEmpty)
      msg='再试试吧！';
    Scaffold.of(context).showSnackBar(SnackBar(
      content: Text(msg),
      action: SnackBarAction(label: 'ok', onPressed: () {}),
    ));
  }
}
