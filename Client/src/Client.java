import engine.utils.*;

import static engine.utils.GlobalVars.CFG_FPS_MAX;

public class Client {

    private int fps=0,tps=0;

    public Client(){
        initialize();

        Debug.error("Config file has been loaded from:");
        Debug.error(VFS.getFile(GlobalVars.CONFIG_FILE).getPath());

        Timer.createTimer(()->{tps++;tick();}, 1000.0/60, -1);
        Timer.createTimer(()->{fps++;render();}, 1000.0/Config.getInt(CFG_FPS_MAX), -1);
        Timer.createTimer(()->{
            Debug.log("FPS:"+fps+" TPS:"+tps);
            fps=0;tps=0;
        }, 1000, -1);

        int i=0;
        while(1>i){
            Timer.tick();
        }

        cleanUp();
    }

    private void render(){

    }

    private void tick(){

    }

    private void initialize(){
        VFS.initializeVirtualSystems();
    }

    private void cleanUp(){

    }

    public static void main(String args[]){
        new Client();
    }
}
