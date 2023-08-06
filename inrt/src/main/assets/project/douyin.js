app.launchApp("抖音");
auto("fast");
threads.start(function () {
    console.show();
    console.setTitle("中文", "#ff11ee00", 30);
    console.setCanInput(false);
});
var token = readFile("token.txt")
var accountText = readFile("account.txt")
var editText = className("EditText").findOne()
if (editText != null) {
    console.log("找到输入框")
    console.log(token)
    console.log(accountText)
    toFollow()
} else {
    console.log("未找打输入框，请打开搜索界面")
}

function toFollow(){
   try {
       var jsonArray = JSON.parse(accountText);
       for (var i = 0; i < jsonArray.length; i++) {
           var account  = jsonArray[i];
           check(account)
       }
   } catch (e) {
       log("JSON 解析出错：" + e);
   }
}

function check(account){

}


function readFile(name){
   var path = "/sdcard/follow/"+name
   var file = open(path)
   var text = file.read()
   file.close()
   return text
}








