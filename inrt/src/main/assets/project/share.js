app.launchApp("抖音");
auto("fast");
threads.start(function () {
    console.show();
    console.setTitle("Log", "#ff11ee00", 30);
    console.setCanInput(false);
});
sleep(1000);
const width = device.width
const height = device.height
var isCanRun = true;
var shareCount = 0;
while (isCanRun) {
    try {
        var shareBtn = desc("分享").findOne(1000);
        if (shareBtn) {
            shareCount += 1;
            clickView(shareBtn)
            sleep(200);
            console.log("已经分享：" + shareCount)
            var bounds = shareBtn.bounds();
            var centerY = bounds.top + (bounds.bottom - bounds.top) / 2;
            if (centerY > height - 100) {
                console.log("底部：" + centerY);
                sleep(100);
                swipe(width / 2, height / 4 * 3, width / 2, 200, 500);
                sleep(1000);
            }
        }
        shareBtn = desc("分享").findOne(1000);
        if (shareBtn == null) {
            swipe(width / 2, height / 4 * 3, width / 2, 200, 500);
            sleep(1000);
        }
        shareBtn = desc("分享").findOne(1000);
        if (shareBtn == null) {
            isCanRun = false;
        }
    } catch (e) {
        console.log("发生错误：" + e);
        isCanRun = false;
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

// sleep(1000);
// console.clear();
// console.hide();
// app.launchPackage("org.autojs.autoxjs.follow");
exit();
