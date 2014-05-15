package com.ckroetsch.chip8;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import Emulation.Emulator;
import chip_8.Chip8Processor;
import chip_8.RomFactory;

/**
 * Created by curtiskroetsch on 2014-05-15.
 */
public class Chip8Application extends Application
{
  private static Context context;
  private static RomFactory factory;
  private static Emulator emulator;
  private static Chip8Processor cpu;

  public static int SCREEN_WIDTH;
  public static int SCREEN_HEIGHT;

  @Override
  public void onCreate()
  {
    super.onCreate();
    context = this;
    factory = new RomFactory(context);
    cpu = new Chip8Processor();
    emulator = new Emulator(cpu);

    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Point p = new Point();
    windowManager.getDefaultDisplay().getSize(p);
    SCREEN_WIDTH = p.x;
    SCREEN_HEIGHT = p.y;
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
}
