package pers.java.connect;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {

    @Test
    public void testConnection1() throws SQLException {
        Driver driver = new com.mysql.jdbc.Driver();

        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123456");
        Connection conn = driver.connect(url,info);
        System.out.println(conn);

    }

    @Test
    public void testConnection2() throws Exception {
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123456");
        Connection conn = driver.connect(url,info);
        System.out.println(conn);

    }

    @Test
    public void testConnection3() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");

        //   Driver driver = (Driver) clazz.newInstance();
        //   DriverManager.deregisterDriver(driver);
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123456";
        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println(conn);


    }

    @Test
    public void testConnection4() throws Exception {
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        System.out.println(is);
        Properties pro = new Properties();
        pro.load(is);
        String url = pro.getProperty("url");
        String user = pro.getProperty("user");
        String password = pro.getProperty("password");
        String driverClass= pro.getProperty("driverClass");
        Class.forName(driverClass);
        Connection conn =  DriverManager.getConnection(url,user,password);
        System.out.println(conn);

    }
}
