import org.sql2o.*;
import java.sql.Time;
import java.sql.Date;
import java.util.List;

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

}
