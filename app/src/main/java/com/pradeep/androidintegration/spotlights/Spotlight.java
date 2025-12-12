package com.pradeep.androidintegration.spotlights;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Spotlight {

    private final SpotlightView spotlight;
    private final Target[] targets;
    private final long duration;
    private final TimeInterpolator interpolator;
    private final ViewGroup container;
    private final OnSpotlightListener spotlightListener;
    private int currentIndex = NO_POSITION;

    private static final int NO_POSITION = -1;

    private Spotlight(Builder builder) {
        this.spotlight = builder.spotlight;
        this.targets = builder.targets;
        this.duration = builder.duration;
        this.interpolator = builder.interpolator;
        this.container = builder.container;
        this.spotlightListener = builder.listener;

        container.addView(spotlight, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void start() {
        startSpotlight();
    }

    public void show(int index) {
        showTarget(index);
    }

    public void next() {
        showTarget(currentIndex + 1);
    }

    public void previous() {
        showTarget(currentIndex - 1);
    }

    public void finish() {
        finishSpotlight();
    }

    private void startSpotlight() {
        spotlight.startSpotlight(duration, interpolator, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (spotlightListener != null) spotlightListener.onStarted();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showTarget(0);
            }
        });
    }

    private void showTarget(int index) {
        if (currentIndex == NO_POSITION) {
            Target target = targets[index];
            currentIndex = index;
            spotlight.startTarget(target);
            if (target.getListener() != null) target.getListener().onStarted();
        } else {
            spotlight.finishTarget(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Target previousTarget = targets[currentIndex];
                    if (previousTarget.getListener() != null) previousTarget.getListener().onEnded();

                    if (index < targets.length) {
                        Target target = targets[index];
                        currentIndex = index;
                        spotlight.startTarget(target);
                        if (target.getListener() != null) target.getListener().onStarted();
                    } else {
                        finishSpotlight();
                    }
                }
            });
        }
    }

    private void finishSpotlight() {
        spotlight.finishSpotlight(duration, interpolator, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                spotlight.cleanup();
                container.removeView(spotlight);
                if (spotlightListener != null) spotlightListener.onEnded();
            }
        });
    }

    public static class Builder {
        private final Activity activity;
        private Target[] targets;
        private long duration = DEFAULT_DURATION;
        private TimeInterpolator interpolator = DEFAULT_ANIMATION;
        @ColorInt private int backgroundColor = DEFAULT_OVERLAY_COLOR;
        private ViewGroup container;
        private OnSpotlightListener listener;
        private SpotlightView spotlight;

        private static final long DEFAULT_DURATION = TimeUnit.SECONDS.toMillis(1);
        private static final TimeInterpolator DEFAULT_ANIMATION = new DecelerateInterpolator(2f);
        private static final int DEFAULT_OVERLAY_COLOR = 0x6000000;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setTargets(Target... targets) {
            if (targets == null || targets.length == 0) throw new IllegalArgumentException("Targets must not be empty.");
            this.targets = targets;
            return this;
        }

        public Builder setTargets(List<Target> targetsList) {
            if (targetsList == null || targetsList.isEmpty()) throw new IllegalArgumentException("Targets must not be empty.");
            this.targets = targetsList.toArray(new Target[0]);
            return this;
        }

        public Builder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder setBackgroundColorRes(@ColorRes int resId) {
            this.backgroundColor = ContextCompat.getColor(activity, resId);
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder setAnimation(TimeInterpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public Builder setContainer(ViewGroup container) {
            this.container = container;
            return this;
        }

        public Builder setOnSpotlightListener(OnSpotlightListener listener) {
            this.listener = listener;
            return this;
        }

        public Spotlight build() {
            if (activity == null) {
                throw new IllegalArgumentException("Activity must not be null");
            }

            this.container = this.container != null
                    ? this.container
                    : (ViewGroup) activity.getWindow().getDecorView();

            this.spotlight = new SpotlightView(activity, null, 0, backgroundColor);

            return new Spotlight(this);
        }

    }
}
