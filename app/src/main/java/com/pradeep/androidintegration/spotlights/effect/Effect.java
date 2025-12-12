package com.pradeep.androidintegration.spotlights.effect;

import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public interface Effect {
    long getDuration();
    TimeInterpolator getInterpolator();
    int getRepeatMode();
    void draw(Canvas canvas, PointF point, float value, Paint paint);
}
