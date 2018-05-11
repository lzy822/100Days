package android.lzy.a100days;

import org.litepal.crud.DataSupport;

public class impulse extends DataSupport{
    private int time;
    private String lastDay;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getLastDay() {
        return lastDay;
    }

    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }
}
