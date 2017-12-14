import com.sun.security.auth.module.NTSystem;
import engine.utils.Config;
import engine.utils.Debug;
import engine.utils.GlobalVars;
import engine.utils.VFS;

import java.net.InetAddress;

public class Client {

    public Client(){
        VFS.createVFS(GlobalVars.GAME_FOLDER, "/");

        Debug.error("Config file has been loaded from:");
        Debug.error(VFS.getFile(GlobalVars.CONFIG_FILE).getPath());

        Debug.log(GlobalVars.getGameNameAndVersion());
        Debug.log(Config.getInt("max_fps"));

        Debug.log(VFS.getFile("/config.cfg").getPath());
    }

    public static void main(String args[]){
        new Client();
    }
}
