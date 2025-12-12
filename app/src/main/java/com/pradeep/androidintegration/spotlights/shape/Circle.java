package com.pradeep.androidintegration.spotlights.shape;

import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.animation.DecelerateInterpolator;

import java.util.concurrent.TimeUnit;

public class Circle implements Shape {

    private final float radius;
    private final long duration;
    private final TimeInterpolator interpolator;

    public Circle(float radius) {
        this(radius, DEFAULT_DURATION, DEFAULT_INTERPOLATOR);
    }

    public Circle(float radius, long duration, TimeInterpolator interpolator) {
        this.radius = radius;
        this.duration = duration;
        this.interpolator = interpolator;
    }

    @Override
    public void draw(Canvas canvas, PointF point, float value, Paint paint) {
        canvas.drawCircle(point.x, point.y, value * radius, paint);
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public TimeInterpolator getInterpolator() {
        return interpolator;
    }

    public static final long DEFAULT_DURATION = TimeUnit.MILLISECONDS.toMillis(500);
    public static final TimeInterpolator DEFAULT_INTERPOLATOR = new DecelerateInterpolator(2f);
}
