package com.huburt.library

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by hubert
 *
 * Created on 2017/10/31.
 */
internal class InitializationCheck<T> : ReadWriteProperty<Any?, T> {
    private var value: T? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("not initialized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}