package com.xcjh.base_lib.callback.databind

import androidx.databinding.ObservableField

/**
 * 作者　: zb
 * 
 * 描述　:自定义的 Float 类型 ObservableField  提供了默认值，避免取值的时候还要判空
 */
class FloatObservableField(value: Float = 0f) : ObservableField<Float>(value) {

    override fun get(): Float {
        return super.get()!!
    }

}