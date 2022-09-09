package com.clientbank.max.services;

import com.clientbank.max.dao.EmployerDao;
import com.clientbank.max.entities.Employer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployerService implements I_Service<Employer>{

    private final EmployerDao employerDao;

    @Override
    @Transactional(readOnly = true)
    public List<Employer> findAll() {
        return employerDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employer getOne(Long id) {
        return employerDao.getOne(id);
    }

    @Override
    public Employer save(Employer employer) {
        return employerDao.save(employer);
    }

    @Override
    public void saveAll(List<Employer> employers) {
        employerDao.saveAll(employers);
    }

    @Override
    public void deleteAll(List<Employer> employers) {
        employerDao.deleteAll(employers);
    }

    @Override
    public boolean delete(Employer employer) {
        return employerDao.delete(employer);
    }

    @Override
    public boolean deleteById(Long id) {
        return employerDao.deleteById(id);
    }
}
