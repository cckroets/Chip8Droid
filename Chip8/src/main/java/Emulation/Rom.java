package Emulation;


import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;


/**
 * @author ckroetsc
 */
public interface Rom
{

  DataInputStream getLoadStream();

  DataOutputStream getSaveStream();
}
