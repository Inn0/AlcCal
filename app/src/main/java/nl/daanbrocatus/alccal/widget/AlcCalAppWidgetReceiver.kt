package nl.daanbrocatus.alccal.widget

import androidx.glance.appwidget.GlanceAppWidgetReceiver

class AlcCalAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = AlcCalAppWidget()
}