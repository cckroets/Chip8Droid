package chip_8.android;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import com.ckroetsch.chip8.Chip8Application;

import chip_8.SoundChip;

/**
 * Created by curtiskroetsch on 2014-05-17.
 */
public class VibratorSoundChip implements SoundChip
{
  private Vibrator vibrator;
  private boolean enabled = true;

  public VibratorSoundChip()
  {
    this.vibrator = Chip8Application.getVibrator();
  }

  @Override
  public void play()
  {
    if (enabled) vibrator.vibrate(500);
  }

  @Override
  public void stop()
  {
    vibrator.cancel();
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    this.enabled = enabled;
  }
}
