package chip_8;


import Emulation.Screen.Bitmap;

/**
 * Created by curtiskroetsch on 2014-05-17.
 */
public interface Display
{
  public void start();

  public void stop();

  public void setBitmap(Bitmap o);
}
