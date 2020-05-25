package com.project.datasource;
import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
public class DataSource {
private static HikariConfig config = new HikariConfig();
private static HikariDataSource ds;

static {
config.setJdbcUrl("jdbc:hsqldb:file:db/projekty"); //Zwróæ uwagê, ¿e teraz œcie¿ka
config.setUsername("admin"); //wzglêdna jest definiowana od
config.setPassword("admin"); //g³ównego katalogu projektu!
config.setMaximumPoolSize(1);//Wystarczy tylko jedno buforowane po³¹czenie
//config.addDataSourceProperty("cachePrepStmts", "true");
//config.addDataSourceProperty("prepStmtCacheSize", "250");
//config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
ds = new HikariDataSource(config);
}

private DataSource() {}
/** Domyœlna metoda tworz¹ca/pobieraj¹ca po³¹czenia
* @return
*/
 public static Connection getConnection() throws SQLException {
return ds.getConnection();
}
 
}