package pers.java.blob;

import org.junit.Test;
import pers.java.bean.Customer;
import pers.java.util.JDBCUtils;

import java.io.*;
import java.sql.*;

/**
 * @description:
 * @author: 吕明翰
 * @createDate: 2021-06-23 10:47
 * @version: 1.0
 */
public class BlobTest {
    @Test
    public void testInsert() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1,"小微");
        ps.setObject(2,"xiaowei@qq.com");
        ps.setObject(3,"1992-09-08");
        FileInputStream is = new FileInputStream(new File("1.jpg"));
        ps.setBlob(4,is);
        ps.execute();
        JDBCUtils.CloseResource(conn,ps);
    }

    @Test
    public void testQuery(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,20);
            res = ps.executeQuery();
            is = null;
            fos = null;
            if (res.next()){
    //            int id = res.getInt(1);
    //            String name = res.getString(2);
    //            String email = res.getString(3);
    //            Date birth = res.getDate(4);
                int id = res.getInt("id");
                String name = res.getString("name");
                String email = res.getString("email");
                Date birth = res.getDate("birth");

                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

                Blob photo = res.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream(new File("小微.jpg"));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1){
                    fos.write(buffer,0,len);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null)
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.CloseResource(conn,ps,res);
        }

    }
}
