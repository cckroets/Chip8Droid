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

import Emulation.ActionListener;
import Emulation.Emulator;
import Emulation.Rom;
import chip_8.Chip8Rom;
import chip_8.Display;
import chip_8.android.AndroidDisplay;
import chip_8.Keyboard;
import chip_8.android.Chip8Driver;
import chip_8.android.RomFactory;


/**
 *
 */
public class PlayRomActivity extends Activity  implements ActionListener {

  Chip8Driver driver;

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

    String romName = getIntent().getStringExtra(ChooseRomActivity.ROM_KEY);
    Chip8Rom rom = RomFactory.get().getRoms().get(romName);
    int cid = controllers[rom.getKeyboard().getNumKeys() - 1];

    ViewStub stub = (ViewStub) findViewById(R.id.controller);
    stub.setLayoutResource(cid);
    stub.inflate();

    Chip8Driver.setDisplay((Display)findViewById(R.id.display));
    driver = Chip8Driver.get();
    driver.addActionListener(this);
    driver.initializeWithRom(rom);
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    setupButtons();
    driver.setFocus(true);
  }

  private void setupButtons()
  {
    View v = findViewById(R.id.controls);
    ArrayList<View> touchables = v.getTouchables();
    final Keyboard keyboard = driver.getKeyboard();

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
    findViewById(R.id.save_button).setOnClickListener(driver.getOnSaveListener());
    findViewById(R.id.load_button).setOnClickListener(driver.getOnLoadListener());
    findViewById(R.id.reset_button).setOnClickListener(driver.getOnResetListener());
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    driver.setFocus(false);
  }

  @Override
  protected void onDestroy()
  {
    super.onDestroy();
    driver.cleanup();
  }

  @Override
  public void onLoad(Rom rom, boolean found)
  {
    showToast(found ? R.string.load_success : R.string.load_failure);
  }

  @Override
  public void onSave(Rom rom)
  {
    showToast(R.string.save_success);
  }


  public void showToast(final int id)
  {
    this.runOnUiThread(new Runnable()
    {
      @Override
      public void run()
      {
        Toast.makeText(Chip8Application.getContext(), id, Toast.LENGTH_SHORT).show();
      }
    });
  }
}
