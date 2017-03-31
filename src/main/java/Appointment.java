import org.sql2o.*;
import java.sql.Time;
import java.sql.Date;
import java.util.List;
import java.text.DateFormat;
import java.time.LocalDate;

public class Appointment {
  private int id;
  private int clientId;
  private int stylistId;
  private String appDate;
  private String appTime;

  public Appointment(int clientId, int stylistId, String appDate, String appTime) {
    this.clientId = clientId;
    this.stylistId = stylistId;
    this.appDate = appDate;
    this.appTime = appTime;
  }

  public int getClientId() {
    return this.clientId;
  }

  public int getStylistId() {
    return this.stylistId;
  }

  public String getDate() {
    return this.appDate;
  }

  public String getTime() {
    return this.appTime;
  }

  public int getId() {
    return this.id;
  }

  @Override
  public boolean equals(Object otherAppointment) {
    if (!(otherAppointment instanceof Appointment)) {
      return false;
    } else {
      Appointment newAppointment = (Appointment) otherAppointment;
      return this.getClientId() == newAppointment.getClientId() &&
             this.getStylistId() == newAppointment.getStylistId() &&
             this.getDate().equals(newAppointment.getDate()) &&
             this.getTime().equals(newAppointment.getTime()) &&
             this.getId() == newAppointment.getId();
    }
  }

  public static List<Appointment> all() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM appointments;";
      return con.createQuery(sql)
        .executeAndFetch(Appointment.class);
    }
  }

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO appointments (clientId, stylistId, appDate, appTime) VALUES (:clientId, :stylistId, CAST(:appDate as date), CAST(:appTime as time));";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("clientId", this.clientId)
        .addParameter("stylistId", this.stylistId)
        .addParameter("appDate", this.appDate)
        .addParameter("appTime", this.appTime)
        .executeUpdate()
        .getKey();
    }
  }

  public static Appointment find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM appointments WHERE id = :id;";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Appointment.class);
    }
  }

  public void updateAppointment(String appDate, String appTime) {
    this.appDate = appDate;
    this.appTime = appTime;
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE appointments SET (appDate, appTime) = (CAST(:appDate as date), CAST(:appTime as time)) WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("appDate", this.appDate)
        .addParameter("appTime", this.appTime)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void deleteAppointment() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM appointments WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public String getFormatDate() {
    Date date;
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT appDate FROM appointments WHERE id = :id;";
      date = con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetchFirst(Date.class);
    }
    return DateFormat.getDateInstance().format(date);
  }

  public String getFormatTime() {
    Time time;
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT appTime FROM appointments WHERE id = :id;";
      time = con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetchFirst(Time.class);
    }
    return DateFormat.getTimeInstance(DateFormat.SHORT).format(time);
  }

  public static List<Appointment> getDaysAppointment(String day) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM appointments WHERE appDate = CAST(:day as date);";
      return con.createQuery(sql)
        .addParameter("day", day)
        .executeAndFetch(Appointment.class);
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

  public String getClientName() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT name FROM clients WHERE id = :clientId;";
      return con.createQuery(sql)
        .addParameter("clientId", this.clientId)
        .executeAndFetchFirst(String.class);
    }
  }

  public static List<Appointment> upcomingAppointments() {
    String today = LocalDate.now().toString();
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM appointments WHERE appDate >= CAST(:today as date) ORDER BY appDate asc;";
      return con.createQuery(sql)
        .addParameter("today", today)
        .executeAndFetch(Appointment.class);
    }
  }

}
