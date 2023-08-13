package com.mind.lib.base

import androidx.annotation.Keep
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

@Keep
class ViewModelEvent <T> : MutableLiveData<T>() {

    private val mPending = AtomicBoolean(false)
    @Keep
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        super.observe(owner) { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }
    @Keep
    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }
    @Keep
    @MainThread
    fun call() {
        value = null
    }

}