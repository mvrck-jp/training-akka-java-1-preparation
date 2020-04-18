package org.mvrck.training.dao;

import org.mvrck.training.config.*;
import org.mvrck.training.entity.*;
import org.seasar.doma.*;

@Dao(config = AppConfig.class)
public interface TicketStockDao {

  @Select
  TicketStock selectById(int id);

  @Update
  int update(TicketStock ticket);
}
