package pers.java.connectionpool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @description:
 * @author: 吕明翰
 * @createDate: 2021-06-25 15:25
 * @version: 1.0
 */
public class C3P0Test {
    @Test
    public void testGetConnection() throws Exception {
        //获取c3p0数据库连接池
        //方式一
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.cj.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost/test" );
        cpds.setUser("root");
        cpds.setPassword("123456");
        //通过相关设置配置管理数据库连接池
        //设置初始数据库连接池中的连接数
        cpds.setInitialPoolSize(10);

        Connection conn = cpds.getConnection();
        System.out.println(conn);
        //销毁连接池，一般不销毁
        //DataSources.destroy( cpds );
    }
    //方式二，通过配置文件连接
    public void testGetConnection1() throws SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
        Connection conn = cpds.getConnection();
        System.out.println(conn);
    }

}
