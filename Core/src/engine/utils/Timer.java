package engine.utils;

import java.util.ArrayList;

public class Timer {

    private static ArrayList<Timer> timers = new ArrayList<>();

    private TimerInterface timerInterface;
    private double millis;
    private int numLoops;
    private long next;
    private double delta=0;

    public static void createTimer(TimerInterface timerInterface, double millis, int numLoops){
        new Timer(timerInterface, millis, numLoops);
    }

    private Timer(TimerInterface timerInterface, double millis, int numLoops){
        timers.add(this);
        if(millis<0)
            millis=0;
        this.millis=millis;
        delta=millis%1;
        this.next = System.currentTimeMillis()+(int)millis;
        this.timerInterface=timerInterface;
        this.numLoops=numLoops;
    }

    public static void tick(){
        long curr = System.currentTimeMillis();
        for(Timer timer:timers){
            if(timer.numLoops!=0 && curr>timer.next){
                double move = timer.millis+timer.delta;
                timer.delta=move%1;
                timer.next += (int)move;
                timer.timerInterface.timerCall();
                if(timer.numLoops>0)
                    timer.numLoops--;
            }
        }
    }

}

