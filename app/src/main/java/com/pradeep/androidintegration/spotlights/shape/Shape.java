package com.pradeep.androidintegration.spotlights.shape;

import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public interface Shape {
    long getDuration();
    TimeInterpolator getInterpolator();
    void draw(Canvas canvas, PointF point, float value, Paint paint);
}
