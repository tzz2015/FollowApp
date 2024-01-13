package com.lyflovelyy.followhelper.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.lyflovelyy.followhelper.R
import com.lyflovelyy.followhelper.databinding.ItemGuideCopyBinding
import com.mind.data.data.model.praise.PraiseVideoModel

/**
 * @Author      : liuyufei
 * @Date        : on 2023-07-29 18:33.
 * @Description :
 */
class GuideCopyAdapter :
    BaseQuickAdapter<PraiseVideoModel, BaseDataBindingHolder<ItemGuideCopyBinding>>(
        R.layout.item_guide_copy
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemGuideCopyBinding>,
        item: PraiseVideoModel
    ) {
        holder.dataBinding?.praiseVideoModel = item
        addChildClickViewIds(R.id.tv_copy)
        bindViewClickListener(holder, 0)
    }

}