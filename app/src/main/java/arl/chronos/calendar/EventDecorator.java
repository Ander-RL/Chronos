package arl.chronos.calendar;

import android.content.res.Resources;
import android.text.style.BulletSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import arl.chronos.R;

public class EventDecorator implements DayViewDecorator {
    private HashSet<CalendarDay> days = new HashSet<>();
    private final View view;

    public EventDecorator(View view, HashSet<CalendarDay> days) {
        this.view = view;
        this.days = days;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return days.contains(day);
    }

    @Override
    public void decorate(DayViewFacade viewDay) {
        viewDay.addSpan(new DotSpan(10, ResourcesCompat.getColor(view.getResources(), R.color.blue_500, null)));
    }

}
