package org.mvrck.training.route;

import akka.http.javadsl.server.*;
import org.mvrck.training.dao.*;
import org.seasar.doma.jdbc.tx.*;

public class AllRoute extends AllDirectives {
  private OrderRoute orderRoute;

  public AllRoute(
    TransactionManager transactionManager,
    OrderDao orderDao,
    TicketStockDao ticketStockDao
  ) {
    this.orderRoute = new OrderRoute(transactionManager, orderDao, ticketStockDao);
  }

  public Route route() {
    return concat(
      orderRoute.route()
    );
  }
}
