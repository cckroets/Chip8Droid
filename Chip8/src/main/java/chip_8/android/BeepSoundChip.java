package chip_8.android;

import android.media.MediaPlayer;

import com.ckroetsch.chip8.Chip8Application;
import com.ckroetsch.chip8.R;

import chip_8.SoundChip;

/**
 * Created by curtiskroetsch on 2014-05-17.
 */
public class BeepSoundChip implements SoundChip
{
  private boolean enabled = true;
  private MediaPlayer player = MediaPlayer.create(Chip8Application.getContext(), R.raw.beep);


  @Override
  public void play()
  {
    if (enabled && ! player.isPlaying())
      player.start();
  }

  @Override
  public void stop()
  {
    if (player.isPlaying()) player.pause();
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    this.enabled = enabled;
  }

}
