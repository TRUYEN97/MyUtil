/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Time.WaitTime;

/**
 *
 * @author Administrator
 */
public abstract class AbsTime {

    private double startTime = 0;
    private double spec = 0;
    private boolean run = false;
    private double endTime = 0;

    protected AbsTime() {
    }

    protected AbsTime(double spec) {
        start(spec);
    }

    protected abstract double getTimeCurrent();

    public final void start(double spec) {
        this.run = true;
        this.spec = spec;
        startTime = getTimeCurrent();
    }

    public double getSpec() {
        return spec;
    }

    public boolean onTime() {
        return getTime() < spec;
    }

    public double getTime() {
        if (isRun()) {
            return getTimeCurrent() - startTime;
        }
        return endTime;
    }

    public void update() {
        startTime = getTimeCurrent();
    }

    public boolean isOverTime(int time) {
        return getTime() >= time;
    }

    public boolean isRun() {
        return run;
    }

    public void stop() {
        this.endTime = getTime();
        this.run = false;
    }
}
