package pers.java.preparedstatementCRUD;

import org.junit.Test;
import pers.java.bean.Customer;
import pers.java.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @description: 针对于customers表的查询操作
 * @author: 吕明翰
 * @createDate: 2021-06-22 9:48
 * @version: 1.0
 */
public class CustomerForQuery {
    @Test
    public void testQueryForCustomer(){
        String sql = "select * from customers where id = ?";
        Customer customer = queryForCustomers(sql, 3);
        System.out.println(customer);
        sql = "select * from customers where name = ?";
        Customer customer1 = queryForCustomers(sql, "周杰伦");
        System.out.println(customer1);
    }
    /**
     * 针对于customers表的通用的查询操作
     */
    public Customer queryForCustomers(String sql,Object...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过元数据获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()){
                Customer cust = new Customer();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    //获取结果集的列名
                    String columnLabel = rsmd.getColumnLabel(i+1);
                    //通过反射，给cust对象的指定columnName属性赋值为columnValue
                    Field field = Customer.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(cust,columnValue);

                }
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps,rs);

        }
        return null;
    }
    public void testQuery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,1);
            res = ps.executeQuery();
            if(res.next()){//判断结果集的下一条是否有数据，如果有数据，指针下移并返回true
                int id = res.getInt(1);
                String name = res.getString(2);
                String email = res.getString(3);
                Date birth = res.getDate(4);
                Customer customer = new Customer(id,name,email,birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps,res);
        }

    }
}
