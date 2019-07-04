class Special {
  ///{"id":7,"specialName":"xinzeng2","specialDes":"sfdsdfsfsd","testTime":3,"count":3,"status":1,"createDate":1561516207000,"updateDate":1561516256000,"questions":"","type":0}
  int id;
  String specialName;
  String specialDes;
  int testTime;
  int count;
  int status;
  DateTime createDate;
  DateTime updateDate;
  String questions; //返回的是所有考题的id
  int type;

  Special(
      {this.id,
      this.specialDes,
      this.specialName,
      this.testTime,
      this.count,
      this.status,
      this.createDate,
      this.updateDate,
      this.questions,
      this.type});

  factory Special.fromJson(Map<String, dynamic> json) {
    return Special(
        id: json['id'],
        specialDes: json['specialDes'],
        specialName: json['specialName'],
        testTime: json['testTime'],
        count: json['count'],
        status: json['status'],
        createDate: json['createDate']==null?null: DateTime.fromMillisecondsSinceEpoch(json['createDate']),
        updateDate: json['updateDate']==null?null: DateTime.fromMillisecondsSinceEpoch(json['updateDate']),
        questions: json['questions'],
        type: json['type']);
  }
}

class SpecialResult {
  int pageNum;
  int pageSize;
  int size;
  int startRow;
  int endRow;
  int total;
  int pages;
  List<Special> list;

  SpecialResult(
      {this.pageSize,
      this.pageNum,
      this.size,
      this.startRow,
      this.endRow,
      this.total,
      this.pages,
      this.list});

  factory SpecialResult.fromJson(Map<String, dynamic> json) {
    var list = json['list'] as List;
    print(list);
    List<Special> specialList = list.map((i) => Special.fromJson(i)).toList();
    return SpecialResult(
      pageNum: json['pageNum'],
      pageSize: json['pageSize'],
      size: json['size'],
      startRow: json['startRow'],
      endRow: json['endRow'],
      total: json['total'],
      pages: json['pages'],
      list: specialList
    );
  }
}
