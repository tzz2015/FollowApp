app.launchApp("抖音");
auto("fast");
threads.start(function () {
    console.show();
    console.setTitle("中文", "#ff11ee00", 30);
    console.setCanInput(false);
});
var editText = className("EditText").findOne()
if (editText != null) {
    console.log("找到输入框")
    preFollow(editText)
} else {
    console.log("未找打输入框，请打开搜索界面")
}

function preFollow(editText) {
    // 网络请求获取关注列表
    var userList = ["20670963758", "46293349953"]
    for (var i = 0; i < userList.length; i++) {
        doFollow(editText, userList[i])
    }

}

function doFollow(editText, userId) {
    editText.setText(userId);
    var searchBtn = desc("搜索").findOne()
    if (searchBtn != null) {
        console.log("找到搜索按钮")
        clickView(searchBtn)
        console.log("点击搜索用户:" + userId)
        clickFollowBtn(userId)
    } else {
        console.log("找不到搜索按钮")
    }
}

function clickFollowBtn(userId) {
    sleep(2000)
    // 查找关注按钮
    var followList = desc("关注按钮").find();
    // 已经关注的按钮
    var followedList = desc("已关注按钮").find();
    console.log("找到关注按钮：" + followList.length + "--已关注：" + followedList.length)
    if (followList.length == 0 && followedList.length == 0) {
        sleep(2000)
        followList = desc("关注按钮").find();
    }
    if (followList.length > 0) {
        var followBtn = followList[0]
        clickView(followBtn)
        sleep(1000)
        console.log("关注用户:" + userId)
    }
    if (followedList.length > 0) {
        console.warn("已经关注用户:" + userId)
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



