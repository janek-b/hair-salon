import org.sql2o.*;
import java.sql.Time;
import java.sql.Date;

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

}
