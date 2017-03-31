import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.sql.Time;
import java.sql.Date;
import java.util.Arrays;

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
    // Date testDate = Date.valueOf("2017-03-16");
    assertEquals("2017-03-16", testAppointment.getDate());
  }

  @Test
  public void getTime_returnsCorrectTime_Time() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30:00");
    // Time testTime = Time.valueOf("01:30:00");
    assertEquals("01:30:00", testAppointment.getTime());
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

  @Test
  public void updateAppointment_updatesAppointmentProperties_true() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30:00");
    testAppointment.save();
    testAppointment.updateAppointment("2017-04-16", "09:30:00");
    assertEquals("2017-04-16", testAppointment.getDate());
    assertEquals("2017-04-16", Appointment.find(testAppointment.getId()).getDate());
    assertEquals("09:30:00", testAppointment.getTime());
    assertEquals("09:30:00", Appointment.find(testAppointment.getId()).getTime());
  }

  @Test
  public void deleteAppointment_removesAppointmentFromDB_true() {
    Appointment testAppointment = new Appointment(1, 1, "2017-03-16", "01:30:00");
    testAppointment.save();
    int testAppointmentId = testAppointment.getId();
    testAppointment.deleteAppointment();
    assertEquals(null, Appointment.find(testAppointmentId));
  }

  @Test
  public void getFormatDate_returnsDateInFormattedString() {
    Appointment testAppointment = new Appointment(1, 1, "2017-04-13", "15:30:00");
    testAppointment.save();
    assertEquals("Apr 13, 2017", testAppointment.getFormatDate());
  }

  @Test
  public void getFormatTime_returnsTimeInFormattedString() {
    Appointment testAppointment = new Appointment(1, 1, "2017-04-13", "15:30:00");
    testAppointment.save();
    assertEquals("3:30 PM", testAppointment.getFormatTime());
  }

  @Test
  public void getDaysAppointment_returnsAllAppointmentsOnGivenDay_List() {
    Appointment testAppointment1 = new Appointment(1, 2, "2017-03-16", "16:30:00");
    testAppointment1.save();
    Appointment testAppointment2 = new Appointment(1, 1, "2017-03-16", "01:30:00");
    testAppointment2.save();
    Appointment testAppointment3 = new Appointment(1, 1, "2017-03-18", "01:30:00");
    testAppointment3.save();
    Appointment[] appointments = new Appointment[] {testAppointment1, testAppointment2};
    assertTrue(Appointment.getDaysAppointment("2017-03-16").containsAll(Arrays.asList(appointments)));
    assertFalse(Appointment.getDaysAppointment("2017-03-16").contains(testAppointment3));
  }

  @Test
  public void getStylistName_returnsNameOfAssignedStylist_Becky() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    Client testClient = new Client("Jessica", testStylist.getId());
    testClient.save();
    Appointment testAppointment = new Appointment(testClient.getId(), testStylist.getId(), "2017-04-13", "15:30:00");
    testAppointment.save();
    assertEquals("Becky", testAppointment.getStylistName());
  }

  @Test
  public void getClientName_returnsNameOfAssignedClient_Jessica() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    Client testClient = new Client("Jessica", testStylist.getId());
    testClient.save();
    Appointment testAppointment = new Appointment(testClient.getId(), testStylist.getId(), "2017-04-13", "15:30:00");
    testAppointment.save();
    assertEquals("Jessica", testAppointment.getClientName());
  }

}
