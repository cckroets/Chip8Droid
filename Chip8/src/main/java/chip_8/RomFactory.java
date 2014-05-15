package chip_8;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import com.ckroetsch.chip8.R;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * @author ckroetsc
 */
public class RomFactory
{


  private Map<String,Chip8Rom> romConf;

  public RomFactory(Context context)
  {
    this.romConf = readRomConfig(context);
  }

  private static Map<String,Chip8Rom> readRomConfig(Context c)
  {
    Map<String,Chip8Rom> romMap = new HashMap<String, Chip8Rom>();
    InputStream in = c.getResources().openRawResource(R.raw.rominfo);
    try {
        byte[] res = Utils.streamToBytes(in);
        String json = new String(res, "UTF-8");

        JSONArray romArray = new JSONArray(json);

        for (int i = 0; i < romArray.length(); i++) {
            JSONObject jsonRom = (JSONObject) romArray.get(i);
            String displayName = jsonRom.getString("name");
            String filename = jsonRom.getString("filename");
            JSONArray keys = jsonRom.getJSONArray("keyMap");
            int[] keyMap = jsonArrayToIntArray(keys);
            Chip8Rom rom = new Chip8Rom(filename, keyMap);
            romMap.put(displayName, rom);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return romMap;
  }

  private static int[] jsonArrayToIntArray(JSONArray jarray) throws Exception
  {
      int[] result = new int[jarray.length()];
      for (int i = 0; i < jarray.length(); i++) {
          result[i] = Integer.parseInt(jarray.get(i).toString());
      }
      return result;
  }

  public String[] getRomNames()
  {
      return romConf.keySet().toArray(new String[romConf.size()]);
  }

  public Map<String, Chip8Rom> getRoms()
  {
    return romConf;
  }

}
