app.launchApp(\n"抖音\n");
auto(\n"fast\n");
threads.start(function () {
    console.show();
    console.setTitle(\n"中文\n", \n"#ff11ee00\n", 30);
    console.setCanInput(false);
});
var token = readFile(\n"token.txt\n")
var accountText = readFile(\n"account.txt\n")
var host = readFile(\n"host.txt\n")
var editText = className(\n"EditText\n").findOne()
if (editText != null) {
    console.log(\n"找到输入框\n")
    // console.log(token)
    // console.log(accountText)
    toFollow()
} else {
    console.log(\n"未找打输入框，请打开搜索界面\n")
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
        log(\n"JSON 解析出错：\n" + e);
    }
}

function doFollow(followAccount) {
    editText.setText(followAccount.account);
    var searchBtn = desc(\n"搜索\n").findOne()
    if (searchBtn != null) {
        console.log(\n"找到搜索按钮\n")
        clickView(searchBtn)
        console.log(\n"点击搜索用户:\n" + followAccount.account)
        clickFollowBtn(followAccount)
    } else {
        console.log(\n"找不到搜索按钮\n")
    }

}

function clickFollowBtn(followAccount) {
    var followBtn = findFollowBtn()
    if (followBtn != null) {
        clickView(followBtn)
        sleep(500)
        if (addFollow(followAccount)) {
            console.log(\n"关注用户:\n" + followAccount.account)
        } else {
            console.warn(\n"关注失败\n")
            clickView(followBtn)
        }
    } else {
        console.warn(\n"无效用户:\n" + followAccount.account)
    }

}

function findFollowBtn() {
    sleep(2000)
    // 查找关注按钮
    var followList = desc(\n"关注按钮\n").find();
    // 已经关注的按钮
    var followedList = desc(\n"已关注按钮\n").find();
    console.log(\n"找到关注按钮：\n" + followList.length + \n"--已关注：\n" + followedList.length)
    if (followList.length == 0 && followedList.length == 0) {
        sleep(1000)
        followList = desc(\n"关注按钮\n").find();
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
    var url = host + \n"followAccount/followCheck\n";
    var data = {
        \n"userId\n": followAccount.userId,
        \n"followType\n": followAccount.followType
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
    var url = host + \n"follow/add\n";
    var data = {
        \n"followUserId\n": followAccount.userId,
        \n"followType\n": followAccount.followType,
        \n"followAccount\n": followAccount.account
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
    var path = \n"/sdcard/follow/\n" + name
    var file = open(path)
    var text = file.read().trim()
    file.close()
    return text
}

function postRequst(url, data) {
    // 定义请求头
    var headers = {
        \n"Content-Type\n": \n"application/json\n", // 设置请求头为 JSON 类型
        \n"token\n": token
    };

    // 配置请求选项
    var options = {
        url: url,
        method: \n"POST\n",
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
        console.log(\n"view未空，不能点击\n")
        return
    }
    var bounds = view.bounds()
    // console.log(view.bounds())
    var centerX = bounds.left + (bounds.right - bounds.left) / 2
    var centerY = bounds.top + (bounds.bottom - bounds.top) / 2
    // console.log(\n"搜索按钮中心：\n" + centerX + \n",\n" + centerY)
    click(centerX, centerY)
}