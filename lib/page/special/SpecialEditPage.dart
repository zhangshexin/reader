import 'package:flutter/material.dart';

///专题编辑页
class SpecialEditPage extends StatefulWidget {
  @override
  _SpecialEditPage createState() => new _SpecialEditPage();
}

class _SpecialEditPage extends State<SpecialEditPage> {
  var _formKey = GlobalKey<FormState>();
  String _specialName; //专题名
  String _specialDes; //专题描述
  int _testTime; //考试时间
  bool _isRandomFromLib = false; //是否从题库随机
  int _examinCount; //人工指定随机考题数量
  String _setIds; //专题里指定的考题id
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    int id = ModalRoute.of(context).settings.arguments; //取到传过来的专题id
    debugPrint('id====$id');

    return Scaffold(
        appBar: AppBar(
          title: Text(id == null ? '新增专题' : '编辑专题'),
        ),
        body: Form(
          child: new ListView(
            padding: EdgeInsets.only(top: 10),
            children: <Widget>[
              _buildSpecialName(),
              _getSB(),
              _buildSpecialDes(),
              _getSB(),
              _buildTestCount(),
              _getSB(),
              _buildRandomCheckbox(),
              _getSB(),
              _buildExaminCount(),
              _getSB(),
              _checkExamine(),
              _getSB(),
              _BuildExamineEditPageBtn(),
              //listview
            ],
          ),
          key: _formKey,
        ));
  }

  IconButton _BuildExamineEditPageBtn() {
    return IconButton(
        icon: Row(
          children: <Widget>[
            Expanded(
                flex: 1,
                child: Container(
                  child: Align(
                    child: Text(
                      '新建考题',
                    ),
                    alignment: Alignment.center,
                  ),
                  color: Colors.yellow,
                )),
            Icon(Icons.arrow_forward_ios),
          ],
        ),
        onPressed: () {
          //跳转去编辑考题页;
        });
  }

  RaisedButton _checkExamine() {
    return RaisedButton(
      onPressed: () {
        //去考题列表页
      },
      child: Text('查看所有人工选题'),
      textColor: Colors.blue,
      color: Colors.amberAccent,
    );
  }

  TextFormField _buildExaminCount() {
    return TextFormField(
      decoration: InputDecoration(labelText: '启用随机后返回此数量的考题'),
      keyboardType: TextInputType.number,
      controller: TextEditingController(
          text: _examinCount == null ? '0' : _examinCount.toString()),
      validator: (String value) {
        if (value == null || int.parse(value) == 0) return '数量必须要填';
      },
      onSaved: (String value) => _examinCount = int.parse(value),
    );
  }

  Row _buildRandomCheckbox() {
    return Row(
      children: <Widget>[
        Expanded(
            flex: 6,
            child: Container(
              alignment: Alignment.center,
              child: CheckboxListTile(
                value: _isRandomFromLib,
                onChanged: (bool value) {
                  setState(() {
                    _isRandomFromLib = value;
                  });
                },
                activeColor: Colors.blue,
                title: Text(_isRandomFromLib ? '返回指定数量的随机考题' : '返回人工选择的考题'),
              ),
            )),
        Expanded(
            flex: 3,
            child: Container(
                alignment: Alignment.center,
                child: FlatButton(
                  onPressed: () {
                    //调用删除接口
                  },
                  child: Text('删除专题'),
                  textColor: Colors.blue,
                  shape: StadiumBorder(),
                  color: Colors.amber,
                  splashColor: Colors.red,
                ))),
      ],
    );
  }

  Row _buildTestCount() {
    return Row(
      children: <Widget>[
        SizedBox(
          width: 150,
          child: TextFormField(
            controller: TextEditingController(text: '0'),
            decoration: InputDecoration(labelText: '考试时长(分钟)'),
            keyboardType: TextInputType.number,
            validator: (String value) {
              if (value.isEmpty || int.parse(value) == 0) return '必须要有考试时间';
            },
            onSaved: (String value) => _testTime = int.parse(value),
          ),
        ),
        Text('分钟')
      ],
    );
  }

  SizedBox _getSB() {
    return SizedBox(
      height: 15,
    );
  }

  TextFormField _buildSpecialDes() {
    return TextFormField(
      decoration: InputDecoration(labelText: '给专题的一个描述'),
      initialValue: _specialDes,
      onSaved: (String value) => _specialDes = value,
    );
  }

  TextFormField _buildSpecialName() {
    return TextFormField(
      decoration: InputDecoration(labelText: '专题名'),
      initialValue: _specialName,
      validator: (String value) {
        if (value.isEmpty) return '专题名';
      },
      onSaved: (String value) => _specialName = value,
    );
  }
}
