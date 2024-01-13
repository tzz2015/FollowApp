package com.lyflovelyy.followhelper.utils


import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.lyflovelyy.followhelper.R
import com.mind.lib.util.DensityUtil
import java.io.File


object CommonBA {

    /**
     * item中图片绑定
     * imageUrl:图片地址
     * placeHolder：占位图
     * errorHolder：错误加载图
     * isCircle：是否圆角
     * corners: 矩形角度
     */
    @JvmStatic
    @BindingAdapter(
        value = [
            "imageUrl",
            "isCircle",
            "corners",
            "placeHolder",
            "errorHolder"],
        requireAll = false
    )
    fun imageLoadUrl(
        imageView: ImageView,
        imageUrl: String?,
        isCircle: Boolean?,
        corners: Int?,
        placeHolder: Drawable?,
        errorHolder: Drawable?
    ) {

        val builder: RequestBuilder<Drawable> = Glide.with(imageView.context).load(imageUrl)
        loadImage(isCircle, corners, imageView, builder, placeHolder, errorHolder)

    }


    @JvmStatic
    @BindingAdapter(
        value = [
            "imageFile",
            "placeHolder",
            "errorHolder",
            "isCircle",
            "corners"], requireAll = false
    )
    fun imgageLoadFile(
        imageView: ImageView,
        imageFile: File,
        placeHolder: Drawable?,
        errorHolder: Drawable?,
        isCircle: Boolean?,
        corners: Int?
    ) {
        val builder: RequestBuilder<Drawable> = Glide.with(imageView).load(imageFile)
        loadImage(isCircle, corners, imageView, builder, placeHolder, errorHolder)
    }

    @BindingAdapter(
        value = [
            "resIdImage",
            "placeHolder",
            "errorHolder",
            "isCircle",
            "corners"], requireAll = false
    )
    fun imgageLoadRes(
        imageView: ImageView,
        resIdImage: Int,
        placeHolder: Drawable?,
        errorHolder: Drawable?,
        isCircle: Boolean?,
        corners: Int?
    ) {
        val builder: RequestBuilder<Drawable> = Glide.with(imageView).load(resIdImage)
        loadImage(isCircle, corners, imageView, builder, placeHolder, errorHolder)
    }

    /**
     * 加载图片
     */
    @SuppressLint("CheckResult")
    private fun loadImage(
        isCircle: Boolean?,
        corners: Int?,
        imageView: ImageView,
        builder: RequestBuilder<Drawable>,
        placeHolder: Drawable?,
        errorHolder: Drawable?
    ) {
        var options = if (isCircle == true) {
            RequestOptions.circleCropTransform()
        } else {

            val roundedCorners =
                RoundedCorners(DensityUtil.dp2px(corners?.toFloat() ?: 1f, imageView.context))
            RequestOptions.bitmapTransform(roundedCorners)
        }

        val layoutParams = imageView.layoutParams
        val width = layoutParams?.width ?: 0
        val height = layoutParams?.height ?: 0
        if (width > 0 && height > 0) {
            builder.override(width, height)
        }
        if (errorHolder != null) {
            builder
                .apply(options)
                .placeholder(R.color.white)
                .error(errorHolder)
                .into(imageView)
        } else {
            builder.apply(options).placeholder(R.color.white).error(R.color.color_ededed)
                .into(imageView)
        }

    }

    @JvmStatic
    @BindingAdapter(
        value = [
            "imageRes",
            "isCircle",
            "corners",
            "placeHolder",
            "errorHolder"],
        requireAll = false
    )
    fun imageLoadRes(
        imageView: ImageView,
        imageUrl: Int?,
        isCircle: Boolean?,
        corners: Int?,
        placeHolder: Drawable?,
        errorHolder: Drawable?

    ) {
        imageView.setBackgroundResource(imageUrl ?: R.color.color_999)
    }

    /**
     * SwipeRefreshLayout
     */
    @JvmStatic
    @BindingAdapter(
        value = [
            "isRefreshing"],
        requireAll = false
    )
    fun swipeRefresh(
        swipeRefresh: SwipeRefreshLayout,
        isRefreshing: Boolean = false
    ) {
        if (swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = false
        }

    }

    @JvmStatic
    @BindingAdapter("loadGifDrawable")
    fun loadGifDrawable(imageView: ImageView, url: String) {
        imageView.context.resources
        Glide.with(imageView.context)
            .asGif()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)

    }


    @JvmStatic
    @BindingAdapter("isVisible")
    fun setVisibility(view: View?, isVisible: Boolean) {
        view?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
    @JvmStatic
    @InverseBindingAdapter(attribute = "isVisible", event = "isVisibleAttrChanged")
    fun getVisibility(view: View): Boolean? {
        return view.visibility == View.VISIBLE
    }
    @JvmStatic
    @BindingAdapter("isVisibleAttrChanged")
    fun setListener(view: View?, listener: InverseBindingListener?) {
        // 不需要实现任何逻辑，只要添加监听器即可
    }


}