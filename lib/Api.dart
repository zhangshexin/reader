class Api{
  static const String  App_Host_URL='http://www.wsrtv.com.cn';

  static String makeUrl(String page,List<String> params){
    String url=App_Host_URL;

    if(page!=null&&page.isNotEmpty)
      page='/$page';

    url=url+page;

    if(params!=null&&params.length>0){
      if(!url.contains("?")){
        url+='?';
      }else if(url.contains('=')&&!url.endsWith("=")){
        url+="&";
      }

      for(int i=0;i<params.length;i+=2){
        url+=params[i]+"="+params[i+1]+"&";
      }

      url=url.substring(0,url.length-1);
    }

    print('url============$url');

    return url;
  }

  static Map<String,String> getNormalRequestHeader(String token){
    Map<String,String> map={'X-CSRF-Token':token};
    return map;
  }
}