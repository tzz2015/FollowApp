app.launchApp("抖音");
auto("fast");
threads.start(function () {
    console.show();
    console.setTitle("中文", "#ff11ee00", 30);
    console.setCanInput(false);
});

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

    return result;
}

var width = device.width
var height = device.height
var fruits = ["关注我带你互粉。", "需要互粉工具找我。", "还要一个个去互粉吗，关注我更便捷。", "用工具互粉找我",
    "看我主页有惊喜。", "别再一个个互粉了，我有工具。", "关注互粉助手，提高效率。", "需要涨粉找我啊。",
    "涨一千不是梦，涨一万不上线", "互粉互助","快来下载互粉助手吧。","互粉助手祝你上青云","分享个好工具给你",
    "加入我的加速计划","别再为一千分跑断腿了","大学生互赞互助","主打就是真诚，来关一个","一关就是一辈子，主打真诚",
    "女大学生求关注","人很话不多，快来使用互粉助手","永不取关，快来快来","一起互粉吧","互关必回"];
// 将一段文本插入另一段文本中间
function insertText(originalText, insertText, position) {
    return originalText.slice(0, position) + insertText + originalText.slice(position);
}
var maxDo = 100
while (maxDo > 0) {
    maxDo -= 1
    var index = myrandom(0, fruits.length - 1)
    var inputText = generateRandomString(5) + fruits[index] + generateRandomString(5)
    var inputText2 = insertText(inputText,generateRandomString(3),10)
    doInput(inputText2)
}

doInput("关注我带你互粉")

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
        sleep(500)
        var searchBtn = text("发送").findOne();
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
     console.log("结束")
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
