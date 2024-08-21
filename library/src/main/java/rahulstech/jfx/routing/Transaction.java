package rahulstech.jfx.routing;

import rahulstech.jfx.routing.backstack.Backstack;
import rahulstech.jfx.routing.backstack.BackstackEntry;
import rahulstech.jfx.routing.util.Disposable;
import rahulstech.jfx.routing.util.StringUtil;

import java.util.ArrayDeque;
import java.util.Queue;

@SuppressWarnings("unused")
public abstract class Transaction implements Disposable {

    private Backstack<Target> backstack = new Backstack<>();

    private ArrayDeque<Runnable> operations = new ArrayDeque<>();

    private boolean disposed = false;

    protected void enqueueOperation(Runnable operation) {
        operations.add(operation);
    }

    protected Queue<Runnable> getOperationsQueue() {
        return operations;
    }

    protected boolean executePendingOperations() {
        if (operations.isEmpty()) {
            return false;
        }
        while (!operations.isEmpty()) {
            operations.pop().run();
        }
        return true;
    }

    public Backstack<Target> getBackstack() {
        return backstack;
    }

    public abstract void doForcedShow(Target target);

    public abstract void doForcedHide(Target target);

    public abstract void doForcedDestroy(Target target);

    @Override
    public void dispose() {
        if (disposed) {
            // it's already disposed
            return;
        }
        operations.clear();
        operations = null;
        backstack.forEach(Target::onDestroy);
        backstack.dispose();
        backstack = null;
        disposed = true;
    }

    public abstract static class Target implements BackstackEntry {

        private final String tag;

        public Target(String tag) {
            if (StringUtil.isEmpty(tag)) {
                throw new IllegalArgumentException("tag can not be empty");
            }
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }

        public void onCreate() {}

        public void onBeforeShow() {}

        public void onShow() {}

        public void onHide() {}

        public void onDestroy() {}

        @Override
        public void dispose() {}
    }
}
