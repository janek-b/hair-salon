import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.time.LocalDate;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("stylists", Stylist.all());
      model.put("appointmentsToday", Appointment.getDaysAppointment(LocalDate.now().toString()));
      model.put("appointmentsTomorrow", Appointment.getDaysAppointment(LocalDate.now().plusDays(1).toString()));
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/admin", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("stylists", Stylist.all());
      model.put("autoStylist", Stylist.getLowestClientCount());
      model.put("template", "templates/admin.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String stylistName = request.queryParams("stylistName");
      Stylist newStylist = new Stylist(stylistName);
      newStylist.save();
      response.redirect("/admin");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/clients/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String clientName = request.queryParams("clientName");
      int clientStylist = Integer.parseInt(request.queryParams("clientStylist"));
      Client newClient = new Client(clientName, clientStylist);
      newClient.save();
      response.redirect("/admin");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/clients", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("clients", Client.all());
      model.put("template", "templates/clients.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("stylists", Stylist.all());
      model.put("template", "templates/stylists.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      model.put("stylist", stylist);
      model.put("template", "templates/stylist.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String stylistName = request.queryParams("stylistName");
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      stylist.updateStylist(stylistName);
      response.redirect(request.headers("Referer"));
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Stylist stylist = Stylist.find(Integer.parseInt(request.params(":id")));
      stylist.deleteStylist();
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists/:id/clients/:clientId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Client client = Client.find(Integer.parseInt(request.params(":clientId")));
      String[] timeslots = {"10:00:00", "10:30:00", "11:00:00", "11:30:00", "12:00:00", "12:30:00", "01:00:00", "01:30:00", "02:00:00", "02:30:00", "03:00:00", "03:30:00", "04:00:00", "04:30:00", "05:00:00", "05:30:00", "06:00:00", "06:30:00", "07:00:00", "07:30:00"};
      model.put("timeslots", timeslots);
      model.put("date", request.session().attribute("date"));
      model.put("clientStylist", Stylist.find(client.getStylistId()));
      model.put("stylists", Stylist.all());
      model.put("client", client);
      model.put("template", "templates/client.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id/clients/:clientId/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String clientName = request.queryParams("clientName");
      int clientStylist = Integer.parseInt(request.queryParams("clientStylist"));
      Client client = Client.find(Integer.parseInt(request.params(":clientId")));
      client.updateClient(clientName, clientStylist);
      response.redirect(request.headers("Referer"));
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id/clients/:clientId/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Client client = Client.find(Integer.parseInt(request.params(":clientId")));
      client.deleteClient();
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id/clients/:clientId/appointments/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Client client = Client.find(Integer.parseInt(request.params(":clientId")));
      String appDate = request.queryParams("appDate");
      String appTime = request.queryParams("appTime");
      Appointment newAppointment = new Appointment(client.getId(), client.getStylistId(), appDate, appTime);
      newAppointment.save();
      String url = String.format("/stylists/%d/clients/%d", client.getStylistId(), client.getId());
      request.session().removeAttribute("date");
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stylists/:id/clients/:clientId/appointments/:appointmentId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("appointment", Appointment.find(Integer.parseInt(request.params(":appointmentId"))));
      model.put("template", "templates/appointment.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id/clients/:clientId/appointments/:appointmentId/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Appointment appointment = Appointment.find(Integer.parseInt(request.params(":appointmentId")));
      String appDate = request.queryParams("appDate");
      String appTime = request.queryParams("appTime");
      appointment.updateAppointment(appDate, appTime);
      String url = String.format("/stylists/%d/clients/%d/appointments/%d", appointment.getStylistId(), appointment.getClientId(), appointment.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stylists/:id/clients/:clientId/appointments/:appointmentId/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Appointment appointment = Appointment.find(Integer.parseInt(request.params(":appointmentId")));
      String url = String.format("/stylists/%d/clients/%d", appointment.getStylistId(), appointment.getClientId());
      appointment.deleteAppointment();
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/appointments", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("appointments", Appointment.upcomingAppointments());
      model.put("template", "templates/appointments.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/setdate/:date", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      request.session().attribute("date", request.params(":date"));
      response.redirect(request.headers("Referer"));
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
