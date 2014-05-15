package chip_8;


import Emulation.HardwareAdapter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
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

  private void handleKeyEvent(char key, boolean pressRelease) {

    if ((key >= '0') && (key <= '9')) {
      keys.set(key - '0',pressRelease);
    } else if ((key >= 'a') && (key <= 'f')) {
      keys.set(key - 'a' + 0xA, pressRelease);
    }
  }


  public void keyPressed(char event)
  {
    // Key was pressed
    handleKeyEvent(event, true);
  }


  public void keyReleased(char event)
  {
    // Key was released
    handleKeyEvent(event,false);
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
