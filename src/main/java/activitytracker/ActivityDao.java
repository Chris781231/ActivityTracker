package activitytracker;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityDao {

    private MariaDbDataSource dataSource;

    public ActivityDao(MariaDbDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Activity saveActivity(Activity activity) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                conn.setAutoCommit(false);
                long activityId = insertActivity(activity, conn);
                List<Trackpoint> trackpoints = insertTrackpoints(activityId, activity, conn);
                conn.commit();
                activity.setId(activityId);
                activity.setTrackpoints(trackpoints);
                return activity;
            } catch (SQLException sqle) {
                conn.rollback();
                throw new IllegalStateException("Cannot create transaction", sqle);
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query", sqle);
        }
    }

    private long insertActivity(Activity activity, Connection conn) {
        String query = "INSERT INTO `activities` (`startTime`, `activity_desc`, `activity_type`) VALUES (?, ?, ?);";

        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(activity.getStartTime()));
            stmt.setString(2, activity.getDesc());
            stmt.setString(3, activity.getType().name());
            stmt.executeUpdate();

            return getIdByStatement(stmt);

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect", sqle);
        }
    }

    private List<Trackpoint> insertTrackpoints(long activityId, Activity activity, Connection conn) {
        List<Trackpoint> trackpoints = new ArrayList<>();

        String query = "INSERT INTO `track_point` (`activity_id`, `time`, `lat`, `lon`) VALUES (?, ?, ?, ?);";

        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            for (Trackpoint trackpoint : activity.getTrackpoints()) {
                if (isValidTrackpoint(trackpoint)) {
                    stmt.setLong(1, activityId);
                    stmt.setTimestamp(2, Timestamp.valueOf(trackpoint.getTime()));
                    stmt.setDouble(3, trackpoint.getLat());
                    stmt.setDouble(4, trackpoint.getLon());
                    stmt.executeUpdate();

                    long trackpointId = getIdByStatement(stmt);
                    trackpoint.setId(trackpointId);
                    trackpoints.add(trackpoint);
                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot insert", sqle);
        }

        return trackpoints;
    }

    private boolean isValidTrackpoint(Trackpoint trackpoint) {
        if (trackpoint.getLat() < -90 || trackpoint.getLat() > 90 || trackpoint.getLon() < -180 || trackpoint.getLon() > 180) {
            throw new IllegalStateException("Invalid GPS Coordinate");
        }
        return true;
    }

    public Activity findActivityById(long id) {
        String query = "SELECT * FROM `activities` LEFT JOIN `track_point` ON `activities`.`id` = `track_point`.`activity_id` WHERE `activities`.`id` = ?;";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setLong(1, id);

            return getActivityByPreparedStatement(stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect");
        }
    }

    public List<Activity> listActivities() {
        List<Activity> result = new ArrayList<>();
        String query = "SELECT * FROM `activities`";

        try (
                Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            getActivitiesByStatement(result, query, stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect", sqle);
        }

        return result;
    }

    private long getIdByStatement(PreparedStatement stmt) {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new IllegalStateException("Cannot get id");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot get id", sqle);
        }
    }

    private void getActivitiesByStatement(List<Activity> result, String query, Statement stmt) {
        try (ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                result.add(new Activity(
                        rs.getLong("id"),
                        rs.getTimestamp("startTime").toLocalDateTime(),
                        rs.getString("activity_desc"),
                        Type.valueOf(rs.getString("activity_type")),
                        null
                ));
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query", sqle);
        }
    }

    private Activity getActivityByPreparedStatement(PreparedStatement stmt) {
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Activity(
                        rs.getLong("id"),
                        rs.getTimestamp("startTime").toLocalDateTime(),
                        rs.getString("activity_desc"),
                        Type.valueOf(rs.getString("activity_type")),
                        null
                );
            } else {
                throw new IllegalStateException("Cannot find");
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Error by select", sqle);
        }
    }
}
