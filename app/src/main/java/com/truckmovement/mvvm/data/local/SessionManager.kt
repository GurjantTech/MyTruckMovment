package com.truckmovement.mvvm.data.local

import android.content.SharedPreferences


class SessionManager(var mSharedPrefrences: SharedPreferences) {

    val token = "token"
    val userId = "userId"
    val name = "name"
    val username="username"
    val password="password"
    val laguagePef="laguagePef"
    var usermail="usermail"

    fun setValue(key: String, value: Any?) {
        when (value) {
            is String? -> edit({ it.putString(key, value) })
            is Int -> edit({ it.putInt(key, value) })
            is Boolean -> edit({ it.putBoolean(key, value) })
            is Float -> edit({ it.putFloat(key, value) })
            is Long -> edit({ it.putLong(key, value) })
            else -> throw UnsupportedOperationException("Not Yet Implemented")
        }

    }

    inline fun edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = mSharedPrefrences.edit()
        operation(editor)
        editor.apply()
    }

    inline fun <reified T : Any?> getValue(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> mSharedPrefrences.getString(key, defaultValue as? String) as T?
            Int::class -> mSharedPrefrences.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> mSharedPrefrences.getBoolean(
                key, defaultValue as? Boolean
                    ?: false
            ) as T?
            Float::class -> mSharedPrefrences.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> mSharedPrefrences.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun logOutUser() {
        val editor = mSharedPrefrences.edit()
        editor.clear()
        editor.commit()
    }
}