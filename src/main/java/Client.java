import org.sql2o.*;
import java.util.List;

public class Client {
  private String name;
  private int id;
  private int stylistId;

  public Client(String name, int stylistId) {
    this.name = name;
    this.stylistId = stylistId;
  }

  public String getName() {
    return this.name;
  }

  public int getStylistId() {
    return this.stylistId;
  }

  public int getId() {
    return this.id;
  }

  @Override
  public boolean equals(Object otherClient) {
    if (!(otherClient instanceof Client)) {
      return false;
    } else {
      Client newClient = (Client) otherClient;
      return this.getName().equals(newClient.getName()) &&
             this.getStylistId() == newClient.getStylistId() &&
             this.getId() == newClient.getId();
    }
  }

}
