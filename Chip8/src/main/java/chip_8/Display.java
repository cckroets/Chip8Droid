package chip_8;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ckroetsch.chip8.Chip8Application;

/**
 * @author ckroetsc
 */
public class Display extends SurfaceView implements SurfaceHolder.Callback
{
  private final int WIDTH = 64;
  private final int HEIGHT = 32;
  private final int EDGE_OFFSET = 5;

  public static int FOREGROUND_COLOR = Color.BLACK;
  public static int BACKGROUND_COLOR = Color.LTGRAY;

  private BinaryBitmap bitmapModel = null;
  private Paint paint = null;
  private RenderThread thread = null;

  public Display(Context context)
  {
    super(context);
    init(context);
  }

  public Display(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    init(context);
  }

  public Display(Context context, AttributeSet attrs, int defStyle)
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
  }

  public void start()
  {
    if (thread == null)
    {
      thread = new RenderThread(this);
      thread.startThread();
    }
  }

  public void stop()
  {
    if (thread != null) {
      thread.stopThread();
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
    canvas.drawRect(0,0,Chip8Application.SCREEN_WIDTH,Chip8Application.SCREEN_HEIGHT,paint);
    float sizeFactor = Chip8Application.SCREEN_WIDTH / WIDTH;
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

class RenderThread extends Thread
{
  private final static int FRAMES_PER_SECOND = 50;
  private final static int SLEEP_TIME = 1000 / FRAMES_PER_SECOND;

  private boolean running = false;
  private Display display = null;
  private SurfaceHolder surfaceHolder = null;

  public RenderThread(Display display)
  {
    super();
    this.display = display;
    this.surfaceHolder = display.getHolder();
  }

  public void startThread()
  {
    running = true;
    super.start();
  }

  public void stopThread()
  {
    running = false;
  }

  public void run()
  {
    Canvas c;
    while (running)
    {
      c = null;
      try
      {
        c = surfaceHolder.lockCanvas();
        synchronized (surfaceHolder)
        {
          if (c != null)
          {
            display.onDraw(c);
          }
        }
        sleep(SLEEP_TIME);
      }
      catch(InterruptedException ie) { }
      finally
      {
        // do this in a finally so that if an exception is thrown
        // we don't leave the Surface in an inconsistent state
        if (c != null)
        {
          surfaceHolder.unlockCanvasAndPost(c);
        }
      }
    }
  }
}