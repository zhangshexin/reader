import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:chianEducation/common/pre_util.dart';
import 'package:chianEducation/AppRoutes.dart';
import 'dart:convert';
import 'package:chianEducation/Api.dart';
import 'dart:io';
import 'package:chianEducation/page/bean/SpecialBean.dart';

class HomePage extends StatefulWidget {
  final String title;

  HomePage({Key key, this.title}) : super(key: key);

  @override
  _HomePageState createState() => new _HomePageState();
}

class _HomePageState extends State<HomePage> with WidgetsBindingObserver {
  //专题列表的数据------------
  final int _pageSize = 20;
  final int _status = 1;
  int _pageNumber = 1;
  SpecialResult _specialResult;

  //------------
  int _counter = 0;

  UserModel _userModel;

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    WidgetsBinding.instance.addObserver(this); //添加观察者
    print('这是初始华啊8888888888');
    _checkUserStatus();
    _getSpecialListData();
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
    WidgetsBinding.instance.removeObserver(this); //销毁
  }

  bool _pushedLoginPage = false;

  ///创建期: initState
  ///
  ///进入后台: inactive -> paused
  ///
  ///从后台恢复: inactive -> resumed
  ///resumed:应用可见并可响应用户操作
  ///inactive:用户可见，但不可响应用户操作
  ///paused:已经暂停了，用户不可见、不可操作
  ///suspending：应用被挂起，此状态IOS永远不会回调
  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    // TODO: implement didChangeAppLifecycleState
    super.didChangeAppLifecycleState(state);
    print('=========生命周期发生变化 $state');
    if (state == AppLifecycleState.resumed) _checkUserStatus();
  }

  ///检查是否已登录
  _checkUserStatus() {
    //在这里判断一下是否已登录，否则跳转到登录页
    Future<String> json =
        PreferenceUtil().getPrefString(PreferenceUtil.KEY_USER_JSON);
    json.then((String value) {
      debugPrint('哦哦=========$value');
      if (value == null || value.isEmpty) {
        if (!_pushedLoginPage) {
          Navigator.pushNamed(context, AppRoutes.LOGIN_REGISTER_PAGE)
              .then((userInfo) {
            if (userInfo != null) {
              String info = userInfo;
              //根据用户角色admin/consumer来显示不用界面
              Map<String, dynamic> json = jsonDecode(info);
              setState(() {
                _userModel = UserModel.fromJson(json);
                print('用户信息:$_userModel');
              });
            }
          });
          setState(() {
            _pushedLoginPage = true;
          });
        }
      } else {
        //根据用户角色admin/consumer来显示不用界面
        Map<String, dynamic> json = jsonDecode(value);
        setState(() {
          _userModel = UserModel.fromJson(json);
          print('用户信息:$_userModel');
        });
      }
    }).catchError((Object error) {
      debugPrint('出错了$error');
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(widget.title),
        actions: <Widget>[
          _userModel.role == 'admin'
              ? IconButton(
                  onPressed: () {
                    //添加新的专题
                    _goSpecialEditPage(context, null);
                  },
                  icon: new Icon(Icons.add),
                )
              : Text('')
        ],
      ),
      drawer: _drawer,
      body: new Center(
        child: _loadSpecialList(context),
      ),
    );
  }

  ///加载专题列表
  ListView _loadSpecialList(BuildContext context) {
    return ListView.builder(
        itemCount: _specialResult == null ? 0 : _specialResult.list.length,
        itemBuilder: (BuildContext context, int position) {
          return new ListTile(
            trailing:_userModel.role=='admin'? IconButton(
                icon: Icon(Icons.edit),
                onPressed: () {
                  debugPrint('我点了一下');
                  _goSpecialEditPage(context, _specialResult.list[position].id);
                }):null,
            title: Text(_specialResult.list[position].specialName),
            subtitle: Text(_specialResult.list[position].specialDes),
          );
        });
  }

  ///加载专题列表数据
  _getSpecialListData() async {
    try {
      Uri uri = Api.specialListGet(_pageNumber, _pageSize, _status);
      debugPrint('get专题列表uri:$uri');
      var httpclient = HttpClient();
      var request = await httpclient.getUrl(uri);
      var response = await request.close();
      if (response.statusCode == HttpStatus.ok) {
        var json = await response.transform(utf8.decoder).join();
        debugPrint('get专题列表：$json');
        Map<String, dynamic> result = jsonDecode(json);
        if (result['code'] == 200) {
          setState(() {
            _specialResult = SpecialResult.fromJson(result['result']);
          });
          debugPrint('special list:' + jsonEncode(result['result']));
        } else {
          //弹出提示
          debugPrint('数据正常访问返回失败');
        }
      } else {
        debugPrint('get专题列表凉了：$response');
      }
    } catch (e) {
      debugPrint('get专题列表：$e');
    }
  }

  ///去专题编辑页，如果参数为空就是新增专题
  _goSpecialEditPage(BuildContext context, int content) {
    Navigator.pushNamed(context, AppRoutes.SPECIAL_EDIT_PAGE,
        arguments: content);
  }

  get _drawer => Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: <Widget>[
            DrawerHeader(
              decoration: BoxDecoration(color: Colors.lightBlueAccent),
              child: Center(
                  child: ListView(
                children: <Widget>[
                  Padding(
                    padding: EdgeInsets.only(top: 30),
                  ),
                  SizedBox(
                    width: 60.0,
                    height: 60.0,
                    child: CircleAvatar(
                      child: Text(
                          _userModel == null ? 'R' : _userModel.phoneNumber),
                    ),
                  ),
                  Padding(
                    padding: EdgeInsets.only(top: 15),
                  ),
                  Center(
                    child: Text(
                      '[' +
                          (_userModel == null ? 'role' : _userModel.role) +
                          ']',
                      style: TextStyle(fontSize: 12, color: Colors.yellow),
                    ),
                  )
                ],
              )),
            ),
            Padding(
              padding: EdgeInsets.only(top: 30),
              child: ListTile(
                leading: Icon(Icons.exit_to_app),
                title: Text('退出登录',
                    style: TextStyle(fontSize: 24, color: Colors.red)),
                onTap: () {
                  print('按了一下,清理数据跳转登录页');
                  PreferenceUtil().cleanAll().then((result) {
                    if (result) {
                      Navigator.pushNamed(
                              context, AppRoutes.LOGIN_REGISTER_PAGE)
                          .then((userInfo) {
                        if (userInfo != null) {
                          String info = userInfo;
                          //根据用户角色admin/consumer来显示不用界面
                          Map<String, dynamic> json = jsonDecode(info);
                          setState(() {
                            _userModel = UserModel.fromJson(json);
                            print('用户信息:$_userModel');
                          });
                        }
                      });
                    }
                  });
                },
              ),
            )
          ],
        ),
      );
}

///用户信息
class UserModel {
  int id;
  String phoneNumber;
  String role;

  UserModel({this.id, this.phoneNumber, this.role});

  factory UserModel.fromJson(Map<String, dynamic> json) {
    return UserModel(
        id: json['id'], phoneNumber: json['phoneNumber'], role: json['role']);
  }
}
