package pers.java.preparedstatementCRUD;

import org.junit.Test;
import pers.java.bean.Customer;
import pers.java.bean.Order;
import pers.java.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 实现针对不同表的通用的查询操作
 * @author: 吕明翰
 * @createDate: 2021-06-22 16:19
 * @version: 1.0
 */
public class PreparedStateQueryTest {
    @Test
    public void testGetForList(){
        String sql = "select id,name,email from customers where id < ?";
        List<Customer> list = getForList(Customer.class, sql, 13);
//        for (Customer cus : list){
//            System.out.println(cus);
//        }
        list.forEach(System.out::println);
    }

    /**
     * 返回多条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> getForList(Class<T> clazz,String sql,Object...args){
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
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while(rs.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    //获取结果集的列名
                    String columnLabel = rsmd.getColumnLabel(i+1);
                    //通过反射，给cust对象的指定columnName属性赋值为columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);

                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps,rs);

        }
        return null;
    }

    @Test
    public void testGetInstance(){
        String sql = "select id,name,email from customers where id = ?";
        Customer customer = getInstance(Customer.class, sql, 12);
        System.out.println(customer);
        String sql1 = "select order_id orderId,order_name orderName from `order` where order_id = ?";
        Order order = getInstance(Order.class, sql1, 2);
        System.out.println(order);
    }

    /**
     * 返回一条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> clazz,String sql,Object...args){
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
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    //获取结果集的列名
                    String columnLabel = rsmd.getColumnLabel(i+1);
                    //通过反射，给cust对象的指定columnName属性赋值为columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);

                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,ps,rs);

        }
        return null;
    }
}
