package pers.java.dao;

import org.junit.jupiter.api.Test;
import pers.java.bean.Customer;
import pers.java.util.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLOutput;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @description:
 * @author: 吕明翰
 * @createDate: 2021-06-25 11:43
 * @version: 1.0
 */
class CustomerDAOImplTest {
    private  CustomerDAOImpl dao = new CustomerDAOImpl();
    @Test
    void insert(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer customer = new Customer(1, "小萌", "11@qq.com", new Date(769687686L));
            dao.insert(conn,customer);
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,null);
        }

    }

    @Test
    void deleteById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            dao.deleteById(conn,13);
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,null);
        }

    }

    @Test
    void updateById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer customer = new Customer(18, "贝多芬", "22@qq.com", new Date(989437438L));
            dao.updateById(conn,customer);
            System.out.println("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,null);
        }
    }

    @Test
    void getCustomerById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer customer = dao.getCustomerById(conn, 19);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,null);
        }
    }

    @Test
    void getAll() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            List<Customer> all = dao.getAll(conn);
            all.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,null);
        }
    }

    @Test
    void getCount() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Long count = dao.getCount(conn);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,null);
        }
    }

    @Test
    void getMaxBirth() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Date maxBirth = dao.getMaxBirth(conn);
            System.out.println(maxBirth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.CloseResource(conn,null);
        }
    }
}