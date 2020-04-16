package com.mycompany.config;

import io.github.cdimascio.dotenv.*;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;
import org.seasar.doma.jdbc.dialect.*;
import org.seasar.doma.jdbc.tx.*;

import javax.sql.*;

@SingletonConfig
public class AppConfig implements Config {
  private static final AppConfig CONFIG = new AppConfig();

  private final Dialect dialect;

  private final LocalTransactionDataSource dataSource;

  private final TransactionManager transactionManager;

  private AppConfig() {
    String url, user, password;

    try {
      var dotenv = Dotenv.load();
      url = dotenv.get("DATABASE.URL");
      user = dotenv.get("DATABASE.USER");
      password = dotenv.get("DATABASE.PASSWORD");
      System.out.println("AppConfig: Tried to load JDBC properties from .env file. (with environment variables as fallback)");
    } catch (Exception e) {
      url = System.getProperty("DATABASE.URL");
      user = System.getProperty("DATABASE.USER");
      password = System.getProperty("DATABASE.PASSWORD");
      System.out.println("AppConfig: Tried to load JDBC properties from environment variables.");
    }

    if (url == null || user == null || password == null)
      throw new RuntimeException("Missing environment variables: one or more of 'DATABASE.URL', 'DATABASE.USER', 'DATABASE.PASSWORD' is not found.");
    else {
      dialect = new MysqlDialect();
      dataSource = new LocalTransactionDataSource(url, user, password);
      transactionManager = new LocalTransactionManager(dataSource.getLocalTransaction(getJdbcLogger()));
    }
  }

  @Override
  public Dialect getDialect() {
    return dialect;
  }

  @Override
  public DataSource getDataSource() {
    return dataSource;
  }

  @Override
  public TransactionManager getTransactionManager() {
    return transactionManager;
  }

  public static AppConfig singleton() {
    return CONFIG;
  }
}
