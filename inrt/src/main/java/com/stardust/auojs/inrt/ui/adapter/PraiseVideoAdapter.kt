package com.stardust.auojs.inrt.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mind.data.data.model.praise.PraiseVideoModel
import org.autojs.autoxjs.inrt.R
import org.autojs.autoxjs.inrt.databinding.ItemPraiseVideoBinding

/**
 * @Author      : liuyufei
 * @Date        : on 2023-07-29 18:33.
 * @Description :
 */
class PraiseVideoAdapter :
    BaseQuickAdapter<PraiseVideoModel, BaseDataBindingHolder<ItemPraiseVideoBinding>>(
        R.layout.item_praise_video
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemPraiseVideoBinding>,
        item: PraiseVideoModel
    ) {
        holder.dataBinding?.praiseVideoModel = item
        addChildClickViewIds(R.id.tv_delete)
        addChildClickViewIds(R.id.tv_edit)
        addChildClickViewIds(R.id.tv_check)
        bindViewClickListener(holder, 0)
    }

}