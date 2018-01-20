package engine.utils;

import java.util.ArrayList;

public class ThreadHandler {

    private static ArrayList<Thread> threadList = new ArrayList<>();

    public static void newThread(String threadName, ThreadInterface threadInterface) {
        Thread thread = new Thread(() -> threadInterface.callback());
        threadList.add(thread);
        thread.setName(threadName);
        thread.start();
    }
}
