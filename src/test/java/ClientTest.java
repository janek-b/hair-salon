import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.time.LocalDate;

public class ClientTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void client_instantiatesCorrectly() {
    Client testClient = new Client("Jessica", 1);
    assertTrue(testClient instanceof Client);
  }

  @Test
  public void getName_returnsClientName_Jessica() {
    Client testClient = new Client("Jessica", 1);
    assertEquals("Jessica", testClient.getName());
  }

  @Test
  public void getStylistId_returnsClientStylistId_1() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    Client testClient = new Client("Jessica", testStylist.getId());
    assertEquals(testStylist.getId(), testClient.getStylistId());
  }

  @Test
  public void equals_ifClientNameAndStylistIdIsTheSame_true() {
    Client testClient1 = new Client("Jessica", 1);
    Client testClient2 = new Client("Jessica", 1);
    assertTrue(testClient1.equals(testClient2));
  }

  @Test
  public void all_returnsAllClientsFromDB_true() {
    Client testClient1 = new Client("Jessica", 1);
    testClient1.save();
    Client testClient2 = new Client("Joe", 1);
    testClient2.save();
    assertTrue(Client.all().get(0).equals(testClient1));
    assertTrue(Client.all().get(1).equals(testClient2));
  }

  @Test
  public void save_savesClientToDB_true() {
    Client testClient = new Client("Jessica", 1);
    testClient.save();
    assertTrue(Client.all().get(0).equals(testClient));
  }

  @Test
  public void save_assignsIdToClient_true() {
    Client testClient = new Client("Jessica", 1);
    testClient.save();
    assertEquals(testClient.getId(), Client.all().get(0).getId());
  }

  @Test
  public void getId_returnsAnId_true() {
    Client testClient = new Client("Jessica", 1);
    testClient.save();
    assertTrue(testClient.getId() > 0);
  }

  @Test
  public void find_returnsCLientWithSameId_Jessica() {
    Client testClient = new Client("Jessica", 1);
    testClient.save();
    assertEquals(testClient, Client.find(testClient.getId()));
  }

  @Test
  public void updateClient_updatesClientProperties_true() {
    Client testClient = new Client("Jessica", 1);
    testClient.save();
    testClient.updateClient("Jennifer", 2);
    assertEquals("Jennifer", Client.find(testClient.getId()).getName());
    assertEquals("Jennifer", testClient.getName());
    assertEquals(2, Client.find(testClient.getId()).getStylistId());
    assertEquals(2, testClient.getStylistId());
  }

  @Test
  public void deleteClient_removesClientFromDB_true() {
    Client testClient = new Client("Jessica", 1);
    testClient.save();
    int testClientId = testClient.getId();
    testClient.deleteClient();
    assertEquals(null, Client.find(testClientId));
  }

  @Test
  public void assignStylist_setsStylistIdEqualsToStylistWithLeastNumberOfClients_true() {
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
    Client testClient4 = new Client("Jeff", testStylist1.getId());
    testClient4.save();
    testClient4.assignStylist();
    assertEquals(testStylist2.getId(), Client.find(testClient4.getId()).getStylistId());
    assertEquals(testStylist2.getId(), testClient4.getStylistId());
  }

  @Test
  public void getStylistName_returnsNameOfAssignedStylist_Becky() {
    Stylist testStylist = new Stylist("Becky");
    testStylist.save();
    Client testClient = new Client("Jessica", testStylist.getId());
    testClient.save();
    assertEquals("Becky", testClient.getStylistName());
  }

  @Test
  public void getUpcomingAppointments_returnsAllAppointmentsForAClient() {
    Client testClient = new Client("Jessica", 1);
    testClient.save();
    Appointment testAppointment1 = new Appointment(testClient.getId(), 1, LocalDate.now().plusDays(2).toString(), "16:30:00");
    testAppointment1.save();
    Appointment testAppointment2 = new Appointment(testClient.getId(), 1, LocalDate.now().plusDays(9).toString(), "16:30:00");
    testAppointment2.save();
    Appointment testAppointment3 = new Appointment(testClient.getId(), 1, LocalDate.now().minusDays(9).toString(), "01:30:00");
    testAppointment3.save();
    assertTrue(testClient.getUpcomingAppointments().get(0).equals(testAppointment1));
    assertTrue(testClient.getUpcomingAppointments().get(1).equals(testAppointment2));
    assertFalse(testClient.getUpcomingAppointments().contains(testAppointment3));
  }

  @Test
  public void getPastAppointments_returnsAllAppointmentsForAClient() {
    Client testClient = new Client("Jessica", 1);
    testClient.save();
    Appointment testAppointment1 = new Appointment(testClient.getId(), 1, LocalDate.now().minusDays(4).toString(), "16:30:00");
    testAppointment1.save();
    Appointment testAppointment2 = new Appointment(testClient.getId(), 1, LocalDate.now().minusDays(9).toString(), "16:30:00");
    testAppointment2.save();
    Appointment testAppointment3 = new Appointment(testClient.getId(), 1, LocalDate.now().plusDays(9).toString(), "01:30:00");
    testAppointment3.save();
    assertTrue(testClient.getPastAppointments().get(0).equals(testAppointment1));
    assertTrue(testClient.getPastAppointments().get(1).equals(testAppointment2));
    assertFalse(testClient.getPastAppointments().contains(testAppointment3));
  }

  @Test
  public void getAllAppointments_returnsAllAppointmentsForAClient() {
    Client testClient = new Client("Jessica", 1);
    testClient.save();
    Appointment testAppointment1 = new Appointment(testClient.getId(), 1, LocalDate.now().minusDays(4).toString(), "16:30:00");
    testAppointment1.save();
    Appointment testAppointment2 = new Appointment(testClient.getId(), 1, LocalDate.now().minusDays(9).toString(), "16:30:00");
    testAppointment2.save();
    Appointment[] appointments = new Appointment[] {testAppointment1, testAppointment2};
    assertTrue(testClient.getAllAppointments().containsAll(Arrays.asList(appointments)));
  }

  @Test
  public void deleteClient_removesAllClientAppointmentsFromDB_true() {
    Client testClient = new Client("Jessica", 1);
    testClient.save();
    Appointment testAppointment1 = new Appointment(testClient.getId(), 1, LocalDate.now().minusDays(4).toString(), "16:30:00");
    testAppointment1.save();
    Appointment testAppointment2 = new Appointment(testClient.getId(), 1, LocalDate.now().minusDays(9).toString(), "16:30:00");
    testAppointment2.save();
    int testClientId = testClient.getId();
    int testAppointment1Id = testAppointment1.getId();
    int testAppointment2Id = testAppointment2.getId();
    testClient.deleteClient();
    assertEquals(null, Client.find(testClientId));
    assertEquals(null, Appointment.find(testAppointment1Id));
    assertEquals(null, Appointment.find(testAppointment2Id));
  }

  @Test
  public void assignStylist_reassignsAppointmentStylistIdToNewStylist_true() {
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
    Client testClient4 = new Client("Jeff", testStylist1.getId());
    testClient4.save();
    Appointment testAppointment1 = new Appointment(testClient4.getId(), testStylist1.getId(), LocalDate.now().minusDays(4).toString(), "16:30:00");
    testAppointment1.save();
    Appointment testAppointment2 = new Appointment(testClient4.getId(), testStylist1.getId(), LocalDate.now().minusDays(9).toString(), "16:30:00");
    testAppointment2.save();
    testClient4.assignStylist();
    assertEquals(Appointment.find(testAppointment1.getId()).getStylistId(), testStylist2.getId());
    assertEquals(Appointment.find(testAppointment2.getId()).getStylistId(), testStylist2.getId());
  }

  @Test
  public void searchClient_returnsAllClientsWithMatchingName_List() {
    Stylist testStylist1 = new Stylist("Becky");
    testStylist1.save();
    Client testClient1 = new Client("Jessica", testStylist1.getId());
    testClient1.save();
    Client testClient2 = new Client("Joe", testStylist1.getId());
    testClient2.save();
    Client testClient3 = new Client("Boe", testStylist1.getId());
    testClient3.save();
    Client[] clients = new Client[] {testClient2, testClient3};
    assertTrue(Client.searchClient("oe").containsAll(Arrays.asList(clients)));
    assertFalse(Client.searchClient("oe").contains(testClient1));
  }

}
