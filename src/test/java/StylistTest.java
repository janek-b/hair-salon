import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

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

}
