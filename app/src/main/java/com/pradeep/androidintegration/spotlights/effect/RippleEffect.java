package com.pradeep.androidintegration.spotlights.effect;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.ColorInt;

import java.util.concurrent.TimeUnit;

public class RippleEffect implements Effect {

    private final float offset;
    private final float radius;
    private final int color;
    private final long duration;
    private final TimeInterpolator interpolator;
    private final int repeatMode;

    public RippleEffect(float offset, float radius, @ColorInt int color) {
        this(offset, radius, color, DEFAULT_DURATION, DEFAULT_INTERPOLATOR, DEFAULT_REPEAT_MODE);
    }

    public RippleEffect(float offset, float radius, @ColorInt int color, long duration, TimeInterpolator interpolator, int repeatMode) {
        if (offset >= radius) {
            throw new IllegalArgumentException("holeRadius should be bigger than rippleRadius.");
        }
        this.offset = offset;
        this.radius = radius;
        this.color = color;
        this.duration = duration;
        this.interpolator = interpolator;
        this.repeatMode = repeatMode;
    }

    @Override
    public void draw(Canvas canvas, PointF point, float value, Paint paint) {
        float currentRadius = offset + ((radius - offset) * value);
        int alpha = 255 - (int)(value * 255);
        paint.setColor(color);
        paint.setAlpha(alpha);
        canvas.drawCircle(point.x, point.y, currentRadius, paint);
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public TimeInterpolator getInterpolator() {
        return interpolator;
    }

    @Override
    public int getRepeatMode() {
        return repeatMode;
    }

    public static final long DEFAULT_DURATION = TimeUnit.MILLISECONDS.toMillis(1000);
    public static final TimeInterpolator DEFAULT_INTERPOLATOR = new DecelerateInterpolator(1f);
    public static final int DEFAULT_REPEAT_MODE = ObjectAnimator.REVERSE;
}
