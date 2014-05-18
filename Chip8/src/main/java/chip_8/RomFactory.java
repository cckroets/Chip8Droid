package chip_8;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import chip_8.android.IOUtil;


/**
 * @author ckroetsc
 */
public class RomFactory
{
  private Map<String,Chip8Rom> romConf;

  public RomFactory()
  {
    this.romConf = readRomConfig();
  }

  private static Map<String,Chip8Rom> readRomConfig()
  {
    Map<String,Chip8Rom> romMap = new HashMap<String, Chip8Rom>();
    InputStream in = IOUtil.openRomConfig();
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
