package chip_8;


import Emulation.CPU;
import Emulation.Hardware;
import Emulation.Rom;
import com.google.common.collect.Lists;
import java.util.Collection;


/**
 * @author ckroetsc
 */
public class Chip8Processor implements CPU
{
  public static InstructionLogger log = new InstructionLogger();
  public static final int SPRITE_LENGTH = 5;
  public static final short START_OF_PROGRAM = 0x200;
  public static final int PIXEL_WIDTH = 64;
  public static final int PIXEL_HEIGHT = 32;

  private Registers reg;
  private Keyboard keyboard;
  private Memory memory;
  private Collection<Hardware> componentList;
  private BinaryBitmap bmap;

  // Use these to avoid annoying explicit casts in arithmetic
  public static final byte zero = 0;
  public static final byte one = 1;


  /* Initialize the Chip8Processor exactly once */
  public Chip8Processor()
  {
    log.setMem(memory);
    log.setReg(reg);
    this.reg = new Registers();
    this.memory = new Memory();
    this.bmap = BinaryBitmap.newBitmap1D(PIXEL_WIDTH,PIXEL_HEIGHT);
    this.componentList = Lists.newArrayList(reg,memory,bmap);
  }

  @Override
  public void loadRom(Rom rom)
  {
    Chip8Rom chip8Rom = (Chip8Rom) rom;
    keyboard = chip8Rom.getKeyboard();
    memory.loadBytes(chip8Rom.getBytes(), START_OF_PROGRAM);
  }


  @Override
  public Collection<Hardware> getHardwareComponents()
  {
    return componentList;
  }


  @Override
  public void cleanup() { }


  @Override
  public void executeCycle()
  {
    /* Read instruction */
    short instr = memory.readInstruction(reg.pc);
    /* Update PC */
    reg.pc += 2;
    /* Execute instruction */
    executeInstr(instr);
    /* Wait for Clock Cycle to end */
    sleep();
  }

  /* Wait for Clock Cycle to end */
  private void sleep() {
    try {
      Thread.sleep(1);
    } catch(InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
  }

  /* Execute an instruction */
  private void executeInstr(int instr)
  {
    int x   = (instr & 0x0F00) >> 8;
    int y   = (instr & 0x00F0) >> 4;
    int kk  = instr & 0x00FF;
    int nnn = instr & 0x0FFF;

    switch(instr & 0xF000)
    {
      case 0x0000:
        if (instr == 0x00E0) {
          bmap.clear();
          //log.name("CLS");
        }
        else if (instr == 0x00EE) {
          reg.ret();
          //log.name("RET");
        }
        break;
      case 0x1000:
        reg.pc = (short)nnn;
        //log.instrAddr("JP", nnn);
        break;
      case 0x2000:
        reg.call(nnn);
        //log.instrAddr("CALL", nnn);
        break;
      case 0x3000:
        se(x,kk);
        //log.instrRegAddr("SE", x, kk);
        break;
      case 0x4000:
        sne(x,kk);
        //log.instrRegAddr("SNE", x, kk);
        break;
      case 0x5000:
        se(x,reg.v(y));
        //log.instrRegReg("SE", x, y);
        break;
      case 0x6000:
        reg.setVx(x, kk);
        //log.instrRegAddr("LD", x, kk);
        break;
      case 0x7000:
        reg.setVx(x, reg.v(x) + kk);
        //log.instrRegAddr("ADD", x, kk);
        break;
      case 0x8000:
        dispatchALU(x, y, instr & 0x000F);
        break;
      case 0x9000:
        sne(x,reg.v(y));
        //log.instrRegReg("SNE", x, y);
        break;
      case 0xA000:
        reg.i = nnn;
        //log.instrRegAddr("LD", "I", nnn);
        break;
      case 0xB000:
        reg.pc = (nnn + reg.v(0));
        //log.instrRegAddr("JP", 0, nnn);
        break;
      case 0xC000:
        int rand = (int)(Math.random() * 255);
        reg.setVx(x, rand & kk);
        //log.instrRegAddr("RND", x, kk);
        break;
      case 0xD000:
        draw(reg.v(x), reg.v(y), (instr & 0x000F));
        //log.instrRegRegAddr("DRW", x, y, (instr & 0x000F));
        break;
      case 0xE000:
        boolean skp_if_pressed = (instr & 1) == 0;
        if (skp_if_pressed == keyboard.isDown(reg.v(x))) {
          keyboard.release(reg.v(x));
          reg.pc += 2;
        }
        //String op = (kk == 0x9E) ? "SKP" : "SKNP";
        //log.instrReg(op, x);
        break;
      case 0xF000:
        dispatchLD(x, kk);
        break;
    }
  }

  /* Skip next instruction on 'equal' */
  private void se(int x, int bite) {
    if (reg.v(x) == bite)
      reg.pc += 2;
  }

  /* Skip next instruction on 'not equal' */
  private void sne(int x, int bite) {
    if (reg.v(x) != bite)
      reg.pc += 2;
  }

  /* Dispatch an instruction to the Arithmetic Logic Unit */
  public void dispatchALU(int x, int y, int opcode) {
    int result = 0;
    int vX = reg.v(x) & 0xFF;
    int vY = reg.v(y) & 0xFF;

    switch (opcode) {
      case 0:
        result = vY;
        break;
      case 1:
        result = (vX | vY);
        //log.instrRegReg("OR", x, y);
        break;
      case 2:
        result = (vX & vY);
        //log.instrRegReg("AND", x, y);
        break;
      case 3:
        result = (vX ^ vY);
        //log.instrRegReg("XOR", x, y);
        break;
      case 4:
        result = (vX + vY);
        reg.setVF(result > 0xFF);
        //log.instrRegReg("ADD", x, y);
        break;
      case 5:
        reg.setVF(vX > vY);
        result = (vX - vY);
        //log.instrRegReg("SUB", x, y);
        break;
      case 6:
        reg.setVF((vX & 1) == 1);
        result = (vX >> 1);
        //log.instrRegReg("SHR", x, y);
        break;
      case 7:
        reg.setVF(vY > vX);
        result = (vY - vX);
        //log.instrRegReg("SUBN", x, y);
        break;
      case 0xE:
        reg.setVF((vX & 0x80) != 0);
        result = (vX << 1);
        //log.instrRegReg("SHL", x, y);
        break;
    }
    reg.setVx(x, result);
  }

  /* Dispatch a general Load instruction */
  private void dispatchLD(int x, int opcode) {
    switch (opcode) {
      case 0x07:
        reg.setVx(x, reg.dt);
        //log.instrRegReg("LD", x, "DT");
        break;
      case 0x0A:
        byte k = (byte)keyboard.waitForPress();
        // If no button was pressed, repeat instruction
        if (k == -1){
          reg.pc -= 2;
          break;
        }
        reg.setVx(x, k);
        keyboard.release(k);
        //log.instrRegReg("LD", x, "K");
        break;
      case 0x15:
        reg.dt = reg.v(x);
        //log.instrRegReg("LD", "DT", x);
        break;
      case 0x18:
        reg.st = reg.v(x);
        //log.instrRegReg("LD", "ST", x);
        break;
      case 0x1E:
        reg.i += (0xFF & reg.v(x));
        //log.instrRegReg("ADD", "I", x);
        break;
      case 0x29:
        reg.i = (reg.v(x) & 0x0F) * SPRITE_LENGTH;
        //log.instrRegReg("LD", "F", x);
        break;
      case 0x33:
        memory.storeBCD(reg.v(x), reg.i);
        //log.instrRegReg("LD", "B", x);
        break;
      case 0x55:
        memory.store(x, reg);
        //log.instrRegReg("LD", "[I]", x);
        break;
      case 0x65:
        memory.load(x, reg);
        //log.instrRegReg("LD", x, "[I]");
        break;
    }
  }

  /* Draw a sprite av (vx,vy) on the screen, which starts at i in memory */
  public void draw(int vx, int vy, int height)
  {
    vx &= 0xFF;
    //log.dumpI();
    //log.dumpSprite(height);
    //log.dumpAllReg();
    if ( vy < 0) vy += PIXEL_HEIGHT;
    int y = vy % PIXEL_HEIGHT;
    boolean collision = false;
    for (int p = 0; p < height; p++) {
      byte bite = memory.at((short)(reg.i+p));
      for (int b = 0; b < 8; b++) {
        boolean on = ((bite & (0x80 >> b))) != 0;
        int x = (vx + b) % PIXEL_WIDTH;
        if (x < 0) x += PIXEL_WIDTH;
        boolean erased = on && bmap.get(x, y);
        collision |= erased;
        bmap.xor(x,y,on);
      }
      y = (y + 1) % PIXEL_HEIGHT;
    }

    reg.setVF(collision);
    bmap.notifyObservers();
  }

  public BinaryBitmap getBitmap() { return bmap; }

  public Registers getRegisters() { return reg; }

  public Keyboard getKeyboard() { return keyboard; }
}