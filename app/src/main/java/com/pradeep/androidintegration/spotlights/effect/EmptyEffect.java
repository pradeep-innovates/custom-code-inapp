package com.pradeep.androidintegration.spotlights.effect;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.animation.LinearInterpolator;

import java.util.concurrent.TimeUnit;

public class EmptyEffect implements Effect {

    private final long duration;
    private final TimeInterpolator interpolator;
    private final int repeatMode;

    public EmptyEffect() {
        this(DEFAULT_DURATION, DEFAULT_INTERPOLATOR, DEFAULT_REPEAT_MODE);
    }

    public EmptyEffect(long duration, TimeInterpolator interpolator, int repeatMode) {
        this.duration = duration;
        this.interpolator = interpolator;
        this.repeatMode = repeatMode;
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

    @Override
    public void draw(Canvas canvas, PointF point, float value, Paint paint) {
        // no-op
    }

    public static final long DEFAULT_DURATION = TimeUnit.MILLISECONDS.toMillis(0);
    public static final TimeInterpolator DEFAULT_INTERPOLATOR = new LinearInterpolator();
    public static final int DEFAULT_REPEAT_MODE = ObjectAnimator.REVERSE;
}
