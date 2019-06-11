class StringUtil{
  static List<String> matchImageSrc(String srcImageStr){
    List<String> result=[];
    ///这个是正则的方法
    RegExp exp=new RegExp(r"<(img|IMG)(.*?)(/>|></imag>)>");
    for(var item in exp.allMatches(srcImageStr)){
      String strImg=item.group(2);
      print('strImg == $strImg');
      RegExp pSrc=new RegExp(r"""(src|SRC)=(\"|\')(.*?)(\"|\')""");
      if(strImg!=null && strImg.isNotEmpty){
        var match = pSrc.firstMatch(strImg);
        if(match!=null){
          result.add(match.group(3));
        }
      }
    }
    return result;
  }


  static String getSrcImagePath(String srcImageStr){
    List list=matchImageSrc(srcImageStr);
    String path=list?.first;

    print('src ===$path');

    return path;
  }
}