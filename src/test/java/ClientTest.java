import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

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
    Client testClient = new Client("Jessica", 1);
    assertEquals(1, testClient.getStylistId());
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

}
