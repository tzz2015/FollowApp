package com.lyflovelyy.followhelper.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.lyflovelyy.followhelper.R
import com.lyflovelyy.followhelper.databinding.ItemPraiseVideoBinding
import com.mind.data.data.model.praise.PraiseVideoModel

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