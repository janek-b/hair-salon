import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.sql.Timestamp;
import java.sql.Date;

public class AppointmentTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void appointment_instantiatesCorrectly() {
    // Timestamp testTimestamp = new Timestamp(Date.valueOf("2017-03-16T01:30").getTime());
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30");
    assertTrue(testAppointment instanceof Appointment);
  }

  @Test
  public void getClientId_returnsClientId_1() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30");
    assertEquals(1, testAppointment.getClientId());
  }

  @Test
  public void getStylistId_returnsStylistId_1() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30");
    assertEquals(1, testAppointment.getStylistId());
  }
}
