public class Stopwatch {
    private final long start;
    private long stop;

    public Stopwatch() {
        this.start = System.currentTimeMillis();
    }

    public void stop() {
        this.stop = System.currentTimeMillis();
    }

    public long getTimeInMs() {
        return this.stop - this.start;
    }
}
