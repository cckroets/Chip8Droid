package com.ckroetsch.chip8;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Vibrator;
import android.view.WindowManager;

import Emulation.ActionListener;
import Emulation.Emulator;
import chip_8.Chip8Processor;
import chip_8.RomFactory;
import chip_8.android.AndroidEmulatorListener;

/**
 * Created by curtiskroetsch on 2014-05-15.
 */
public class Chip8Application extends Application
{
  private static Context context;
  private static RomFactory factory;
  private static Emulator emulator;
  private static Chip8Processor cpu;
  private static Vibrator vibrator;

  @Override
  public void onCreate()
  {
    super.onCreate();
    context = this;
    vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    factory = new RomFactory();
    cpu = new Chip8Processor();
    emulator = new Emulator(cpu);
    emulator.addActionListener(new AndroidEmulatorListener());
  }

  public static Context getContext()
  {
    return context;
  }

  public static RomFactory getRomFactory()
  {
    return factory;
  }

  public static Emulator getEmulator()
  {
    return emulator;
  }

  public static Chip8Processor getCPU()
  {
    return cpu;
  }

  public static Vibrator getVibrator()
  {
    return vibrator;
  }
}
