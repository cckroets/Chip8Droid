package com.ckroetsch.chip8;

import com.ckroetsch.chip8.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import Emulation.Emulator;
import chip_8.Chip8Processor;
import chip_8.Chip8Rom;
import chip_8.Display;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class PlayRomActivity extends Activity {

  Chip8Processor cpu = Chip8Application.getCPU();
  Emulator emulator = Chip8Application.getEmulator();
  Display display;
  String romName = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_play_rom);
    display = (Display)findViewById(R.id.display);
    romName = getIntent().getStringExtra(ChooseRomActivity.ROM_KEY);
  }

  @Override
  protected void onStart()
  {
    super.onStart();
  }

  @Override
  protected void onRestart()
  {
    super.onRestart();
  }

  @Override
  protected void onPostResume()
  {
    super.onPostResume();
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    Log.w("PLAY", "romName=" + romName);
    Chip8Rom rom = Chip8Application.getRomFactory().getRoms().get(romName);
    Log.w("PLAY", "rom=" + rom);
    emulator.reset(rom);
    display.startGame();
  }

  @Override
  protected void onPause()
  {
    super.onPause();
  }

  @Override
  protected void onStop()
  {
    super.onStop();
  }

  @Override
  protected void onDestroy()
  {
    super.onDestroy();
  }
}
