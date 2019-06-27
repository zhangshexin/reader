import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:chianEducation/common/pre_util.dart';
import 'package:chianEducation/AppRoutes.dart';

class HomePage extends StatefulWidget {
  final String title;

  HomePage({Key key, this.title}) : super(key: key);

  @override
  _HomePageState createState() => new _HomePageState();
}

class _HomePageState extends State<HomePage> with WidgetsBindingObserver {
  int _counter = 0;

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
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
    WidgetsBinding.instance.removeObserver(this); //销毁
  }

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
    Future<String> json = PreferenceUtil().getPrefString(PreferenceUtil.KEY_USER_JSON);
    json.then((String value){
      debugPrint('哦哦=========$value');
      if(value==null||value.isEmpty){
        Navigator.pushNamed(context, AppRoutes.LOGIN_REGISTER_PAGE);
      }
      else{
        //根据用户角色admin/consumer来显示不用界面
      }
    }).catchError((Object error){
      debugPrint('出错了$error');
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(widget.title),
      ),
      drawer: _drawer,
      body: new Center(
        child: new Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            new Text('你点的次数：'),
            new Text(
              '$_counter',
              style: Theme.of(context).textTheme.display1,
            )
          ],
        ),
      ),
      floatingActionButton: new FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: '自增',
        child: new Icon(Icons.add),
      ),
    );
  }

  get _drawer => Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: <Widget>[
            DrawerHeader(
              decoration: BoxDecoration(color: Colors.lightBlueAccent),
              child: Center(
                child: SizedBox(
                  width: 60.0,
                  height: 60.0,
                  child: CircleAvatar(
                    child: Text('R'),
                  ),
                ),
              ),
            ),
            ListTile(
              leading: Icon(Icons.settings),
              title: Text('设置'),
            )
          ],
        ),
      );
}

///用户信息
class UserModel {
  final int id;
  final String phoneNumber;
  final String role;

  UserModel({this.id, this.phoneNumber, this.role});

  factory UserModel.fromJson(Map<String, dynamic> json) {
    return UserModel(
        id: json['id'], phoneNumber: json['phoneNumber'], role: json['role']);
  }
}
