package com.example.frequency.preferences

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


typealias KPToString = (KProperty<*>) -> String

fun SharedPreferences.string(
    defValue: String? = "",
    key: KPToString = KProperty<*>::name
): ReadWriteProperty<Any, String?> = object : ReadWriteProperty<Any, String?> {

    override fun getValue(
        thisRef: Any,
        property: KProperty<*>
    ) = getString(key(property),defValue) ?: defValue

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: String?
    ) = edit().putString(key(property), value).apply()

}

fun SharedPreferences.int(
    defValue: Int = 0,
    key: KPToString = KProperty<*>::name
): ReadWriteProperty<Any, Int> =
    object : ReadWriteProperty<Any, Int> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getInt(key(property), defValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: Int
        ) = edit().putInt(key(property), value).apply()
    }

fun SharedPreferences.boolean(
    defValue: Boolean = false,
    key: KPToString = KProperty<*>::name
): ReadWriteProperty<Any, Boolean> =
    object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(
            thisRef: Any,
            property: KProperty<*>
        ) = getBoolean(key(property), defValue)

        override fun setValue(
            thisRef: Any,
            property: KProperty<*>,
            value: Boolean
        ) = edit().putBoolean(key(property), value).apply()
    }

class DelegateUtil