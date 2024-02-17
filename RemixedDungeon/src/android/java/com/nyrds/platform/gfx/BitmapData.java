package com.nyrds.platform.gfx;

import static com.badlogic.gdx.graphics.g2d.Gdx2DPixmap.GDX2D_FORMAT_RGBA8888;

import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;

import java.io.InputStream;

public class BitmapData {

    Gdx2DPixmap bitmap;

    public BitmapData(int w, int h) {
        bitmap = Gdx2DPixmap.newPixmap(w,h,GDX2D_FORMAT_RGBA8888);
    }
    public BitmapData(android.graphics.Bitmap androidBitmap) {
        int width = androidBitmap.getWidth();
        int height = androidBitmap.getHeight();
        int[] pixels = new int[width * height];
        androidBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        bitmap = Gdx2DPixmap.newPixmap(width, height, GDX2D_FORMAT_RGBA8888);
        bitmap.getPixels().asIntBuffer().put(pixels);
    }
    public BitmapData(InputStream inputStream) {
        bitmap = Gdx2DPixmap.newPixmap(inputStream,GDX2D_FORMAT_RGBA8888);
    }

    public static BitmapData createBitmap(int w, int h) {
        return new BitmapData(w,h);
    }

    public static BitmapData decodeStream(java.io.InputStream inputStream) {
        return new BitmapData(inputStream);
    }
    public android.graphics.Bitmap toBitmap() {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels().asIntBuffer().get(pixels);

        android.graphics.Bitmap androidBitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
        androidBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return androidBitmap;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public void getAllPixels(int[] pixels) {
        bitmap.getPixels().asIntBuffer().get(pixels);
    }

    public int getPixel(int x, int y) {
        return bitmap.getPixel(x,y);
    }

    public void eraseColor(int color) {
        bitmap.clear(color);
    }

    public void setPixel(int x, int y, int color) {
        bitmap.setPixel(x,y, color);
    }
}
