package chip_8;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.ckroetsch.chip8.Chip8Application;

import Emulation.Screen.ImageScalingAlgorithm;
import Emulation.Screen.ImageScalingFactory;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * @author ckroetsc
 */
public class Display extends SurfaceView implements Observer, SurfaceHolder.Callback, View.OnTouchListener
{
  private int WIDTH = 64;
  private int HEIGHT = 32;

  public static int FOREGROUND_COLOR = Color.CYAN;
  public static int BACKGROUND_COLOR = Color.DKGRAY;

  private BinaryBitmap bitmapModel = null;
  private Bitmap bmap = null;
  private Paint paint = null;
  private GameThread thread = null;




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
    cpu.setDisplay(this);
    this.bitmapModel = cpu.getBitmap();
    this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    setOnTouchListener(this);
  }

/*
  @Override
  public synchronized void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    int scaleFactor = upscaleAlg.getScaleFactor();
    int scaledTileSize = tileSize / scaleFactor;

    g2.setColor(BACKGROUND_COLOR);
    g2.fillRect(0,0, tileSize * tilesAcross, tileSize * tilesDown);
    g2.setColor(FOREGROUND_COLOR);

    int pixelOnLength = 0;

    for (int y=0; y < _bitmapUpscaled.getHeight(); y++) {
      for (int x=0; x < _bitmapUpscaled.getWidth(); x++) {
        if (_bitmapUpscaled.get(x,y)) {
          pixelOnLength++;
        } else {
          drawLine(g2, x - pixelOnLength, y, pixelOnLength, scaledTileSize);
          pixelOnLength = 0;
        }
      }
      drawLine(g2, _bitmapUpscaled.getWidth() - pixelOnLength, y, pixelOnLength, scaledTileSize);
      pixelOnLength = 0;
    }
  }

  private void drawLine(Graphics2D g2, int x, int y, int width, int scale)
  {
    if (width > 0) {
      g2.fillRect(x * scale, y * scale, scale * width, scale);
    }
  }

  public Display(Chip8Processor cpu)
  {
    this._bitmapModel = cpu.getBitmap();
    this._bitmapUpscaled = _bitmapModel.genEmptyAndScaled(upscaleAlg.getScaleFactor());
    this.tilesAcross = Chip8Processor.PIXEL_WIDTH;
    this.tilesDown = Chip8Processor.PIXEL_HEIGHT;
    cpu.setDisplay(this);

    this.setPreferredSize(new Dimension(tilesAcross * tileSize, tilesDown * tileSize));
    this.setFocusable(true);
    this.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent keyEvent)
      {
        if (keyEvent.getKeyChar() == 'n')
        {
          algIndex = (algIndex + 1) % algorithms.size();
          setUpscaleAlgorithm(algorithms.get(algIndex));
        } else if (keyEvent.getKeyChar() == 'm')
        {
          algIndex--;
          algIndex = (algIndex == -1) ? algorithms.size()-1 : algIndex;
          setUpscaleAlgorithm(algorithms.get(algIndex));
        }
      }
    });
  } */

  public void startGame()
  {
    if (thread == null)
    {
      thread = new GameThread(this);
      thread.startThread();
    }
  }

  @Override
  public void update(Observable observable, Object o)
  {
  }

  @Override
  public void surfaceCreated(SurfaceHolder surfaceHolder)
  {

  }

  @Override
  public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3)
  {

  }

  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceHolder)
  {

  }

  @Override
  protected void onDraw(Canvas canvas)
  {
    paint.setColor(BACKGROUND_COLOR);
    canvas.drawRect(0,0,Chip8Application.SCREEN_WIDTH,Chip8Application.SCREEN_HEIGHT,paint);
    float sizeFactor = Chip8Application.SCREEN_WIDTH / 64;
    Log.d("DISPLAY", "DRAW");
    paint.setColor(FOREGROUND_COLOR);
    for (int x=0; x < WIDTH; x++) {
      for (int y=0; y < HEIGHT; y++) {
        boolean on = bitmapModel.get(x,y);
        if (on) {
          float x1 = x * sizeFactor;
          float x2 = x1 + sizeFactor;
          float y1 = y * sizeFactor;
          float y2 = y1 + sizeFactor;
          canvas.drawRect(x1,y1,x2,y2,paint);
        }
      }
    }

  }


  @Override
  public boolean onTouch(View view, MotionEvent motionEvent)
  {
    this.invalidate();
    return true;
  }
}

 class GameThread extends Thread
{
  private final static int SLEEP_TIME = 40;

  private boolean running = false;
  private Display canvas = null;
  private SurfaceHolder surfaceHolder = null;

  public GameThread(Display canvas)
  {
    super();
    this.canvas = canvas;
    this.surfaceHolder = canvas.getHolder();
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
    Canvas c = null;
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
            canvas.onDraw(c);
          }
        }
        sleep(SLEEP_TIME);
      }
      catch(InterruptedException ie)
      {
      }
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