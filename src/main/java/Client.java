import org.sql2o.*;
import java.util.List;
import java.time.LocalDate;

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

  public static Client find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM clients WHERE id = :id;";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Client.class);
    }
  }

  public void updateClient(String name, int stylistId) {
    this.name = name;
    this.stylistId = stylistId;
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE clients SET (name, stylistId) = (:name, :stylistId) WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("name", this.name)
        .addParameter("stylistId", this.stylistId)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void deleteClient() {
    List<Appointment> appointments = this.getAllAppointments();
    for (Appointment appointment: appointments) {
      appointment.deleteAppointment();
    }
    try (Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM clients WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void assignStylist() {
    int oldStylistId = this.stylistId;
    this.stylistId = Stylist.getLowestClientCount().getId();
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE clients SET stylistId = :stylistId WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("stylistId", this.stylistId)
        .addParameter("id", this.id)
        .executeUpdate();
    }
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE appointments SET stylistId = :stylistId WHERE clientId = :id AND stylistId = :oldStylistId;";
      con.createQuery(sql)
        .addParameter("stylistId", this.stylistId)
        .addParameter("id", this.id)
        .addParameter("oldStylistId", oldStylistId)
        .executeUpdate();
    }
  }

  public String getStylistName() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT name FROM stylists WHERE id = :stylistId;";
      return con.createQuery(sql)
        .addParameter("stylistId", this.stylistId)
        .executeAndFetchFirst(String.class);
    }
  }

  public List<Appointment> getUpcomingAppointments() {
    String today = LocalDate.now().toString();
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM appointments WHERE clientId = :id AND appDate >= CAST(:today as date) ORDER BY appDate asc;";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .addParameter("today", today)
        .executeAndFetch(Appointment.class);
    }
  }

  public List<Appointment> getPastAppointments() {
    String today = LocalDate.now().toString();
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM appointments WHERE clientId = :id AND appDate <= CAST(:today as date) ORDER BY appDate desc;";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .addParameter("today", today)
        .executeAndFetch(Appointment.class);
    }
  }

  public List<Appointment> getAllAppointments() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM appointments WHERE clientId = :id;";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Appointment.class);
    }
  }

  public static List<Client> searchClient(String input) {
    String newInput = "%" + input + "%";
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM clients WHERE lower(name) LIKE lower(:input) ORDER BY name asc;";
      return con.createQuery(sql)
        .addParameter("input", newInput)
        .executeAndFetch(Client.class);
    }
  }

}
