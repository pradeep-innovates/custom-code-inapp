package com.pradeep.androidintegration;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class BottomRoundedBorderTransformation extends BitmapTransformation {

    private final int radius;
    private final int borderSize;
    private final int borderColor;

    public BottomRoundedBorderTransformation(int radius, int borderSize, int borderColor) {
        this.radius = radius;
        this.borderSize = borderSize;
        this.borderColor = borderColor;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return addBottomRoundedBorder(pool, toTransform);
    }

    private Bitmap addBottomRoundedBorder(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap result = pool.get(width, height, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);

        // Draw image with bottom rounded corners
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Path path = new Path();
        RectF rect = new RectF(0, 0, width, height);

        float[] radii = new float[]{
                0f, 0f,          // Top-left
                0f, 0f,          // Top-right
                radius, radius,  // Bottom-left
                radius, radius   // Bottom-right
        };

        path.addRoundRect(rect, radii, Path.Direction.CW);
        canvas.clipPath(path);
        canvas.drawBitmap(source, 0, 0, paint);

        // Draw the orange border for bottom rounded corners
        Paint borderPaint = new Paint();
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderSize);
        borderPaint.setAntiAlias(true);

        RectF borderRect = new RectF(
                borderSize / 2f,
                borderSize / 2f,
                width - borderSize / 2f,
                height - borderSize / 2f
        );

        Path borderPath = new Path();
        borderPath.addRoundRect(borderRect, radii, Path.Direction.CW);
        canvas.drawPath(borderPath, borderPaint);

        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(("BottomRoundedBorderTransformation" + radius + borderSize + borderColor).getBytes());
    }
}
