package chip_8;

import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import Emulation.Rom;
import chip_8.android.IOUtil;

/**
 * Created by curtiskroetsch on 2014-05-15.
 */
public class Chip8Rom implements Rom
{
  private static String saveExtension  = ".save";

  private final String filename;
  private final Keyboard keyboard;
  private final String saveName;

  public Chip8Rom(String fname, int[] keyMap)
  {
    this.filename = fname;
    this.keyboard = new Keyboard(keyMap);
    this.saveName = fname + saveExtension;
  }

  public Keyboard getKeyboard() {
    return keyboard;
  }

  public byte[] getBytes()
  {
    byte[] result = null;
    try {
      InputStream in = IOUtil.openAsset("roms/" + filename);
      result = Utils.streamToBytes(in);
      in.close();
    } catch (IOException e) {
      Log.e("ROM", "Could not open Rom " + filename);
    } catch (NullPointerException e) {
      Log.e("ROM", "NPE");
    }
    return result;
  }

  @Override
  public DataInputStream getLoadStream()
  {
    DataInputStream loadStream = null;
    try {
      loadStream = IOUtil.getFileInputStream(saveName);
    } catch (FileNotFoundException e) {
      /* No save stream found, just return null */
    }
    return loadStream;
  }

  @Override
  public DataOutputStream getSaveStream()
  {
    DataOutputStream saveStream = null;
    try {
      saveStream = IOUtil.getFileOutputStream(saveName);
    } catch (IOException e) {
      Log.e("ROM", "Could not open save stream for " + saveName);
    }
    return saveStream;
  }
}
