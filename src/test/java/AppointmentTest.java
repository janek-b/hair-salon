import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.sql.Time;
import java.sql.Date;

public class AppointmentTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void appointment_instantiatesCorrectly() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30:00");
    assertTrue(testAppointment instanceof Appointment);
  }

  @Test
  public void getClientId_returnsClientId_1() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30:00");
    assertEquals(1, testAppointment.getClientId());
  }

  @Test
  public void getStylistId_returnsStylistId_1() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30:00");
    assertEquals(1, testAppointment.getStylistId());
  }

  @Test
  public void getDate_returnsCorrectDate_Date() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30:00");
    Date testDate = Date.valueOf("2017-03-16");
    assertEquals(testDate, testAppointment.getDate());
  }

  @Test
  public void getTime_returnsCorrectTime_Time() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30:00");
    Time testTime = Time.valueOf("01:30:00");
    assertEquals(testTime, testAppointment.getTime());
  }

  @Test
  public void equals_ifAllPropertiesAreTheSame_true() {
    Appointment testAppointment1 = new Appointment(1, 1, "2017-03-16", "01:30:00");
    Appointment testAppointment2 = new Appointment(1, 1, "2017-03-16", "01:30:00");
    assertTrue(testAppointment1.equals(testAppointment2));
  }

  @Test
  public void all_returnsAllAppointmentsInDB_List() {
    Appointment testAppointment1 = new Appointment(1, 1, "2017-04-16", "16:30:00");
    testAppointment1.save();
    Appointment testAppointment2 = new Appointment(1, 1, "2017-03-16", "01:30:00");
    testAppointment2.save();
    assertTrue(Appointment.all().get(0).equals(testAppointment1));
    assertTrue(Appointment.all().get(1).equals(testAppointment2));
  }

  @Test
  public void save_savesAppointmentIntoDB_true() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30:00");
    testAppointment.save();
    assertTrue(Appointment.all().get(0).equals(testAppointment));
  }

  @Test
  public void save_assignsIdToAppointment_true() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30:00");
    testAppointment.save();
    assertEquals(testAppointment.getId(), Appointment.all().get(0).getId());
  }

  @Test
  public void getId_returnsAnId_true() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30:00");
    testAppointment.save();
    assertTrue(testAppointment.getId() > 0);
  }

  @Test
  public void find_returnsAppointmentWithSameId_true() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30:00");
    testAppointment.save();
    assertEquals(testAppointment, Appointment.find(testAppointment.getId()));
  }

}
