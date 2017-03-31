import org.sql2o.*;
import java.sql.Time;
import java.sql.Date;
import java.util.List;

public class Appointment {
  private int id;
  private int clientId;
  private int stylistId;
  private Date appDate;
  private Time appTime;

  public Appointment(int clientId, int stylistId, String appDate, String appTime) {
    this.clientId = clientId;
    this.stylistId = stylistId;
    this.appDate = Date.valueOf(appDate);
    this.appTime = Time.valueOf(appTime);
  }

  public int getClientId() {
    return this.clientId;
  }

  public int getStylistId() {
    return this.stylistId;
  }

  public Date getDate() {
    return this.appDate;
  }

  public Time getTime() {
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
      String sql = "INSERT INTO appointments (clientId, stylistId, appDate, appTime) VALUES (:clientId, :stylistId, :appDate, :appTime);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("clientId", this.clientId)
        .addParameter("stylistId", this.stylistId)
        .addParameter("appDate", this.appDate)
        .addParameter("appTime", this.appTime)
        .executeUpdate()
        .getKey();
    }
  }

}
