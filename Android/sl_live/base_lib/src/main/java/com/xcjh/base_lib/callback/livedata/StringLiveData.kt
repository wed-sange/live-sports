package com.xcjh.base_lib.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 作者　: zb
 * 
 * 描述　:自定义的Double类型 MutableLiveData 提供了默认值，避免取值的时候还要判空
 */
class StringLiveData : MutableLiveData<String>() {

    override fun getValue(): String {
        return super.getValue() ?: ""
    }

}