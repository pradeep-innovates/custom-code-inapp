package com.pradeep.androidintegration.spotlights;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;

public class SpotlightView extends FrameLayout {

    private final Paint backgroundPaint;
    private final Paint shapePaint;
    private final Paint effectPaint;

    private final ValueAnimator.AnimatorUpdateListener invalidator;

    private ValueAnimator shapeAnimator;
    private ValueAnimator effectAnimator;

    private Target target;

    public SpotlightView(Context context, AttributeSet attrs, int defStyleAttr, @ColorInt int backgroundColor) {
        super(context, attrs, defStyleAttr);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);

        shapePaint = new Paint();
        shapePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        effectPaint = new Paint();

        invalidator = animation -> invalidate();

        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0f, 0f, getWidth(), getHeight(), backgroundPaint);

        if (target != null && shapeAnimator != null) {
            float shapeValue = (float) shapeAnimator.getAnimatedValue();
            target.getShape().draw(canvas, target.getAnchor(), shapeValue, shapePaint);
        }

        if (target != null && effectAnimator != null && shapeAnimator != null && !shapeAnimator.isRunning()) {
            float effectValue = (float) effectAnimator.getAnimatedValue();
            target.getEffect().draw(canvas, target.getAnchor(), effectValue, effectPaint);
        }
    }

    public void startSpotlight(long duration, TimeInterpolator interpolator, Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        animator.setDuration(duration);
        animator.setInterpolator(interpolator);
        animator.addListener(listener);
        animator.start();
    }

    public void finishSpotlight(long duration, TimeInterpolator interpolator, Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        animator.setDuration(duration);
        animator.setInterpolator(interpolator);
        animator.addListener(listener);
        animator.start();
    }

    public void startTarget(Target target) {
        removeAllViews();
        addView(target.getOverlay(), LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        this.target = target;

        int[] location = new int[2];
        getLocationInWindow(location);
        PointF offset = new PointF(location[0], location[1]);
        target.getAnchor().offset(-offset.x, -offset.y);

        if (shapeAnimator != null) {
            shapeAnimator.removeAllListeners();
            shapeAnimator.removeAllUpdateListeners();
            shapeAnimator.cancel();
        }

        shapeAnimator = ValueAnimator.ofFloat(0f, 1f);
        shapeAnimator.setDuration(target.getShape().getDuration());
        shapeAnimator.setInterpolator(target.getShape().getInterpolator());
        shapeAnimator.addUpdateListener(invalidator);
        shapeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                shapeAnimator.removeAllListeners();
                shapeAnimator.removeAllUpdateListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                shapeAnimator.removeAllListeners();
                shapeAnimator.removeAllUpdateListeners();
            }
        });

        if (effectAnimator != null) {
            effectAnimator.removeAllListeners();
            effectAnimator.removeAllUpdateListeners();
            effectAnimator.cancel();
        }

        effectAnimator = ValueAnimator.ofFloat(0f, 1f);
        effectAnimator.setStartDelay(target.getShape().getDuration());
        effectAnimator.setDuration(target.getEffect().getDuration());
        effectAnimator.setInterpolator(target.getEffect().getInterpolator());
        effectAnimator.setRepeatMode(target.getEffect().getRepeatMode());
        effectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        effectAnimator.addUpdateListener(invalidator);
        effectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                effectAnimator.removeAllListeners();
                effectAnimator.removeAllUpdateListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                effectAnimator.removeAllListeners();
                effectAnimator.removeAllUpdateListeners();
            }
        });

        shapeAnimator.start();
        effectAnimator.start();
    }

    public void finishTarget(Animator.AnimatorListener listener) {
        if (target == null || shapeAnimator == null) return;

        float currentValue = (float) shapeAnimator.getAnimatedValue();

        shapeAnimator.removeAllListeners();
        shapeAnimator.removeAllUpdateListeners();
        shapeAnimator.cancel();

        shapeAnimator = ValueAnimator.ofFloat(currentValue, 0f);
        shapeAnimator.setDuration(target.getShape().getDuration());
        shapeAnimator.setInterpolator(target.getShape().getInterpolator());
        shapeAnimator.addUpdateListener(invalidator);
        shapeAnimator.addListener(listener);
        shapeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                shapeAnimator.removeAllListeners();
                shapeAnimator.removeAllUpdateListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                shapeAnimator.removeAllListeners();
                shapeAnimator.removeAllUpdateListeners();
            }
        });

        if (effectAnimator != null) {
            effectAnimator.removeAllListeners();
            effectAnimator.removeAllUpdateListeners();
            effectAnimator.cancel();
            effectAnimator = null;
        }

        shapeAnimator.start();
    }

    public void cleanup() {
        if (effectAnimator != null) {
            effectAnimator.removeAllListeners();
            effectAnimator.removeAllUpdateListeners();
            effectAnimator.cancel();
            effectAnimator = null;
        }
        if (shapeAnimator != null) {
            shapeAnimator.removeAllListeners();
            shapeAnimator.removeAllUpdateListeners();
            shapeAnimator.cancel();
            shapeAnimator = null;
        }
        removeAllViews();
    }
}
