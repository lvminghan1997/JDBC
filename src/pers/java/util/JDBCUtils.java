package pers.java.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 操作数据库的工具类
 */
public class JDBCUtils {
    /**
     * 获取数据库连接
     * @return
     * @throws Exception
     */

    public static Connection getConnection() throws Exception {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties pro = new Properties();
        pro.load(is);
        String url = pro.getProperty("url");
        String user = pro.getProperty("user");
        String password = pro.getProperty("password");
        String driverClass = pro.getProperty("driverClass");
        Class.forName(driverClass);
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    /**
     * c3p0连接池获取连接
     */
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
    public static Connection getConnection1() throws SQLException {

        Connection conn = cpds.getConnection();
        return conn;
    }

    /**
     * //DBCP连接池获取连接
     * @throws Exception
     */
    private static DataSource source;
    static{
        try {
            Properties pros = new Properties();
            //方式一
            //InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            //方式二
            FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
            pros.load(is);
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection testGetConnection2() throws Exception {

        Connection conn = source.getConnection();
        return conn;
    }

    /**
     * 通过Druid数据库连接池获取连接
     * @throws Exception
     */
    private static DataSource source1;
    static {
        try {
            Properties pros = new Properties();

            FileInputStream is = new FileInputStream(new File("src/Druid.properties"));
            pros.load(is);
            source1 = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection3() throws Exception {

        Connection conn = source1.getConnection();
        return conn;
    }
    /**
     * 关闭资源和连接的操作
     * @param conn
     * @param ps
     */
    public static void CloseResource(Connection conn, Statement ps){
        try {
            if(ps != null){
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn != null){
            conn.close();
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void CloseResource(Connection conn, Statement ps, ResultSet res){
        try {
            if(ps != null){
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn != null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(res != null){
                res.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
