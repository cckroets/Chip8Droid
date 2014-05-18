package chip_8;

/**
 * Created by curtiskroetsch on 2014-05-17.
 * A Chip-8 Sound Chip that is used to play a single sound
 */
public interface SoundChip
{
  public void play();

  public void stop();

  public void setEnabled(boolean enabled);
}
