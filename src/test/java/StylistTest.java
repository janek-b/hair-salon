import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.time.LocalDate;

public class StylistTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void stylist_instantiatesCorrectly() {
    Stylist testStylist = new Stylist("Becky");
    assertTrue(testStylist instanceof Stylist);
  }

  @Test
  public void getName_returnsStylistName_Becky() {
    Stylist testStylist = new Stylist("Becky");
    assertEquals("Becky", testStylist.getName());
  }

  @Test
  public void equals_ifStylistNameIsTheSame_true() {
    Stylist testStylist1 = new Stylist("Becky");
    Stylist testStylist2 = new Stylist("Becky");
    assertTrue(testStylist1.equals(testStylist2));
  }

  @Test
  public void all_returnsAllStylistsFromDB_true() {
    Stylist testStylist1 = new Stylist("Becky");
    testStylist1.save();
    Stylist testStylist2 = new Stylist("Martha");
    testStylist2.save();
    assertTrue(Stylist.all().get(0).equals(testStylist1));
    assertTrue(Stylist.all().get(1).equals(testStylist2));
  }

  @Test
  public void save_savesStylistToDB_true() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    assertTrue(Stylist.all().get(0).equals(testStylist));
  }

  @Test
  public void save_assignsIdToStylist_true() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    assertEquals(testStylist.getId(), Stylist.all().get(0).getId());
  }

  @Test
  public void getId_returnsAnId_true() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    assertTrue(testStylist.getId() > 0);
  }

  @Test
  public void find_returnsStylistWithSameId_Becky() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    assertEquals(testStylist, Stylist.find(testStylist.getId()));
  }

  @Test
  public void getClients_returnsAllClientsWithSameStylistId_List() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    Client testClient1 = new Client("Jessica", testStylist.getId());
    testClient1.save();
    Client testClient2 = new Client("Joe", testStylist.getId());
    testClient2.save();
    Client[] clients = new Client[] {testClient1, testClient2};
    assertTrue(testStylist.getClients().containsAll(Arrays.asList(clients)));
  }

  @Test
  public void updateStylist_updatesStylistProperties_true() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    testStylist.updateStylist("Jennifer");
    assertEquals("Jennifer", Stylist.find(testStylist.getId()).getName());
    assertEquals("Jennifer", testStylist.getName());
  }

  @Test
  public void getLowestClientCount_returnsStylistWithTheLowestClientCount_testStylist2() {
    Stylist testStylist1 = new Stylist("Becky");
    testStylist1.save();
    Stylist testStylist2 = new Stylist("Martha");
    testStylist2.save();
    Client testClient1 = new Client("Jessica", testStylist1.getId());
    testClient1.save();
    Client testClient2 = new Client("Joe", testStylist1.getId());
    testClient2.save();
    Client testClient3 = new Client("Bob", testStylist2.getId());
    testClient3.save();
    assertTrue(Stylist.getLowestClientCount().equals(testStylist2));
  }


  @Test
  public void deleteStylist_deletesStylistFromDB() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    int testStylistId = testStylist.getId();
    testStylist.deleteStylist();
    assertEquals(null, Stylist.find(testStylistId));
  }

  @Test
  public void deleteStylist_deletesStylistFromDBAndReassignsStylistsClients() {
    Stylist testStylist1 = new Stylist("Becky");
    testStylist1.save();
    Stylist testStylist2 = new Stylist("Martha");
    testStylist2.save();
    Client testClient1 = new Client("Jessica", testStylist1.getId());
    testClient1.save();
    Client testClient2 = new Client("Joe", testStylist1.getId());
    testClient2.save();
    Client testClient3 = new Client("Bob", testStylist2.getId());
    testClient3.save();
    int testStylist1Id = testStylist1.getId();
    testStylist1.deleteStylist();
    assertEquals(null, Stylist.find(testStylist1Id));
    assertEquals(testStylist2.getId(), Client.find(testClient1.getId()).getStylistId());
    assertEquals(testStylist2.getId(), Client.find(testClient2.getId()).getStylistId());
    assertEquals(testStylist2.getId(), Client.find(testClient3.getId()).getStylistId());
  }

  @Test
  public void getUpcomingAppointments_returnsAllAppointmentsForAStylist() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    Appointment testAppointment1 = new Appointment(1, testStylist.getId(), LocalDate.now().plusDays(2).toString(), "16:30:00");
    testAppointment1.save();
    Appointment testAppointment2 = new Appointment(1, testStylist.getId(), LocalDate.now().plusDays(9).toString(), "16:30:00");
    testAppointment2.save();
    Appointment testAppointment3 = new Appointment(1, testStylist.getId(), LocalDate.now().minusDays(9).toString(), "01:30:00");
    testAppointment3.save();
    assertTrue(testStylist.getUpcomingAppointments().get(0).equals(testAppointment1));
    assertTrue(testStylist.getUpcomingAppointments().get(1).equals(testAppointment2));
    assertFalse(testStylist.getUpcomingAppointments().contains(testAppointment3));
  }

  @Test
  public void getPastAppointments_returnsAllAppointmentsForAStylist() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    Appointment testAppointment1 = new Appointment(1, testStylist.getId(), LocalDate.now().minusDays(1).toString(), "16:30:00");
    testAppointment1.save();
    Appointment testAppointment2 = new Appointment(1, testStylist.getId(), LocalDate.now().minusDays(9).toString(), "16:30:00");
    testAppointment2.save();
    Appointment testAppointment3 = new Appointment(1, testStylist.getId(), LocalDate.now().plusDays(9).toString(), "01:30:00");
    testAppointment3.save();
    assertTrue(testStylist.getPastAppointments().get(0).equals(testAppointment1));
    assertTrue(testStylist.getPastAppointments().get(1).equals(testAppointment2));
    assertFalse(testStylist.getPastAppointments().contains(testAppointment3));
  }

  @Test
  public void getAllAppointments_returnsAllAppointmentsForAStylist() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    Appointment testAppointment1 = new Appointment(1, testStylist.getId(), LocalDate.now().minusDays(1).toString(), "16:30:00");
    testAppointment1.save();
    Appointment testAppointment2 = new Appointment(1, testStylist.getId(), LocalDate.now().minusDays(9).toString(), "16:30:00");
    testAppointment2.save();
    Appointment[] appointments = new Appointment[] {testAppointment1, testAppointment2};
    assertTrue(testStylist.getAllAppointments().containsAll(Arrays.asList(appointments)));
  }

  @Test
  public void timeslotAvailable_ifGivenTimeslotIsNotTaken_true() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    Appointment testAppointment1 = new Appointment(1, testStylist.getId(), "2017-03-16", "16:30:00");
    testAppointment1.save();
    assertTrue(testStylist.timeslotAvailable("2017-03-16", "16:00:00"));
    assertFalse(testStylist.timeslotAvailable("2017-03-16", "16:30:00"));
  }

  @Test
  public void searchStylist_returnsAllStylistsWithMatchingName_List() {
    Stylist testStylist1 = new Stylist("Becky");
    testStylist1.save();
    Stylist testStylist2 = new Stylist("Martha");
    testStylist2.save();
    Stylist testStylist3 = new Stylist("Maggie");
    testStylist3.save();
    Stylist[] stylists = new Stylist[] {testStylist2, testStylist3};
    assertTrue(Stylist.searchStylist("ma").containsAll(Arrays.asList(stylists)));
    assertFalse(Stylist.searchStylist("ma").contains(testStylist1));
  }

}
