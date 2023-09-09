app.launchApp("抖音");
auto("fast");
threads.start(function () {
    console.show();
    console.setTitle("中文", "#ff11ee00", 30);
    console.setCanInput(false);
});
toRequestPermiss()


function toRequestPermiss() {
    //申请权限并自动点击
    threads.start(function () {
        sleep(1000)
        var beginBtn = classNameContains("Button").textContains("立即开始").findOne(1000);
        if (beginBtn) {
            console.log("找到立即开始按钮");
            clickView(beginBtn)
        } else {
            console.log("没有找到立即开始按钮");
        }
    });
    if (!requestScreenCapture()) {
        console.log("请求截图失败");
        exit();
    } else {
        console.log("请求截图成功");
    }
}

function myrandom(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

// 生成一个随机字符串，包含数字和字母
function generateRandomString(length) {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789！@#￥%……&*()_+-=~`';
    let result = '';

    for (let i = 0; i < length; i++) {
        var randomIndex = Math.floor(Math.random() * characters.length);
        result += characters.charAt(randomIndex);
    }

    return " " + result + " ";
}

var width = device.width
var height = device.height
var fruits = ["现在出了个互粉互赞工具：https://s1.link-nemo.com/bot/lyf/inrt-release.apk。", "需要互粉工具找我。https://s1.link-nemo.com/bot/lyf/inrt-release.apk。", "用工具互粉找我https://s1.link-nemo.com/bot/lyf/inrt-release.apk。",
    "推荐一个互粉互赞工具。https://s1.link-nemo.com/bot/lyf/inrt-release.apk。", "关注互粉助手，提高效率。https://s1.link-nemo.com/bot/lyf/inrt-release.apk。", "需要涨粉找我啊。https://s1.link-nemo.com/bot/lyf/inrt-release.apk。",
    "使用互粉互赞工具，涨一千不是梦，涨一万不上线：https://s1.link-nemo.com/bot/lyf/inrt-release.apk。", "互粉互助：https://s1.link-nemo.com/bot/lyf/inrt-release.apk。", "快来下载互粉助手吧。https://s1.link-nemo.com/bot/lyf/inrt-release.apk。",
    "快来下载互粉互赞工具：https://s1.link-nemo.com/bot/lyf/inrt-release.apk。", "别再为一千分跑断腿了来下载互粉互赞工具：https://s1.link-nemo.com/bot/lyf/inrt-release.apk。", "一关就是一辈子，主打真诚https://s1.link-nemo.com/bot/lyf/inrt-release.apk。",
    "你还不知道有互粉互赞工具吧：https://s1.link-nemo.com/bot/lyf/inrt-release.apk。", "人很话不多，快来使用互粉助手：https://s1.link-nemo.com/bot/lyf/inrt-release.apk。", "永不取关，快来快来https://s1.link-nemo.com/bot/lyf/inrt-release.apk。",
    "分享个好工具给你：https://s1.link-nemo.com/bot/lyf/inrt-release.apk。", "互关必回https://s1.link-nemo.com/bot/lyf/inrt-release.apk。"];

// 将一段文本插入另一段文本中间
function insertText(originalText, insertText, position) {
    return originalText.slice(0, position) + insertText + originalText.slice(position);
}

var maxDo = 100
while (maxDo > 0) {
    if (findLikeView("#E7E9E6")) {
        maxDo -= 1
        var index = myrandom(0, fruits.length - 1)
        var inputText = generateRandomString(5) + fruits[index] + generateRandomString(5)
        var inputText2 = insertText(inputText, generateRandomString(3), 10)
        doInput(inputText2)
    } else {
        console.log("直播")
        swipe(width / 2, height / 2, width / 2, 200, 500)
        sleep(200);
    }

}

function findLikeView(color) {
    try {
        //截图
        var img = captureScreen();
        sleep(500);
        // var color = images.pixel(img, x, y);
        // console.log("点前xy颜色" + colors.toString(color))
        // 未点赞颜色#EBEBEB  点赞颜色#FB4872
        if (findColor(img, color, {
            region: [width - 100, height -500, 100, 200],
            threshold: 4
        })) {
            console.log("找到相应颜色")
            return true
        }
        console.log("没有找到相应颜色")
    } catch (e) {
        console.log("截图异常：" + e)
        // exit();
    }
    return false
}


function doInput(inptuText) {
    sleep(1000)
    console.log("width:" + width + "--height:" + height)
    var cmw = width - 100
    var cmh = height - 850
    // 评论
    click(cmw, cmh)
    console.log("cmw:" + cmw + "--height:" + cmh)
    sleep(800)
    click(width / 2, height - 50)
    console.log("点击输入框")
    sleep(800)
    var editText = className("EditText").findOne(1000);
    console.log("查找输入框：" + (editText != null))
    if (editText != null) {
        sleep(500)
        editText.setText(inptuText)
        console.log("设置文本")
        sleep(200)
        var searchBtn = text("发送").findOne(1000);
        if (searchBtn != null) {
            sleep(200)
            clickView(searchBtn)
            click(width - 200, 500)
            sleep(200)
            click(width - 200, 500)
            sleep(200)
            swipe(width / 2, height / 2, width / 2, 200, 500)
            sleep(500)
        } else {
            console.log("没有找到发送按钮")
        }
    }
}


function clickView(view) {
    if (view == null) {
        console.log("view未空，不能点击")
        return
    }
    var bounds = view.bounds();
    var centerX = bounds.left + (bounds.right - bounds.left) / 2;
    var centerY = bounds.top + (bounds.bottom - bounds.top) / 2;
    click(centerX, centerY);
}
