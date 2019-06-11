import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'dart:math' as math;

class DrawProgress extends CustomPainter{
  final Color color;
  final double radius;
  double angle;
  AnimationController animationController;

  Paint circleFillPaint;
  Paint progressPaint;
  Rect rect;

  /**
   * 花括号中的是可选参数
   */
  DrawProgress(this.color,this.radius,{double this.angle,AnimationController this.animationController}){
    circleFillPaint=new Paint();
    circleFillPaint.color=Colors.white;
    circleFillPaint.style=PaintingStyle.fill;

    progressPaint=new Paint();
    progressPaint.color=color;
    progressPaint.style=PaintingStyle.stroke;
    progressPaint.strokeCap=StrokeCap.round;
    progressPaint.strokeWidth=4.0;
    
    if(animationController!=null && !animationController.isAnimating)
      animationController.forward();//启动
  }

  @override
  void paint(Canvas canvas, Size size) {
    double x =size.width/2;
    double y =size.height/2;
    Offset center=new Offset(x, y);
    canvas.drawCircle(center, radius-2, circleFillPaint);
    
    rect=Rect.fromCircle(center: center,radius: radius);//因为这里的参数是可选项，所以要明确给的是哪个形参
    angle = angle *(-1);
    double startAngle=-math.pi/2;
    double sweepAngle=math.pi*angle/180;
    print('draw paint------- $startAngle   ,$sweepAngle');
    
    Path path=new Path();
    path.arcTo(rect, startAngle, sweepAngle, true);
    canvas.drawPath(path, progressPaint);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return oldDelegate!=this;
  }

}