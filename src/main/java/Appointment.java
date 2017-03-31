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

}
