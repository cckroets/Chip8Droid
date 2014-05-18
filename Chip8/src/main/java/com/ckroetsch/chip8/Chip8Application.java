package com.ckroetsch.chip8;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;

import Emulation.Emulator;
import chip_8.Chip8Processor;
import chip_8.android.RomFactory;
import chip_8.android.AndroidEmulatorListener;

/**
 * Created by curtiskroetsch on 2014-05-15.
 */
public class Chip8Application extends Application
{
  private static Context context;

  @Override
  public void onCreate()
  {
    super.onCreate();
    context = this;
  }

  public static Context getContext()
  {
    return context;
  }

}
