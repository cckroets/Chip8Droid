package chip_8.android;

import android.content.Context;
import android.os.Vibrator;
import com.ckroetsch.chip8.Chip8Application;
import chip_8.SoundChip;

/**
 * Created by curtiskroetsch on 2014-05-17.
 */
public class VibratorSoundChip implements SoundChip
{
  private Vibrator vibrator;

  public VibratorSoundChip()
  {
    this.vibrator = (Vibrator) Chip8Application.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    /* vibrator will b2 null if device does not have one */
  }

  @Override
  public void play()
  {
    if (vibrator != null)
      vibrator.vibrate(500);
  }

  @Override
  public void stop()
  {
    if (vibrator != null)
      vibrator.cancel();
  }

  @Override
  public void quit() { stop(); }
}
