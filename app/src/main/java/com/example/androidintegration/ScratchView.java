package com.example.androidintegration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ScratchView extends View {

    private Bitmap overlayBitmap;
    private Canvas overlayCanvas;
    private Paint scratchPaint;
    private Path scratchPath;
    private boolean isScratched = false;
    private OnScratchCompleteListener scratchCompleteListener;

    private static final float SCRATCH_THRESHOLD = 0.2f; // 20%

    public ScratchView(Context context) {
        super(context);
        init();
    }

    public ScratchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        scratchPaint = new Paint();
        scratchPaint.setAlpha(0);
        scratchPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        scratchPaint.setAntiAlias(true);
        scratchPaint.setDither(true);
        scratchPaint.setStyle(Paint.Style.STROKE);
        scratchPaint.setStrokeJoin(Paint.Join.ROUND);
        scratchPaint.setStrokeCap(Paint.Cap.ROUND);
        scratchPaint.setStrokeWidth(100);

        scratchPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (overlayBitmap != null) {
            canvas.drawBitmap(overlayBitmap, 0, 0, null);
            overlayCanvas.drawPath(scratchPath, scratchPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scratchPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                scratchPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                checkScratchedPercentage();
                break;
        }

        invalidate();
        return true;
    }

    private void checkScratchedPercentage() {
        if (overlayBitmap == null || isScratched) return;

        int width = overlayBitmap.getWidth();
        int height = overlayBitmap.getHeight();
        int totalPixels = width * height;
        int[] pixels = new int[totalPixels];
        overlayBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int transparentPixels = 0;
        for (int pixel : pixels) {
            if (Color.alpha(pixel) == 0) {
                transparentPixels++;
            }
        }

        float scratchedPercentage = (transparentPixels * 1f) / totalPixels;

        if (scratchedPercentage >= SCRATCH_THRESHOLD) {
            isScratched = true;
            if (scratchCompleteListener != null) {
                scratchCompleteListener.onScratchComplete();
            }
        }
    }

    public void setOverlayView(View view) {
        if (view == null) return;

        view.setDrawingCacheEnabled(true);
        view.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
        view.layout(0, 0, getWidth(), getHeight());
        view.buildDrawingCache();

        Bitmap viewBitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        overlayBitmap = Bitmap.createBitmap(viewBitmap.getWidth(), viewBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        overlayCanvas = new Canvas(overlayBitmap);
        overlayCanvas.drawBitmap(viewBitmap, 0, 0, null);
        invalidate();
    }

    public void clearRemainingArea() {
        if (overlayCanvas != null) {
            overlayCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            invalidate();
        }
    }

    public interface OnScratchCompleteListener {
        void onScratchComplete();
    }

    public void setOnScratchCompleteListener(OnScratchCompleteListener listener) {
        this.scratchCompleteListener = listener;
    }
}
