package com.example.monstruacion

import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class RangeDecorator(
    private val drawable: Drawable,
    private val fechaInicio: CalendarDay,
    private val fechaFin: CalendarDay
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day.isInRange(fechaInicio, fechaFin)
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(drawable)
    }
}