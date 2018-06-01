package com.luxinx.test;


import com.luxinx.helper.DataBaseHelper;
import com.luxinx.model.Customer;

import java.util.List;
import java.util.Map;

public class CustomerService {

    public List<Customer> getCustomerList(String keyword){
        String sql ="select * from customer";
        try {
            return DataBaseHelper.queryEntityList(Customer.class,sql);
        } finally {
            DataBaseHelper.closeConnection();
        }
    }

    public Customer getCustomer(long id){
        String sql = "SELECT * from customer where id=?";
        return DataBaseHelper.queryEntity(Customer.class,sql,id);
    }

    public boolean createCustomer(Map<String, Object> fieldMap){
        return DataBaseHelper.insertEntity(Customer.class,fieldMap);
    }

    public boolean updateCustomer(long id , Map<String, Object> fieldMap){
        return DataBaseHelper.updateEntity(Customer.class,id,fieldMap);
    }
    public boolean deleteCustomer(long id){
        return DataBaseHelper.deleteEntity(Customer.class,id);
    }
}
