package com.mycompany.dao;

import com.mycompany.config.*;
import com.mycompany.entity.*;
import org.seasar.doma.*;

@Dao(config = AppConfig.class)
public interface OrderDao {

  @Insert
  int insert(Order order);
}
