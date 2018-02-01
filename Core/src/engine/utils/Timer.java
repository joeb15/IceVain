package engine.utils;

import java.util.concurrent.CopyOnWriteArrayList;

public class Timer {

    private static CopyOnWriteArrayList<Timer> timers = new CopyOnWriteArrayList<>();
    private static long start = System.currentTimeMillis();
    private TimerInterface timerInterface;
    private double millis;
    private int numLoops;
    private long next;
    private double delta=0;

    public static void createTimer(TimerInterface timerInterface, double millis, int numLoops){
        new Timer(timerInterface, millis, numLoops);
    }
    private Timer(TimerInterface timerInterface, double millis, int numLoops) {
        timers.add(this);
        if(millis<0)
            millis=0;
        this.millis=millis;
        delta=millis%1;
        this.next = System.currentTimeMillis()+(int)millis;
        this.timerInterface=timerInterface;
        this.numLoops=numLoops;
    }

    public static void tick() {
        for (Timer timer : timers)
            timer.check();
    }

    private void check(){
        long curr = System.currentTimeMillis();
        while(numLoops!=0 && curr>next){
            double move = millis+delta;
            delta=move%1;
            next += (int)move;
            timerInterface.timerCall();
            if(numLoops>0)
                numLoops--;
            if(millis==0)
                break;
        }
    }

    public static float getTime() {
        float time = (System.currentTimeMillis()-start)/1000f;
        return time;
    }
}

