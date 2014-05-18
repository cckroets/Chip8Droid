package chip_8.android;

import android.view.View;

import Emulation.ActionListener;
import Emulation.Emulator;
import chip_8.Chip8Processor;
import chip_8.Chip8Rom;
import chip_8.Display;
import chip_8.Keyboard;
import chip_8.SoundChip;

/**
 * Created by curtiskroetsch on 2014-05-18.
 * A Singleton Driver for the Chip-8 Emulator.
 * Handles focus and setting changes. Uses lazy initialization to save memory.
 * Different from the Emulator, because this handles both pausing within the game,
 * and pausing due to changes in focus.
 */
public class Chip8Driver
{
  private static Chip8Driver instance = new Chip8Driver();
  private static Display display = null;

  private Emulator emulator;
  private Chip8Processor cpu;
  private Chip8Rom rom;
  private boolean paused = false;
  private boolean started = false;

  public static Chip8Driver get()
  {
    if (instance == null) {
      instance = new Chip8Driver();
    }
    return instance;
  }

  public static void setDisplay(Display newDisplay)
  {
    newDisplay.setBitmap(get().cpu.getBitmap());
    display = newDisplay;
  }

  /**
   * Initialize the Chip-8 Emulator
   */
  private Chip8Driver()
  {
    this.cpu = new Chip8Processor();
    this.emulator = new Emulator(cpu);
  }

  public void initializeWithRom(Chip8Rom rom)
  {
    this.setSoundChip(SoundChipFactory.get().getChipFromPreferences());
    this.rom = rom;
    this.started = false;
    this.paused = false;
  }

  /**
   * Set the sound chip to a new chip
   * @param chip new sound chip
   */
  public void setSoundChip(SoundChip chip)
  {
    cpu.getRegisters().setSoundChip(chip);
  }

  public void addActionListener(ActionListener listener)
  {
    emulator.addActionListener(listener);
  }

  public Keyboard getKeyboard()
  {
    return rom.getKeyboard();
  }

  public void setFocus(boolean focused)
  {
    if (focused) {
      if (! started) {
        emulator.reset(rom);
        started = true;
      } else if (! paused) {
        emulator.resume();
      }
      display.start();
    } else {
      emulator.pause();
      display.stop();
    }
  }

  public void pause()
  {
    paused = true;
    emulator.pause();
  }

  public void resume()
  {
    paused = false;
    emulator.resume();
  }

  public void cleanup()
  {
    emulator.pause();
    display.stop();
    started = false;
  }

  public View.OnClickListener getOnSaveListener()
  {
    return new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        emulator.save();
      }
    };
  }

  public View.OnClickListener getOnLoadListener()
  {
    return new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        emulator.load();
      }
    };
  }

  public View.OnClickListener getOnResetListener()
  {
    return new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        emulator.reset();
      }
    };
  }

}
