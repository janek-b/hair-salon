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

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO clients (name, stylistId) VALUES (:name, :stylistId);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("stylistId", this.stylistId)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Client> all() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM clients;";
      return con.createQuery(sql)
        .executeAndFetch(Client.class);
    }
  }

}
