package chip_8.android;


import com.ckroetsch.chip8.R;

import chip_8.EmptySoundChip;
import chip_8.SoundChip;

/**
 * Created by curtiskroetsch on 2014-05-18.
 */
public class SoundChipFactory
{
  private static SoundChipFactory instance;

  private SoundChip beepChip;
  private SoundChip vibrateChip;
  private SoundChip emptyChip;

  public static SoundChipFactory get()
  {
    if (instance == null) {
      instance = new SoundChipFactory();
    }
    return instance;
  }

  public SoundChip getVibatorChip()
  {
    if (vibrateChip == null) {
      vibrateChip = new VibratorSoundChip();
    }
    return vibrateChip;
  }

  public SoundChip getBeepChip()
  {
    if (beepChip == null) {
      beepChip = new BeepSoundChip();
    }
    return beepChip;
  }

  public SoundChip getEmptyChip()
  {
    if (emptyChip == null) {
      emptyChip = new EmptySoundChip();
    }
    return emptyChip;
  }

  private SoundChip getSoundChip(int value)
  {
    switch (value) {
      case 1 : return getVibatorChip();
      case 2 : return getBeepChip();
      default: return getEmptyChip();
    }
  }

  public SoundChip getChipFromPreferences()
  {
    int prefValue = IOUtil.getPreferenceInt(R.string.pref_sound_key,0);
    return getSoundChip(prefValue);
  }


}
