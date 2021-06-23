import org.junit.Test;

import java.sql.*;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class PreparedStatementUpdateTest {
    //向customers中添加数据
    @Test
    public void testConnection4() throws Exception {
        InputStream is = PreparedStatementUpdateTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pro = new Properties();
        pro.load(is);
        String url = pro.getProperty("url");
        String user = pro.getProperty("user");
        String password = pro.getProperty("password");
        String driverClass = pro.getProperty("driverClass");
        Class.forName(driverClass);
        Connection conn = DriverManager.getConnection(url, user, password);
//        System.out.println(conn);
        //预编译sql语句,返回PreparedStatement实例
        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        //填充占位符
        ps.setString(1,"哪吒");
        ps.setString(2,"nezha@qq.com");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("1997-02-02");
        ps.setDate(3,new java.sql.Date(date.getTime()));
        //执行操作
        ps.execute();
        //关闭资源
        ps.close();
        conn.close();

    }

}
