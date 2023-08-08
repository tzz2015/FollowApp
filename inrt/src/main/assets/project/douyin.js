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
    // console.log(token)
    // console.log(accountText)
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
            var followAccount = jsonArray[i];
            if (check(followAccount)) {
                doFollow(followAccount)
            }
        }
    } catch (e) {
        log("JSON 解析出错：" + e);
    }
}

function doFollow(followAccount) {
    editText.setText(followAccount.account);
    var searchBtn = desc("搜索").findOne()
    if (searchBtn != null) {
        console.log("找到搜索按钮")
        clickView(searchBtn)
        console.log("点击搜索用户:" + followAccount.account)
        clickFollowBtn(followAccount)
    } else {
        console.log("找不到搜索按钮")
    }

}

function clickFollowBtn(followAccount) {
    var followBtn = findFollowBtn()
    if (followBtn != null) {
        clickView(followBtn)
        sleep(500)
        if (addFollow(followAccount)) {
            console.log("关注用户:" + followAccount.account)
        } else {
            console.warn("关注失败")
            clickView(followBtn)
        }
    } else {
        console.warn("无效用户:" + followAccount.account)
    }

}

function findFollowBtn() {
    sleep(2000)
    // 查找关注按钮
    var followList = desc("关注按钮").find();
    // 已经关注的按钮
    var followedList = desc("已关注按钮").find();
    console.log("找到关注按钮：" + followList.length + "--已关注：" + followedList.length)
    if (followList.length == 0 && followedList.length == 0) {
        sleep(1000)
        followList = desc("关注按钮").find();
    }
    if (followList.length > 0) {
        return followList[0]
    }
    return null
}

/**
 * 检查当前账户是否可以被关注
 * @param account
 */
function check(followAccount) {
    var url = host + "followAccount/followCheck";
    var data = {
        "userId": followAccount.userId,
        "followType": followAccount.followType
    };
    var resp = postRequst(url, data)
    if (resp == true) {
        return true
    } else {
        return false
    }
}

/**
 * 添加关注记录
 * @param account
 * @returns {boolean}
 */
function addFollow(followAccount) {
    var url = host + "follow/add";
    var data = {
        "followUserId": followAccount.userId,
        "followType": followAccount.followType,
        "followAccount": followAccount.account
    };
    var resp = postRequst(url, data)
    if (resp != null) {
        return true
    } else {
        return false
    }
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
    var res = http.postJson(url, data, options)
    var result = res.body.string();
    var resultObject = JSON.parse(result);
    if (resultObject.code == 200) {
        return resultObject.data
    } else {
        toast(resultObject.message)
        return null
    }

}

function clickView(view) {
    if (view == null) {
        console.log("view未空，不能点击")
        return
    }
    var bounds = view.bounds()
    // console.log(view.bounds())
    var centerX = bounds.left + (bounds.right - bounds.left) / 2
    var centerY = bounds.top + (bounds.bottom - bounds.top) / 2
    // console.log("搜索按钮中心：" + centerX + "," + centerY)
    click(centerX, centerY)
}








