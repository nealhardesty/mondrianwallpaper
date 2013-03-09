package com.roadwaffle;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class MondrianWallpaper extends WallpaperService {

  private final Handler handler = new Handler();

  @Override
  public Engine onCreateEngine() {
    return new MondrianEngine();
  }

  class MondrianEngine extends Engine {

    private boolean visible;
    private Paint paint = new Paint();
    private Random rand = new Random();

    MondrianEngine() {
      //paint.setColor(0xff000000);
      //paint.setAntiAlias(false);
      //paint.setStrokeWidth(4);
      //paint.setStyle(Paint.Style.STROKE);
      paint.setStyle(Paint.Style.FILL);
    }

    private final Runnable drawFrameRunnable = new Runnable() {
      public void run() {
        drawFrame(2);
      }
    };

    void drawFrame() {
      drawFrame(1);
    }

    void drawFrame(int count) {
      final SurfaceHolder holder = getSurfaceHolder();

      Canvas c = null;
      try {
        c = holder.lockCanvas();
        if (c != null) {
          drawBox(c, count);
        }
      } finally {
        if (c != null)
          holder.unlockCanvasAndPost(c);
      }

      handler.removeCallbacks(drawFrameRunnable);
      if (visible)
        handler.postDelayed(drawFrameRunnable, 1000);
    }

    // return an int RGB given the current color palette
    private int getRandomRGB() {
      int r = rand.nextInt(96) + 64;
      int g = rand.nextInt(96) + 128;
      int b = rand.nextInt(192) + 64;
      return Color.rgb(r, g, b)|0x66000000;
    }

    private void drawBox(Canvas c, int count) {
      for (int i = 0; i < count; i++) {
        int x = rand.nextInt(c.getWidth());
        int y = rand.nextInt(c.getHeight());
        int width = 100 + rand.nextInt((c.getWidth() / 4));

        paint.setColor(getRandomRGB());
        c.drawRect(x, y, x + width, y + width, paint);

        //c.clipRect(x, y, x + width, y + width);
      }
    }

    @Override
    public void onCreate(SurfaceHolder surfaceHolder) {
      super.onCreate(surfaceHolder);

      setTouchEventsEnabled(false);
    }


    @Override
    public void onDestroy() {
      super.onDestroy();
    }



    @Override
    public void onSurfaceChanged(SurfaceHolder holder, int format,
                                 int width, int height) {
      super.onSurfaceChanged(holder, format, width, height);

      drawFrame(1000);
    }


    @Override
    public void onSurfaceCreated(SurfaceHolder holder) {
      super.onSurfaceCreated(holder);

      drawFrame(1000);

    }


    @Override
    public void onSurfaceDestroyed(SurfaceHolder holder) {
      super.onSurfaceDestroyed(holder);

      visible = false;
      handler.removeCallbacks(drawFrameRunnable);
    }


    @Override
    public void onTouchEvent(MotionEvent event) {
      super.onTouchEvent(event);
    }


    @Override
    public void onVisibilityChanged(boolean visible) {
      super.onVisibilityChanged(visible);
      this.visible = visible;
      if (visible)
        drawFrame();
      else
        handler.removeCallbacks(drawFrameRunnable);
    }

  }

}
