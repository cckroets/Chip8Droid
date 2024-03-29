package chip_8;


import android.util.Log;
import Emulation.Hardware;
import com.ckroetsch.chip8.Chip8Application;
import com.ckroetsch.chip8.R;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


/**
 * @author ckroetsc
 */
public class Memory implements Hardware
{
  public static final int RAM_SIZE = 4096;
  private byte[] ram = new byte[RAM_SIZE];

  public Memory()
  {
    /* Load the digits font into memory */
    InputStream in = Chip8Application.getContext().getResources().openRawResource(R.raw.digits);
    try {
      byte[] bytes = Utils.streamToBytes(in);
      this.loadBytes(bytes, Chip8Processor.zero);
    } catch (IOException e) {
      Log.e("MEM", "Could not load digits");
      e.printStackTrace();
    }
  }

  @Override
  public void saveState(DataOutput out)
      throws IOException
  {
    out.write(ram);
  }

  @Override
  public void loadState(DataInput in)
      throws IOException
  {
    in.readFully(ram);
  }

  /* Reset the memory. Blank the loaded rom, but leave the loaded digits */
  @Override
  public void reset()
  {
    int begin = Chip8Processor.START_OF_PROGRAM;
    int end = RAM_SIZE - begin;
    Arrays.fill(ram, begin, end, Chip8Processor.zero);
  }

  /* Read an instruction from memory at pointer */
  public short readInstruction(int pointer)
  {
    return Utils.concatBytes(ram[pointer],ram[pointer+1]);
  }

  /* Store the binary digits of a number in memory at I */
  public void storeBCD(int num, int location)
  {
    num &= 0xFF;
    int hun = (num / 100);
    int ten = ((num % 100) / 10);
    int one = ((num % 100) % 10);

    ram[location] = (byte)hun;
    ram[location+1] = (byte)ten;
    ram[location+2] = (byte)one;
  }

  /* Store [v[0],v[x]] at I in memory */
  public void store(int x, Registers reg) {
    // Source: v[0], Dest: ram[i], length: x
    System.arraycopy(reg.getV(),0,ram,reg.i,x+1);
  }

  /* Load memory at I into [v[0],v[x]] */
  public void load(int x, Registers reg)
  {
    // Source: ram[i], Dest: v[0], length: x
    System.arraycopy(ram,reg.i,reg.getV(),0,x+1);
  }

  /* Get byte at index in memory */
  public byte at(short index)
  {
    return ram[index];
  }

  /* Load bytes from a file into memory at the given location */
  public void loadBytes(byte[] bytes, short location)
  {
    int len = bytes.length;
    System.arraycopy(bytes,0,ram,location,len);
  }



}
