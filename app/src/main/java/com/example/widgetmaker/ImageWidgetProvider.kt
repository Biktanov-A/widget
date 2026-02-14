package com.example.widgetmaker

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class ImageWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        val repository = WidgetRepository(context)
        appWidgetIds.forEach(repository::remove)
    }

    companion object {
        fun refreshAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val component = ComponentName(context, ImageWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(component)
            ids.forEach { id -> updateWidget(context, appWidgetManager, id) }
        }

        fun updateWidget(context: Context, manager: AppWidgetManager, appWidgetId: Int) {
            val repository = WidgetRepository(context)
            val views = RemoteViews(context.packageName, R.layout.widget_image)

            repository.getImageUri(appWidgetId)?.let { uri ->
                views.setImageViewUri(R.id.widgetImage, uri)
            } ?: run {
                views.setImageViewResource(R.id.widgetImage, R.drawable.ic_widget_placeholder)
            }

            val configureIntent = Intent(context, WidgetConfigureActivity::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }

            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            val pendingIntent = PendingIntent.getActivity(context, appWidgetId, configureIntent, flags)
            views.setOnClickPendingIntent(R.id.widgetImage, pendingIntent)

            manager.updateAppWidget(appWidgetId, views)
        }
    }
}
