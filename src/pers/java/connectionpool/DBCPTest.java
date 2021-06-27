package pers.java.connectionpool;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @description:
 * @author: 吕明翰
 * @createDate: 2021-06-25 16:38
 * @version: 1.0
 */
public class DBCPTest {
    /**
     * 测试DBCP的数据库连接池技术
     */
    //方式一 不推荐
    @Test
    public void testGetConnection() throws SQLException {
        BasicDataSource source = new BasicDataSource();
        //设置基本信息
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost/test");
        source.setUsername("root");
        source.setPassword("123456");
        //还可以设置数据库连接池管理的相关属性
        source.setInitialSize(10);
        source.setMaxActive(10);
        //...

        Connection conn = source.getConnection();
        System.out.println(conn);
    }

    //方式二 通过配置文件连接
    @Test
    public void testGetConnection1() throws Exception {
        Properties pros = new Properties();
        //方式一
        //InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        //方式二
        FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
        pros.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(pros);
        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
