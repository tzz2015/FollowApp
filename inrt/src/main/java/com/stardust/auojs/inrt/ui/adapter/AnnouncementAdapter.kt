package com.stardust.auojs.inrt.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mind.data.data.model.AnnouncementModel
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.ItemAnnouncementBinding

/**
 * @Author      : liuyufei
 * @Date        : on 2023-07-29 18:33.
 * @Description :
 */
class AnnouncementAdapter :
    BaseQuickAdapter<AnnouncementModel, BaseDataBindingHolder<ItemAnnouncementBinding>>(
        R.layout.item_announcement
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemAnnouncementBinding>,
        item: AnnouncementModel
    ) {
        holder.dataBinding?.anModel = item
    }

}