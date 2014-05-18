package Emulation;

/**
 * Created by curtiskroetsch on 2014-05-17.
 */
public interface ActionListener
{
  public void onLoad(Rom rom, boolean found);

  public void onSave(Rom rom);
}
