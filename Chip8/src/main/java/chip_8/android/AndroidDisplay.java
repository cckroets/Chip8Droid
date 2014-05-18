package chip_8.android;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.ckroetsch.chip8.Chip8Application;

import chip_8.BinaryBitmap;
import chip_8.Chip8Processor;
import chip_8.Display;

/**
 * @author ckroetsc
 */
public class AndroidDisplay extends SurfaceView implements SurfaceHolder.Callback, Display
{
  private final int WIDTH = 64;
  private final int HEIGHT = 32;
  private final int EDGE_OFFSET = 5;

  public static int SCREEN_WIDTH;
  public static int SCREEN_HEIGHT;

  public static int FOREGROUND_COLOR = Color.BLACK;
  public static int BACKGROUND_COLOR = Color.LTGRAY;

  private BinaryBitmap bitmapModel = null;
  private Paint paint = null;
  private RenderThread thread = null;

  public AndroidDisplay(Context context)
  {
    super(context);
    init(context);
  }

  public AndroidDisplay(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    init(context);
  }

  public AndroidDisplay(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    init(context);
  }


  public void init(Context context)
  {
    getHolder().addCallback(this);
    setFocusable(true);
    Chip8Processor cpu = Chip8Application.getCPU();
    this.bitmapModel = cpu.getBitmap();
    this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    context.getSystemService(Context.WINDOW_SERVICE);
    Point p = new Point();
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    windowManager.getDefaultDisplay().getSize(p);
    SCREEN_WIDTH = p.x;
    SCREEN_HEIGHT = p.y;
  }

  @Override
  public void start()
  {
    if (thread == null)
    {
      thread = new RenderThread(this);
      thread.startThread();
    }
  }

  @Override
  public void stop()
  {
    if (thread != null) {
      thread.stopThread();
      thread = null;
    }
  }

  @Override
  public void surfaceCreated(SurfaceHolder surfaceHolder) { }

  @Override
  public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) { }

  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceHolder) { }


  @Override
  protected void onDraw(Canvas canvas)
  {
    paint.setColor(BACKGROUND_COLOR);
    canvas.drawRect(0,0,SCREEN_WIDTH,SCREEN_HEIGHT,paint);
    float sizeFactor = SCREEN_WIDTH / WIDTH;
    paint.setColor(FOREGROUND_COLOR);

    float x1, x2, y1, y2;
    for (int x=0; x < WIDTH; x++) {
      for (int y=0; y < HEIGHT; y++) {
        boolean on = bitmapModel.get(x,y);
        if (on) {
          x1 = x * sizeFactor + EDGE_OFFSET;
          x2 = x1 + sizeFactor;
          y1 = y * sizeFactor + EDGE_OFFSET;
          y2 = y1 + sizeFactor;
          canvas.drawRect(x1,y1,x2,y2,paint);
        }
      }
    }
  }
}

