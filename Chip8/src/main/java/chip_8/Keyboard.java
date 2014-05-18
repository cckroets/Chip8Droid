package chip_8;


import Emulation.HardwareAdapter;
import java.util.BitSet;


/**
 * @author ckroetsc
 */
public class Keyboard extends HardwareAdapter
{
  public static final int NUM_KEYS = 16;

  private BitSet keys;
  private int[] keyMap;

  public Keyboard(int[] keyMap)
  {
    this.keys = new BitSet(NUM_KEYS);
    this.keyMap = keyMap;
  }

  public int waitForPress() {
    for (int i = 0; i < keyMap.length; i++)
      if (keys.get(keyMap[i])) return keyMap[i];
    return -1;
  }

  public boolean isDown(int key) {
    return keys.get(key);
  }

  private void handleKeyEvent(int button, boolean pressRelease)
  {
    int key = keyMap[button];
    keys.set(key,pressRelease);
  }


  public void keyPressed(int button)
  {
    // Key was pressed
    handleKeyEvent(button, true);
  }


  public void keyReleased(int button)
  {
    // Key was released
    handleKeyEvent(button,false);
  }

  public void release(int key)
  {
    keys.set(key, false);
  }

  public int getNumKeys() { return keyMap.length; }

  @Override
  public void reset()
  {
    keys.clear();
  }
}
