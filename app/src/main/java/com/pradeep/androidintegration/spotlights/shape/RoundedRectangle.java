package com.pradeep.androidintegration.spotlights.shape;

import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.animation.DecelerateInterpolator;

import java.util.concurrent.TimeUnit;

public class RoundedRectangle implements Shape {

    private final float height;
    private final float width;
    private final float radius;
    private final long duration;
    private final TimeInterpolator interpolator;

    public RoundedRectangle(float height, float width, float radius) {
        this(height, width, radius, DEFAULT_DURATION, DEFAULT_INTERPOLATOR);
    }

    public RoundedRectangle(float height, float width, float radius, long duration, TimeInterpolator interpolator) {
        this.height = height;
        this.width = width;
        this.radius = radius;
        this.duration = duration;
        this.interpolator = interpolator;
    }

    @Override
    public void draw(Canvas canvas, PointF point, float value, Paint paint) {
        float halfWidth = width / 2 * value;
        float halfHeight = height / 2 * value;
        float left = point.x - halfWidth;
        float top = point.y - halfHeight;
        float right = point.x + halfWidth;
        float bottom = point.y + halfHeight;
        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rect, radius, radius, paint);
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
