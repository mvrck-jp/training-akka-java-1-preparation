package org.mvrck.training.app;

import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.stream.*;
import org.mvrck.training.config.*;
import org.mvrck.training.dao.*;
import org.mvrck.training.route.*;

public class Main {

  public static void main(String[] args) throws Exception {
    // boot up server using the route as defined below
    var system = ActorSystem.create("routes");

    var config = AppConfig.singleton();
    var orderDao = new OrderDaoImpl();
    var ticketStockDao = new TicketStockDaoImpl();

    var http = Http.get(system);
    var materializer = Materializer.createMaterializer(system);

    //In order to access all directives we need an instance where the routes are define.
    var tm = config.getTransactionManager();
    var allRoute = new AllRoute(tm, orderDao, ticketStockDao);

    var routeFlow = allRoute.route().flow(system, materializer);
    var binding = http.bindAndHandle(
      routeFlow,
      ConnectHttp.toHost("localhost", 8080),
      materializer);

    System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
    System.in.read(); // let it run until user presses return

    binding
      .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
      .thenAccept(unbound -> system.terminate()); // and shutdown when done
  }
}