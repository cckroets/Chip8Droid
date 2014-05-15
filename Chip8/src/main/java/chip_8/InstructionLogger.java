package chip_8;


import android.util.Log;

/**
 * @author ckroetsc
 */
public class InstructionLogger
{
  private Registers reg;
  private Memory mem;

  public void setReg(Registers reg)
  {
    this.reg = reg;
  }

  public void setMem(Memory mem)
  {
    this.mem = mem;
  }

  public InstructionLogger() { }

  public InstructionLogger(Registers registers, Memory memory)
  {
    this.reg = registers;
    this.mem = memory;
  }

  public void name(String s)
  {
    FormattedLogger.debug(s);
  }

  public void instrAddr(String s, int addr)
  {
    FormattedLogger.debug("{} {}", s, addr);
  }

  public void instrRegAddr(String s, int reg, int addr)
  {
    FormattedLogger.debug("{} V{}, {}", s, reg, addr);
  }

  public void instrRegAddr(String s, String reg, int addr)
  {
    FormattedLogger.debug("{} {}, {}", s, reg, addr);
  }

  public void instrRegReg(String s, int r1, int r2)
  {
    FormattedLogger.debug("{} V{}, V{}", s, r1, r2);
  }

  public void instrReg(String s, int r1)
  {
    FormattedLogger.debug("{} V{}", s, r1);
  }

  public void instrRegReg(String s, int r1, String r2)
  {
    FormattedLogger.debug("{} V{}, {}", s, r1, r2);
  }

  public void instrRegReg(String s, String r1, int r2)
  {
    FormattedLogger.debug("{} {}, V{}", s, r1, r2);
  }

  public void instrRegRegAddr(String s, int r1, int r2, int n)
  {
    FormattedLogger.debug("{} V{}, V{}, {}", s, r1, r2, n);
  }

  public void dumpI()
  {
    checkConfigured();
    FormattedLogger.debug("I=" + reg.i);
  }

  public void dumpV()
  {
    checkConfigured();
    FormattedLogger.debug("V0={}, V1={}, V2={}, V3={}, V4={}, V5={}, V6={}, V7={}, " +
              "V8={}, V9={}, VA={}, VB={}, VC={}, VD={}, VE={}, VF={}",
              reg.v(0),reg.v(1),reg.v(2),reg.v(3),reg.v(4),reg.v(5),
              reg.v(6),reg.v(7),reg.v(8),reg.v(9),reg.v(10),reg.v(11),
              reg.v(12),reg.v(13),reg.v(14),reg.v(15));
  }

  public void dumpTimers()
  {
    checkConfigured();
    FormattedLogger.debug("DT={}, ST={}", reg.dt, reg.st);
  }

  public void dumpAllReg()
  {
    checkConfigured();
    if (reg == null || mem == null)
      throw new NullPointerException("Registers/Memory have not been configured");
    dumpI();
    dumpTimers();
    dumpV();
  }

  public void dumpSprite(int length, boolean condition)
  {
    checkConfigured();
    if (! condition) return;
    for (int p=0; p < length; p++) {
      int bite = 0xFF & mem.at((short)(p+reg.i));
      FormattedLogger.debug(spriteLine(bite));
    }
  }

  private String spriteLine(int bite)
  {
    bite = (bite & 0xFF) | 0x100;
    return Integer.toBinaryString(bite).substring(1,9)
        .replaceAll("1","██")
        .replaceAll("0","--");
  }

  private void checkConfigured()
  {
    if (reg == null || mem == null)
      throw new NullPointerException("Registers/Memory have not been configured");
  }

  public void dumpSprite(int length)
  {
    dumpSprite(length,true);
  }
}


class FormattedLogger {

    public static void debug(String s, Object... values)
    {
        //Log.d("CHIP8", String.format(s,values));
    }
}