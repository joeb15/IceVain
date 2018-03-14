package engine.utils;

import java.util.concurrent.CopyOnWriteArrayList;

public class Timer {

    private static CopyOnWriteArrayList<Timer> timers = new CopyOnWriteArrayList<>();
    private static long start = System.currentTimeMillis();
    private TimerInterface timerInterface;
    private double millis;
    private int numLoops;
    private long next;
    private double delta;
    private boolean destroy = false;

    /**
     * Creates a timer
     *
     * @param timerInterface The callback to call when the timer is triggered
     * @param millis The number of milliseconds between timer calls
     * @param numLoops The number of loops to run, -1 for infinite
     */
    public static void createTimer(TimerInterface timerInterface, double millis, int numLoops){
        new Timer(timerInterface, millis, numLoops);
    }

    /**
     * Creates a timer
     *
     * @param timerInterface The callback to call when the timer is triggered
     * @param millis The number of milliseconds between timer calls
     * @param numLoops The number of loops to run, -1 for infinite
     */
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

    /**
     * Handles all timer instances
     */
    public static void tick() {
        for (Timer timer : timers)
            timer.check();
    }

    /**
     * Checks whether the timer should be called or destroyed
     */
    private void check(){
        long curr = System.currentTimeMillis();
        while(numLoops!=0 && curr>next){
            double move = millis+delta;
            delta=move%1;
            next += (int)move;
            timerInterface.timerCall();
            if(numLoops==0)
                timers.remove(this);
            if(numLoops>0) {
                numLoops--;
            }
            if(millis==0)
                break;
        }
        if(numLoops==0)
            timers.remove(this);
    }

    /**
     * Gets the current time the game has been running
     *
     * @return The current time in seconds
     */
    public static float getTime() {
        return (System.currentTimeMillis()-start)/1000f;
    }

    /**
     * Runs something as a side process
     *
     * @param sideProcess The process to run on the side
     */
    public static void runAsSideProcess(Runnable sideProcess) {
        new Thread(sideProcess).start();
    }
}

