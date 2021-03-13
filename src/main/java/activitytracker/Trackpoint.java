package activitytracker;

import java.time.LocalDateTime;

public class Trackpoint {

    private long id;

    private final LocalDateTime time;
    private final double lat;
    private final double lon;
    public Trackpoint(LocalDateTime time, double lat, double lon) {
        this.time = time;
        this.lat = lat;
        this.lon = lon;
    }

    public Trackpoint(long id, LocalDateTime time, double lat, double lon) {
        this.id = id;
        this.time = time;
        this.lat = lat;
        this.lon = lon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "Trackpoint{" +
                "id=" + id +
                ", time=" + time +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
