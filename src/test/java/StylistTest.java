import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

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

}
