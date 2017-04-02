import org.sql2o.*;
import java.util.List;
import java.time.LocalDate;

public class Stylist {
  private String name;
  private int id;

  public Stylist(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public int getId() {
    return this.id;
  }

  @Override
  public boolean equals(Object otherStylist) {
    if (!(otherStylist instanceof Stylist)) {
      return false;
    } else {
      Stylist newStylist = (Stylist) otherStylist;
      return this.getName().equals(newStylist.getName()) &&
             this.getId() == newStylist.getId();
    }
  }

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stylists (name) VALUES (:name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Stylist> all() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stylists;";
      return con.createQuery(sql)
        .executeAndFetch(Stylist.class);
    }
  }

  public static Stylist find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stylists WHERE id = :id;";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Stylist.class);
    }
  }

  public List<Client> getClients() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM clients WHERE stylistId = :id;";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Client.class);
    }
  }

  public void updateStylist(String name) {
    this.name = name;
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE stylists SET name = :name WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("name", this.name)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public static Stylist getLowestClientCount() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stylists AS a ORDER BY (SELECT COUNT(name) FROM clients WHERE stylistId = a.id) asc;";
      return con.createQuery(sql)
        .executeAndFetchFirst(Stylist.class);
    }
  }

  public void deleteStylist() {
    List<Client> clients = this.getClients();
    try (Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM stylists WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
    for (Client client: clients) {
      client.assignStylist();
    }
  }

  public List<Appointment> getUpcomingAppointments() {
    String today = LocalDate.now().toString();
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM appointments WHERE stylistId = :id AND appDate >= CAST(:today as date) ORDER BY appDate asc;";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .addParameter("today", today)
        .executeAndFetch(Appointment.class);
    }
  }

  public List<Appointment> getPastAppointments() {
    String today = LocalDate.now().toString();
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM appointments WHERE stylistId = :id AND appDate <= CAST(:today as date) ORDER BY appDate desc;";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .addParameter("today", today)
        .executeAndFetch(Appointment.class);
    }
  }

  public List<Appointment> getAllAppointments() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM appointments WHERE stylistId = :id;";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Appointment.class);
    }
  }

  public boolean timeslotAvailable(String date, String time) {
    Appointment app;
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM appointments WHERE stylistId = :id AND appDate = CAST(:date as date) AND appTime = CAST(:time as time);";
      app = con.createQuery(sql)
        .addParameter("id", this.id)
        .addParameter("date", date)
        .addParameter("time", time)
        .executeAndFetchFirst(Appointment.class);
    }
    if (app instanceof Appointment) {
      return false;
    } else {
      return true;
    }
  }

  public static List<Stylist> searchStylist(String input) {
    String newInput = "%" + input + "%";
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stylists WHERE lower(name) LIKE lower(:input) ORDER BY name asc;";
      return con.createQuery(sql)
        .addParameter("input", newInput)
        .executeAndFetch(Stylist.class);
    }
  }

}
