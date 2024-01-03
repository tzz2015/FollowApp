package com.lyflovelyy.followhelper.viewmodel

import com.mind.lib.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Author      : liuyufei
 * @Date        : on 2024-01-02 21:36.
 * @Description :
 */
@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    fun getText(): String {
        return "哈哈哈"
    }
}