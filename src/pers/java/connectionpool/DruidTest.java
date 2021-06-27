package pers.java.connectionpool;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ProxySelector;
import java.sql.Connection;
import java.util.Properties;

/**
 * @description:
 * @author: 吕明翰
 * @createDate: 2021-06-25 17:09
 * @version: 1.0
 */
public class DruidTest {
    @Test
    public void getConnection() throws Exception {
        Properties pros = new Properties();

        FileInputStream is = new FileInputStream(new File("src/Druid.properties"));
        pros.load(is);
        DataSource source = DruidDataSourceFactory.createDataSource(pros);
        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
