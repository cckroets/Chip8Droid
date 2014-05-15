package chip_8;

import android.util.Log;
import com.ckroetsch.chip8.Chip8Application;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import Emulation.Rom;

/**
 * Created by curtiskroetsch on 2014-05-15.
 */
public class Chip8Rom implements Rom
{
  private static String romExtension  = ".rom";
  private static String saveExtension  = ".chip8";
  private static String romFolderName  = "roms";
  private static String saveFolderName = "saves";

  private final String filename;
  private final Keyboard keyboard;

  public Chip8Rom(String fname, int[] keyMap)
  {
    this.filename = fname;
    this.keyboard = new Keyboard(keyMap);
  }

  public Keyboard getKeyboard() {
    return keyboard;
  }

  public byte[] getBytes()
  {
    byte[] result = null;
    try {
      Log.d("ROM", Arrays.toString(Chip8Application.getContext().getAssets().list("roms")));
      InputStream in = Chip8Application.getContext().getAssets().open("roms/" + filename);
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
    return null;
  }

  @Override
  public DataOutputStream getSaveStream()
  {
    return null;
  }
}
