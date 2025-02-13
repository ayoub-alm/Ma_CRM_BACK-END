package com.sales_scout.service;

import com.sales_scout.dto.request.RightsRequestDto;
import com.sales_scout.dto.response.RightsResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.Right;
import com.sales_scout.exception.DataAlreadyExistsException;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.exception.DuplicateKeyValueException;
import com.sales_scout.mapper.RightsMapper;
import com.sales_scout.repository.CompanyRepository;
import com.sales_scout.repository.RightRepository;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RightService {

    private final CompanyRepository companyRepository;
    private final RightRepository rightRepository;

    public RightService(CompanyRepository companyRepository, RightRepository rightRepository) {
        this.companyRepository = companyRepository;
        this.rightRepository = rightRepository;
    }

    public List<RightsResponseDto> getAllRight() throws DataNotFoundException {
        List<Right> right = rightRepository.findAllByDeletedAtIsNull();
        if(!right.isEmpty() && right != null){
            return right.stream().map(RightsMapper::fromEntity).collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data of Right Not Found : List Of Rights Is Null" , 565L);
        }
    }

    
    public RightsResponseDto getRightById(Long id) throws DataNotFoundException {
        Right right = rightRepository.findByIdAndDeletedAtIsNull(id);
        if (right != null){
            return RightsMapper.fromEntity(right);
        }else {
            throw new DataNotFoundException("Data Of Right Not Found : Right With Id "+ id +" Not Found",199L);
        }
    }

    
    public RightsResponseDto createRight(RightsRequestDto rightsRequestDto) throws DataAlreadyExistsException {
        Right right = rightRepository.findByNameAndDeletedAtIsNull(rightsRequestDto.getName());
        if (right == null){
            Right convertright = RightsMapper.fromDto(rightsRequestDto);
            Right rightSave = rightRepository.save(convertright);
            return RightsMapper.fromEntity(rightSave);
        }else {
            throw new DataAlreadyExistsException("Right with Name "+rightsRequestDto.getName() + " Already Exists",888L);
        }
    }

    
    public RightsResponseDto updateRight(Long id, RightsRequestDto rightsRequestDto) throws DataNotFoundException{
        try{
            Right right = rightRepository.findByIdAndDeletedAtIsNull(id);
            if (right != null) {
                right.setName(rightsRequestDto.getName());
                right.setDescription(rightsRequestDto.getDescription());
                Optional<Company> company = companyRepository.findByDeletedAtIsNullAndId(rightsRequestDto.getCompanyId());
                if (company.isPresent()){
                    right.setCompany(company.get());
                }
                Right rightSave = rightRepository.save(right);
                return RightsMapper.fromEntity(rightSave);
            } else {
                throw new DataNotFoundException("Data Of Right Not Found : Right With Id : " + id + " Not Found", 777L);
            }
        }catch (DataNotFoundException e){
            throw new DataNotFoundException("Data Of Right Not Found : Right With Id : " + id + " Not Found", 777L);
        }
    }

    
    public Boolean deleteRight(Long id) throws DataNotFoundException {
        Right right =rightRepository.findByIdAndDeletedAtIsNull(id);
        if (right != null ){
            rightRepository.deleteById(id);
            return true;
        }else {
            throw new DataNotFoundException("Right With Id : "+ id + " Not Found",444L);
        }
    }
}
