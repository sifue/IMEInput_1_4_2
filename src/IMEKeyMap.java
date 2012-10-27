
import java.util.HashMap;
import java.util.Map;

public class IMEKeyMap
{
  private static final Map awt2lwjgl = new HashMap();

  public int awt2lwjgl(int keyCode)
  {
    Integer key = (Integer)awt2lwjgl.get(Integer.valueOf(keyCode));

    if (key != null) {
      return key.intValue();
    }
    return 0;
  }

  public int lwjgl2awt(int keyCode)
  {
    throw new UnsupportedOperationException();
  }

  public boolean contains(int awtKey) {
    return awt2lwjgl.containsKey(Integer.valueOf(awtKey));
  }

  static
  {
    awt2lwjgl.put(Integer.valueOf(27), Integer.valueOf(1));
    awt2lwjgl.put(Integer.valueOf(10), Integer.valueOf(28));
    awt2lwjgl.put(Integer.valueOf(8), Integer.valueOf(14));
    awt2lwjgl.put(Integer.valueOf(38), Integer.valueOf(200));
    awt2lwjgl.put(Integer.valueOf(40), Integer.valueOf(208));
    awt2lwjgl.put(Integer.valueOf(37), Integer.valueOf(-1));
    awt2lwjgl.put(Integer.valueOf(39), Integer.valueOf(-1));
  }
}