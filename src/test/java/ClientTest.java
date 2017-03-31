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

}
