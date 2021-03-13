package activitytracker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Activity {

    private long id;
    private final LocalDateTime startTime;
    private final String desc;
    private final Type type;
    private List<Trackpoint> trackpoints;

    public Activity(LocalDateTime startTime, String desc, Type type, List<Trackpoint> trackpoints) {
        this.startTime = startTime;
        this.desc = desc;
        this.type = type;
        this.trackpoints = trackpoints;
    }

    public Activity(long id, LocalDateTime startTime, String desc, Type type, List<Trackpoint> trackpoints) {
        this.id = id;
        this.startTime = startTime;
        this.desc = desc;
        this.type = type;
        this.trackpoints = trackpoints;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getDesc() {
        return desc;
    }

    public Type getType() {
        return type;
    }

    public List<Trackpoint> getTrackpoints() {
        return new ArrayList<>(trackpoints);
    }

    public void setTrackpoints(List<Trackpoint> trackpoints) {
        this.trackpoints = trackpoints;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", desc='" + desc + '\'' +
                ", type=" + type +
                '}';
    }
}
