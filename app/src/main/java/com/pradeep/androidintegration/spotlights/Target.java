package com.pradeep.androidintegration.spotlights;

import android.graphics.PointF;
import android.view.View;

import com.pradeep.androidintegration.spotlights.effect.Effect;
import com.pradeep.androidintegration.spotlights.effect.EmptyEffect;
import com.pradeep.androidintegration.spotlights.shape.Circle;
import com.pradeep.androidintegration.spotlights.shape.Shape;

public class Target {

    private final PointF anchor;
    private final Shape shape;
    private final Effect effect;
    private final View overlay;
    private final OnTargetListener listener;

    public Target(PointF anchor, Shape shape, Effect effect, View overlay, OnTargetListener listener) {
        this.anchor = anchor;
        this.shape = shape;
        this.effect = effect;
        this.overlay = overlay;
        this.listener = listener;
    }

    public PointF getAnchor() {
        return anchor;
    }

    public Shape getShape() {
        return shape;
    }

    public Effect getEffect() {
        return effect;
    }

    public View getOverlay() {
        return overlay;
    }

    public OnTargetListener getListener() {
        return listener;
    }

    public static class Builder {

        private PointF anchor = new PointF(0f, 0f);
        private Shape shape = new Circle(100f);
        private Effect effect = new EmptyEffect();
        private View overlay;
        private OnTargetListener listener;

        public Builder setAnchor(View view) {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            float x = location[0] + view.getWidth() / 2f;
            float y = location[1] + view.getHeight() / 2f;
            this.anchor = new PointF(x, y);
            return this;
        }

        public Builder setAnchor(float x, float y) {
            this.anchor = new PointF(x, y);
            return this;
        }

        public Builder setAnchor(PointF anchor) {
            this.anchor = anchor;
            return this;
        }

        public Builder setShape(Shape shape) {
            this.shape = shape;
            return this;
        }

        public Builder setEffect(Effect effect) {
            this.effect = effect;
            return this;
        }

        public Builder setOverlay(View overlay) {
            this.overlay = overlay;
            return this;
        }

        public Builder setOnTargetListener(OnTargetListener listener) {
            this.listener = listener;
            return this;
        }

        public Target build() {
            return new Target(anchor, shape, effect, overlay, listener);
        }
    }
}
