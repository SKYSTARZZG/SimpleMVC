package org.smart4j.chapter4.service;

import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.annotation.Transaction;
import org.smart4j.framework.bean.Customer;
import org.smart4j.framework.helper.DatabaseHelper;

import java.util.List;
import java.util.Map;

/**
 * 凡是对数据库有变更的操作都建议加上@Transaction注解，可以保证一旦某个方法中一个更新操作失败了，整个方法都可以回滚
 * @Author Administrator
 * @Date 2018-05-06
 * @since 1.0.0
 */
@Service
public class CustomerService {

    /**获取客户列表*/
    public List<Customer> getCustomerList(){
        String sql= "select * from customer";
        return DatabaseHelper.queryEntityList(Customer.class,sql);
    }

    /**获取客户*/
    public Customer getCustomer(Long id){
        String sql="select * from customer where id=?";
        return DatabaseHelper.getEntity(Customer.class,sql,id);
    }

    /**创建客户*/
    @Transaction
    public boolean createCustomer(Map<String,Object> fieldMap){
        return DatabaseHelper.insertEntity(Customer.class,fieldMap);
    }

    /**更新客户*/
    @Transaction
    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        return DatabaseHelper.updateEntity(Customer.class,id,fieldMap);
    }

    /**删除客户*/
    @Transaction
    public boolean deleteCustomer(long id){
        return DatabaseHelper.deleteEntity(Customer.class,id);
    }
}
