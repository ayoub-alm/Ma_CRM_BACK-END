package com.sales_scout.service.data;


import com.sales_scout.entity.data.CompanySize;
import com.sales_scout.repository.data.CompanySizeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanySizeService {
    private final CompanySizeRepository companySizeRepository;
    public CompanySizeService(CompanySizeRepository companySizeRepository){
        this.companySizeRepository = companySizeRepository;
    }

    /**
     * This function allows to get all companies size's
     * @return {List<CompanySize>} the list of companies
     */
    public List<CompanySize> getAllCompanySize(){
        return companySizeRepository.findAll();
    }

    /**
     * This function allows to get company size's by ID
     * @param {id} the company size id
     * @return {CompanySize} the selected company size
     */
    public Optional<CompanySize> getCompanySizeById(Long id){
        return companySizeRepository.findById(id);
    }

    /**
     * This function allows to create new Company size
     * @param {companySize} company size to create
     * @return {companySize} created company
     */
    public CompanySize addCompanySize(CompanySize companySize){
        return companySizeRepository.save(companySize);
    }

    /**
     * this function allows to update company size
     * @param {companySize} company size to update
     * @return {CompanySize} updated company size
     */
    public CompanySize updateCompanySize(CompanySize companySize){
       return this.companySizeRepository.save(companySize);
    }

    /**
     * This function allows to delete company size by ID
     * @param {id} the id of company size to delete
     */
    public void deleteCompanySize(Long id){
        companySizeRepository.deleteById(id);
    }
}
