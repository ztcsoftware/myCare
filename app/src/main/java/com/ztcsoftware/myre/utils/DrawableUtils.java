package com.ztcsoftware.myre.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.applandeo.materialcalendarview.utils.DayColorsUtils;
import com.ztcsoftware.myre.R;

public final class DrawableUtils {

    private DrawableUtils() {
    }

    public static Drawable getDayCircle(Context context, @ColorRes int borderColor, @ColorRes int fillColor) {
        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.calendar_day_background);
        drawable.setStroke(6, DayColorsUtils.parseColor(context, borderColor));
        drawable.setColor(DayColorsUtils.parseColor(context, fillColor));
        return drawable;
    }
}

