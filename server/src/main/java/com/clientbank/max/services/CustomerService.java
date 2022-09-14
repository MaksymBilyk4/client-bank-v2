package com.clientbank.max.services;

import com.clientbank.max.dao.CustomerDao;
import com.clientbank.max.entities.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CustomerService implements I_Service<Customer> {

    private final CustomerDao customerDao;

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getOne(Long id) {
        return customerDao.getOne(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerDao.save(customer);
    }

    @Override
    public void saveAll(List<Customer> customers) {
        customerDao.saveAll(customers);
    }

    @Override
    public void deleteAll(List<Customer> customers) {
        customerDao.deleteAll(customers);
    }

    @Override
    public boolean delete(Customer customer) {
        return customerDao.delete(customer);
    }

    @Override
    public boolean deleteById(Long id) {
        return customerDao.deleteById(id);
    }

    public Customer update (Customer customer) {
        return customerDao.update(customer);
    }

    public boolean addEmployer (Long customerId, Long employerId) {
        return customerDao.addEmployer(customerId, employerId);
    }
}
