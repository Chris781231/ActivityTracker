package activitytracker;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ActivityDaoTest {

    private final List<Activity> activities = new ArrayList<>();
    private ActivityDao activityDao;

    @BeforeEach
    void setUp() {
        MariaDbDataSource dataSource;

        try {
            dataSource = new MariaDbDataSource();
            dataSource.setUrl("jdbc:mariadb://localhost:3308/activitytracker?useUnicode=true");
            dataSource.setUser("activitytracker");
            dataSource.setPassword("activitytracker");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot create dataSource", sqle);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        activityDao = new ActivityDao(dataSource);

        List<Trackpoint> trackpoints = new ArrayList<>(List.of(
                new Trackpoint(LocalDateTime.of(2021, 3, 8, 10, 0), 47.12345, 15.12345),
                new Trackpoint(LocalDateTime.of(2021, 3, 8, 11, 0), 47.12347, 15.12347),
                new Trackpoint(LocalDateTime.of(2021, 3, 8, 12, 0), 47.12349, 15.12349)
        ));

        activities.add(new Activity(LocalDateTime.of(2021, 3, 8, 10, 0), "Kocogás a Duna mentén Váctól Sződligetig", Type.RUNNING, trackpoints));
        activities.add(new Activity(LocalDateTime.of(2021, 3, 3, 10, 0), "Bringázás Vác - Nagymaros", Type.BIKING, trackpoints));
        activities.add(new Activity(LocalDateTime.of(2021, 3, 4, 8, 20), "Túrázás Csóványos", Type.HIKING, trackpoints));
        activities.add(new Activity(LocalDateTime.of(2021, 3, 8, 10, 45), "Kosáredzés dunaparti kosárpályán", Type.BASKETBALL, trackpoints));
    }

    @Test
    void saveActivityWithInvalidGPSCoord() {
        List<Trackpoint> trackpoints = List.of(new Trackpoint(LocalDateTime.of(2021, 3, 8, 10, 0), -91.12345, 15.12345));
        Activity activity = new Activity(LocalDateTime.now(), "", Type.BASKETBALL, trackpoints);
        assertThrows(IllegalStateException.class, () -> activityDao.saveActivity(activity));
    }

    @Test
    void saveActivity() {
        Activity expected = activityDao.saveActivity(activities.get(0));
        assertEquals(1L, expected.getId());
    }

    @Test
    void findActivityById() {
        activityDao.saveActivity(activities.get(0));
        Activity expectedActivity = activityDao.findActivityById(1);
        assertEquals(Type.RUNNING, expectedActivity.getType());
        assertEquals(8, expectedActivity.getStartTime().getDayOfMonth());
    }

    @Test
    void listActivities() {
        for (Activity activity : activities) {
            activityDao.saveActivity(activity);
        }

        List<Activity> expected = activityDao.listActivities();
        assertEquals(4, expected.size());
    }
}