/*
package activitytracker;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityTrackerMain {


    public static void main(String[] args) {
        ActivityTrackerMain atMain = new ActivityTrackerMain();
        Activity activity = new Activity(0, LocalDateTime.of(2021, 3, 8, 10, 0), "Kocogás a Duna mentén Váctól Sződligetig", Type.RUNNING);

        MariaDbDataSource dataSource;

        try {
            dataSource = new MariaDbDataSource();
            dataSource.setUrl("jdbc:mariadb://localhost:3308/activitytracker?useUnicode=true");
            dataSource.setUser("activitytracker");
            dataSource.setPassword("activitytracker");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot create dataSource", sqle);
        }

        atMain.saveActivity(activity, dataSource);
        System.out.println(atMain.findActivityById(1, dataSource));
        System.out.println(atMain.listActivities(dataSource));
    }

    private void saveActivity(Activity activity, MariaDbDataSource dataSource) {
        String query = "INSERT INTO `activities` (`startTime`, `activity_desc`, `activity_type`) VALUES (?, ?, ?);";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setTimestamp(1, Timestamp.valueOf(activity.getStartTime()));
            stmt.setString(2, activity.getDesc());
            stmt.setString(3, activity.getType().name());
            int i = stmt.executeUpdate();
            System.out.println(i);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect", sqle);
        }
    }

    private Activity findActivityById(long id, MariaDbDataSource dataSource) {
        String query = "SELECT * FROM `activities` WHERE `id` = ?;";

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

    private List<Activity> listActivities(MariaDbDataSource dataSource) {
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

    private void getActivitiesByStatement(List<Activity> result, String query, Statement stmt) {
        try (ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                result.add(new Activity(
                        rs.getLong("id"),
                        rs.getTimestamp("startTime").toLocalDateTime(),
                        rs.getString("activity_desc"),
                        Type.valueOf(rs.getString("activity_type"))
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
                        Type.valueOf(rs.getString("activity_type"))
                );
            } else {
                throw new IllegalStateException("Cannot find");
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Error by select", sqle);
        }
    }
}
*/
