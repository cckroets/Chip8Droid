package com.ckroetsch.chip8;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import Emulation.Emulator;
import chip_8.Chip8Processor;
import chip_8.Chip8Rom;
import chip_8.Display;
import chip_8.Keyboard;


/**
 *
 */
public class PlayRomActivity extends Activity {

  Chip8Processor cpu = Chip8Application.getCPU();
  Emulator emulator = Chip8Application.getEmulator();
  Chip8Rom rom;
  Display display;
  String romName;

  private static int[] controllers = new int[] {
          R.layout.controller1,
          R.layout.controller2,
          R.layout.controller3,
          R.layout.controller4
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_play_rom);

    romName = getIntent().getStringExtra(ChooseRomActivity.ROM_KEY);
    rom = Chip8Application.getRomFactory().getRoms().get(romName);
    int cid = controllers[rom.getKeyboard().getNumKeys() - 1];

    ViewStub stub = (ViewStub) findViewById(R.id.controller);
    stub.setLayoutResource(cid);
    stub.inflate();

    display = (Display)findViewById(R.id.display);
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
    setupButtons();
    Log.w("PLAY", "romName=" + romName);
    Chip8Rom rom = Chip8Application.getRomFactory().getRoms().get(romName);
    emulator.reset(rom);
    display.start();
  }

  private void setupButtons()
  {
    View v = findViewById(R.id.button_nest);
    ArrayList<View> touchables = v.getTouchables();
    final Keyboard keyboard = rom.getKeyboard();

    for (int i = 0; i < touchables.size(); i++) {
      Button b = (Button) touchables.get(i);
      final int button = i;
      final Toast toast = Toast.makeText(this,"HELLO", Toast.LENGTH_SHORT);
      b.setOnTouchListener(new View.OnTouchListener()
      {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent)
        {
          toast.show();
          if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            keyboard.keyPressed(button);
          } else {
            keyboard.keyReleased(button);
          }
          return true;
        }
      });
    }
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    display.stop();
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
