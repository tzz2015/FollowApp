app.launchApp("抖音");
auto("fast");
threads.start(function () {
    console.show();
    console.setTitle("中文", "#ff11ee00", 30);
    console.setCanInput(false);
});
var token = readFile("token.txt")
var accountText = readFile("account.txt")
var host = readFile("host.txt")
var editText = className("EditText").findOne()
if (editText != null) {
    console.log("找到输入框")
    console.log(token)
    console.log(accountText)
    toFollow()
} else {
    console.log("未找打输入框，请打开搜索界面")
}

/**
 * 去关注
 */
function toFollow() {
    try {
        var jsonArray = JSON.parse(accountText);
        for (var i = 0; i < jsonArray.length; i++) {
            var account = jsonArray[i];
            check(account)
        }
    } catch (e) {
        log("JSON 解析出错：" + e);
    }
}

/**
 * 检查当前账户是否可以被关注
 * @param account
 */
function check(account) {
    var url = host + "followAccount/followCheck";
    var data = JSON.stringify({
        "userId": account.userId,
        "followType": account.followType
    });
    var resp = postRequst(url, data)

}

/**
 * 读取本地文件
 * @param name
 * @returns {string}
 */
function readFile(name) {
    var path = "/sdcard/follow/" + name
    var file = open(path)
    var text = file.read().trim()
    file.close()
    return text
}

function postRequst(url, data) {
    // 定义请求头
    var headers = {
        "Content-Type": "application/json", // 设置请求头为 JSON 类型
        "token": token
    };

    // 配置请求选项
    var options = {
        url: url,
        method: "POST",
        headers: headers,
        body: data
    };
    // 发送POST请求
    var res = http.post(url,data, options)
    var result = res.body.string();
    console.log(result);
    return result

}








