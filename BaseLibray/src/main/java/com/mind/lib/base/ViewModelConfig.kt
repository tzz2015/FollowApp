package com.mind.lib.base

import android.util.SparseArray
import androidx.annotation.Keep

/**
 * Created by rui
 *  on 2021/8/5
 */
@Keep
class ViewModelConfig(private var layout: Int) {

    companion object {
        //viewModel 与 layout 没有绑定的默认值
        const val VM_NO_BIND = -9999
    }


    private val bindingParams: SparseArray<Any> = SparseArray()


    //viewModel 绑定的 variableId
    private var vmVariableId: Int = VM_NO_BIND

    // 标题
    private var mTitleId: Int = -1

    fun getLayout(): Int {
        return layout
    }


    fun getBindingParams(): SparseArray<Any> {
        return bindingParams
    }

    fun getVmVariableId(): Int {
        return vmVariableId
    }

    fun getTitleId(): Int {
        return mTitleId
    }

    /**
     * 与layout绑定
     */
    fun bindingParam(variableId: Int, obj: Any): ViewModelConfig {
        if (bindingParams[variableId] == null) {
            bindingParams.put(variableId, obj)
        }
        return this
    }

    /**
     * 将viewModel 传给基类并与layout 绑定
     */
    fun bindViewModel(vmVariableId: Int): ViewModelConfig {
        this.vmVariableId = vmVariableId
        return this
    }

    fun bindTitle(titleId: Int): ViewModelConfig {
        this.mTitleId = titleId
        return this
    }

}