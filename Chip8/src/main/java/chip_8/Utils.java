package chip_8;


import android.util.Log;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author ckroetsc
 */
public class Utils
{

  public static short concatBytes(byte a, byte b)
  {
    return (short)(((a & 0xFF) << 8) | (b & 0xFF));
  }

  public static byte[] streamToBytes(InputStream in) throws IOException
  {
    byte[] result = new byte[in.available()];
    ByteStreams.readFully(in, result);
    return result;
  }
}