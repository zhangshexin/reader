import 'package:flutter/material.dart';
import 'package:chianEducation/Rout.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
//      title: 'Welcome to Flutter',
//      home: new Scaffold(
//        appBar: new AppBar(
//          title: new Text('Welcom to Flutter'),
//        ),
//        body: new Center(
//          child: new RandomWords(),
//        ),
//      ),

      title: 'startup name generator',
      initialRoute: "/",
      theme: new ThemeData(
        primaryColor: Colors.white,
        primarySwatch: Colors.blue
      ),
      routes: Rout.getRoutes(),
    );
  }
}
