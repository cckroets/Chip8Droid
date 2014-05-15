package com.ckroetsch.chip8;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import chip_8.Chip8Rom;
import chip_8.RomFactory;


public class ChooseRomActivity extends ListActivity {

  public static final String ROM_KEY = "ROM";

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choose_rom);
    RomFactory factory = new RomFactory(this);
    ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.rom_layout, factory.getRomNames());
    setListAdapter(adapter);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.choose_rom, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id)
  {
    super.onListItemClick(l, v, position, id);
    TextView tv = (TextView) v;
    String romName = tv.getText().toString();
    Intent playIntent = new Intent(this, PlayRomActivity.class);
    playIntent.putExtra(ROM_KEY,romName);
    startActivity(playIntent);
  }
}
