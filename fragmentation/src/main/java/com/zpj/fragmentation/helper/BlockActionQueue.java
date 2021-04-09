package com.zpj.fragmentation.helper;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.zpj.fragmentation.ISupportFragment;
import com.zpj.fragmentation.SupportHelper;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Z-P-J
 */
public class BlockActionQueue {

    private static final String TAG = "BlockActionQueue";

    private final Queue<Action> mQueue = new LinkedList<>();
    private final AtomicBoolean start = new AtomicBoolean(false);

    private final Handler mHandler;

    public abstract static class Action {

        public long delay = 0;

        public abstract void run();

    }

    public BlockActionQueue(Handler handler) {
        this.mHandler = handler;
    }

    public boolean isStart() {
        return start.get();
    }

    public void start() {
        start.set(true);
        handleAction();
    }

    public void stop() {
        start.set(false);
    }

    public void onDestroy() {
        start.set(false);
        this.mQueue.clear();
    }

    public void post(final Runnable runnable) {
        enqueue(new Action() {
            @Override
            public void run() {
                runnable.run();
            }
        });
    }

    public void postDelayed(final Runnable runnable, long delay) {
        Action action = new Action() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        action.delay = delay;
        enqueue(action);
    }

    public void enqueue(final Action action) {
        mHandler.post(() -> {
            mQueue.add(action);
            Log.d(TAG, "size=" + mQueue.size());
            if (mQueue.size() == 1) {
                handleAction();
            }
        });
    }

    private void handleAction() {
        if (!start.get() || mQueue.isEmpty()) return;

        final Action action = mQueue.peek();
        mHandler.postDelayed(() -> {
            action.run();
            mQueue.poll();
            handleAction();
        }, action.delay);
    }

}
