package chip_8.android;

import android.content.Context;

import com.ckroetsch.chip8.Chip8Application;
import com.ckroetsch.chip8.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by curtiskroetsch on 2014-05-17.
 */
public class IOUtil
{
  public static DataOutputStream getFileOutputStream(String filename)
    throws IOException
  {
    OutputStream out;
    out = Chip8Application.getContext().openFileOutput(filename, Context.MODE_PRIVATE);
    return new DataOutputStream(out);
  }

  public static DataInputStream getFileInputStream(String filename)
    throws FileNotFoundException
  {
    InputStream in;
    in = Chip8Application.getContext().openFileInput(filename);
    return new DataInputStream(in);
  }

  public static InputStream openAsset(String assetName)
          throws IOException
  {
    return Chip8Application.getContext().getAssets().open(assetName);
  }

  public static InputStream openRawResource(int id)
  {
    return Chip8Application.getContext().getResources().openRawResource(id);
  }

  public static InputStream openRomConfig()
  {
    return openRawResource(R.raw.rominfo);
  }

}
