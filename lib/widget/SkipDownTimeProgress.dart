import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'DrawProgress.dart';

///
///在启动页显"跳过"的控件
///
class SkipDownTimeProgress extends StatefulWidget{

  final Color color;
  final double radius;
  final Duration duration;//这是一个类似定时器
  final Size size;
  String skipText;
  OnSkipClickListener onSkipClickListener;


  SkipDownTimeProgress(this.color,this.radius,this.duration,this.size,
  {
    Key key,this.skipText="跳过",OnSkipClickListener this.onSkipClickListener
  }):super(key:key);

  @override
  _SkipDownTimeProgressState createState() {
    return new _SkipDownTimeProgressState();
  }
}

class _SkipDownTimeProgressState extends State<SkipDownTimeProgress> with SingleTickerProviderStateMixin{
  AnimationController animationController;
  double curAngle=360.0;
  
  @override
  void initState() {
    super.initState();
    print('初始化');
    animationController=new AnimationController(vsync: this,duration: widget.duration);
    animationController.addListener(_change);
    _doAnimation();
  }

  void _onSkipClick(){
    if(widget.onSkipClickListener!=null){
      print('skip onclick-------------');
      widget.onSkipClickListener.onSkipClick();
    }
  }

  void _doAnimation() async{
      Future.delayed(new Duration(microseconds: 50),(){
        if(mounted){
          animationController.forward().orCancel;
        }else{
          _doAnimation();
        }
      });
  }
  void _change(){
    print('ange=======$animationController.value');
    double ange=double.parse(((animationController.value*360)~/1).toString());
    setState(() {
      curAngle=(360.0-ange);
    });
  }
  @override
  Widget build(BuildContext context) {
    return new GestureDetector(
      onTap: _onSkipClick,
      child: new Stack(
        alignment: Alignment.center,
        children: <Widget>[
          new CustomPaint(
            painter: new DrawProgress(widget.color,widget.radius,angle: curAngle),
            size: widget.size,
          ),
          Text(
            widget.skipText,
            style: TextStyle(
              color: widget.color,
              fontSize: 13.5,
              decoration: TextDecoration.none
            ),
          )
        ],
      ),
    );
  }

}



abstract class OnSkipClickListener{
  void onSkipClick();
}