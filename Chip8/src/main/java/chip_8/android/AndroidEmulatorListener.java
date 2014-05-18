package chip_8.android;

import android.widget.Toast;

import com.ckroetsch.chip8.Chip8Application;
import com.ckroetsch.chip8.R;

import Emulation.ActionListener;
import Emulation.Rom;

/**
 * Created by curtiskroetsch on 2014-05-17.
 */
public class AndroidEmulatorListener implements ActionListener
{
  private Toast toast = Toast.makeText(Chip8Application.getContext(),"",Toast.LENGTH_SHORT);

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

  public void showToast(int id)
  {
    toast.setText(id);
    toast.show();
  }
}
