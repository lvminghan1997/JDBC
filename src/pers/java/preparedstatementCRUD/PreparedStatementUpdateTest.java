package pers.java.preparedstatementCRUD;

import com.sun.javaws.Main;
import org.junit.Test;
import pers.java.util.JDBCUtils;

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

    //修改customers表中的记录
    @Test
    public void testUpdate(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,"莫扎特");
            ps.setObject(2,18);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps);
        }

    }
    @Test//通用的增删改操作
    public void testCommonUpdate() throws Exception {
//        String sql = "delete from customers where id = ?";
//
//        update(sql,3);

        String sql = "update `order` set order_name = ? where order_id = ?";
        update(sql,"DD","2");
    }
    public void update(String sql ,Object...arg) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < arg.length; i++) {
                ps.setObject(i+1,arg[i]);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps);
        }

    }

}
