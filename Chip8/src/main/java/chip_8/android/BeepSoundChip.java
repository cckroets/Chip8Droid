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
  private MediaPlayer player = MediaPlayer.create(Chip8Application.getContext(), R.raw.buzz);

  @Override
  public void play()
  {
    if (! player.isPlaying()) {
      player.start();
    }
  }

  @Override
  public void stop()
  {
    if (player.isPlaying()) {
      player.pause();
    }
  }

  @Override
  public void quit()
  {
    player.release();
  }

}
