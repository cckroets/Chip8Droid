package chip_8.android;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by curtiskroetsch on 2014-05-17.
 */
class RenderThread extends Thread
{
  private final static int FRAMES_PER_SECOND = 50;
  private final static int SLEEP_TIME = 1000 / FRAMES_PER_SECOND;

  private boolean running = false;
  private AndroidDisplay display = null;
  private SurfaceHolder surfaceHolder = null;

  public RenderThread(AndroidDisplay display)
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
