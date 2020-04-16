package com.mycompany.route;

import akka.http.javadsl.marshallers.jackson.*;
import akka.http.javadsl.server.*;
import com.mycompany.dao.*;
import com.mycompany.dto.*;
import com.mycompany.entity.*;
import org.seasar.doma.jdbc.tx.*;

public class OrderRoute extends AllDirectives {
  private TransactionManager transactionManager;
  private OrderDao orderDao;
  private TicketStockDao ticketStockDao;

  public OrderRoute(
    TransactionManager transactionManager,
    OrderDao orderDao,
    TicketStockDao ticketStockDao
  ) {
    this.transactionManager = transactionManager;
    this.orderDao = orderDao;
    this.ticketStockDao = ticketStockDao;
  }

  private Order convert(OrderPutRequest req) {
    var order = new Order();
    order.setTicketId(req.getTicketId());
    order.setUserId(req.getUserId());
    order.setQuantity(1);
    return order;
  }

  private void insert(Order order) {
    transactionManager.required(() -> {
      orderDao.insert(order);
      var ticket = ticketStockDao.selectById(order.getTicketId());
      var quantity = ticket.getQuantity();
      ticket.setQuantity(quantity - 1);
      ticketStockDao.update(ticket);
    });
  }

  public Route route () {
    return pathPrefix("orders", () ->
      pathEndOrSingleSlash(() ->
        post(() ->
          entity(Jackson.unmarshaller(OrderPutRequest.class), req -> {
            insert(convert(req));
            return complete( "Put request received: ticket_id = " + req.getTicketId() + ", user_id = " + req.getUserId() );
          })
        )
      )
    );
  }
}
