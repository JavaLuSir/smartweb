package com.luxinx.test;


import com.luxinx.helper.DataBaseHelper;
import com.luxinx.model.Customer;

import java.sql.Connection;
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
        return null;
    }

    public boolean createCustomer(Map<String, Object> fieldMap){
        return false;
    }

    public boolean updateCustomer(long id , Map<String, Object> fieldMap){
        return false;
    }
    public boolean deleteCustomer(long id){
        return false;
    }
}
