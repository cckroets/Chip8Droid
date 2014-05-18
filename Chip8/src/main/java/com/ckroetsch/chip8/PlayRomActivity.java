package com.ckroetsch.chip8;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;

import Emulation.Emulator;
import chip_8.Chip8Rom;
import chip_8.Display;
import chip_8.android.AndroidDisplay;
import chip_8.Keyboard;


/**
 *
 */
public class PlayRomActivity extends Activity {

  Emulator emulator = Chip8Application.getEmulator();
  Chip8Rom rom;
  Display display;
  String romName;
  boolean started = false;

  private static int[] controllers = new int[] {
          R.layout.controller1,
          R.layout.controller2,
          R.layout.controller3,
          R.layout.controller4
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.w("ACTION", "CREATE");
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
    Log.w("ACTION", "START");
  }

  @Override
  protected void onRestart()
  {
    super.onRestart();
    Log.w("ACTION", "RESTART");
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
    Log.w("ACTION", "RESUME");
    Log.d("PLAY", "romName=" + romName);
    resumePlay();
  }

  private void resumePlay()
  {
    if (started) {
      emulator.resume();
    } else {
      emulator.reset(rom);
      started = true;
    }
    display.start();
  }

  private void pausePlay()
  {
    emulator.pause();
    display.stop();
  }

  private void setupButtons()
  {
    View v = findViewById(R.id.controls);
    ArrayList<View> touchables = v.getTouchables();
    final Keyboard keyboard = rom.getKeyboard();

    for (int i = 0; i < touchables.size(); i++) {
      Button b = (Button) touchables.get(i);
      final int button = i;
      b.setOnTouchListener(new View.OnTouchListener()
      {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent)
        {
          if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            keyboard.keyPressed(button);
          } else {
            keyboard.keyReleased(button);
          }
          return true;
        }
      });
    }

    Button save = (Button) findViewById(R.id.save_button);
    Button load = (Button) findViewById(R.id.load_button);
    Button reset = (Button) findViewById(R.id.reset_button);

    save.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        emulator.save();
      }
    });
    load.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        emulator.load();
      }
    });
    reset.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        emulator.reset();
      }
    });
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    Log.w("ACTION", "PAUSE");
    pausePlay();
  }

  @Override
  protected void onStop()
  {
    super.onStop();
    Log.w("ACTION", "STOP");
  }

  @Override
  protected void onDestroy()
  {
    super.onDestroy();
    Log.w("ACTION", "DESTROY");
  }
}
