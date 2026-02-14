package com.example.widgetmaker

import android.content.Context
import android.net.Uri

class WidgetRepository(context: Context) {
    private val prefs = context.getSharedPreferences("widgets", Context.MODE_PRIVATE)

    fun saveImageUri(appWidgetId: Int, uri: Uri) {
        prefs.edit().putString(key(appWidgetId), uri.toString()).apply()
    }

    fun getImageUri(appWidgetId: Int): Uri? {
        val value = prefs.getString(key(appWidgetId), null) ?: return null
        return Uri.parse(value)
    }

    fun remove(appWidgetId: Int) {
        prefs.edit().remove(key(appWidgetId)).apply()
    }

    private fun key(appWidgetId: Int): String = "widget_image_$appWidgetId"
}
