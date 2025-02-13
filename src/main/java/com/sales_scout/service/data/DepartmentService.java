package com.sales_scout.service.data;


import com.sales_scout.dto.request.DepartmentRequestDto;
import com.sales_scout.dto.response.DepartmentResponseDto;
import com.sales_scout.entity.data.Department;
import com.sales_scout.exception.DataAlreadyExistsException;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.mapper.DepartmentMapper;
import com.sales_scout.repository.data.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * get All Department
     * @return {List<DepartmentResponseDto>}
     * @throws {DataNotFoundException}
     */
    public List<DepartmentResponseDto> findAll() throws DataNotFoundException {
        List<Department> departments = departmentRepository.findAllByDeletedAtIsNull();
        if (!departments.isEmpty() && departments != null){
            return departments.stream().map(DepartmentMapper::fromEntity).collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Data of Department Not Found : List Of Department Is Null",154L);
        }
    }

    /**
     * get Department With id
     * @param {id}
     * @return {DepartmentResponseDto}
     * @throws {DataNotFoundException}
     */
    public DepartmentResponseDto findById(Long id) throws DataNotFoundException {
        Optional<Department> department = departmentRepository.findByIdAndDeletedAtIsNull(id);
        if (!department.isEmpty() && department != null){
         return DepartmentMapper.fromEntity(department.get());
        }else {
            throw new DataNotFoundException("Department With Id : "+ id +" Not Found",999L);
        }
    }

    /**
     * create Department
      * @param {departmentRequestDto}
     * @return {DepartmentResponseDto}
     * @throws {DataAlreadyExistsException}
     */
    public DepartmentResponseDto save(DepartmentRequestDto departmentRequestDto) throws DataAlreadyExistsException {
        Optional<Department> department = departmentRepository.findByNameAndDeletedAtIsNull(departmentRequestDto.getName());
        if(department.isEmpty()){
            Department departmentDtoToEntity = DepartmentMapper.fromDto(departmentRequestDto);
            Department departmentSave = departmentRepository.save(departmentDtoToEntity);
            return DepartmentMapper.fromEntity(departmentSave);
        }else {
            throw new DataAlreadyExistsException("Department With Name : "+departmentRequestDto.getName()+" Already Exists",888L);
        }
    }

    /**
     * update Department With id
     * @param {id}
     * @param {departmentRequestDto}
     * @return {DepartmentResponseDto}
     * @throws {DataNotFoundException}
     * @throws {DataAlreadyExistsException}
     */
    public DepartmentResponseDto updateDepartment(Long id , DepartmentRequestDto departmentRequestDto)throws DataNotFoundException, DataAlreadyExistsException{
        Optional<Department> department = departmentRepository.findByIdAndDeletedAtIsNull(id);
        Optional<Department> departmentByName = departmentRepository.findByNameAndDeletedAtIsNull(departmentRequestDto.getName());
        if (!department.isEmpty() && department != null){
            if (departmentByName.isEmpty()){
                Department departmentUpdate = department.get();
                departmentUpdate.setName(departmentRequestDto.getName());
                departmentUpdate.setActive(departmentRequestDto.isActive());
                Department departmentSave = departmentRepository.save(departmentUpdate);
                return DepartmentMapper.fromEntity(departmentSave);
            }else {
                throw new DataAlreadyExistsException("Department With Name :"+departmentRequestDto.getName()+" Already Exists",899L);
            }
        }else {
            throw new DataNotFoundException("Department With Id : "+id+" Not Found",478L);
        }
    }

    /**
     * delete Department With id
     * @param {id}
     * @return {Boolean}
     * @throws {DataNotFoundException}
     */
    public Boolean deleteById(Long id) throws DataNotFoundException {
        Optional<Department> department = departmentRepository.findByIdAndDeletedAtIsNull(id);
        if (!department.isEmpty() && department != null){
            departmentRepository.delete(department.get());
            return true;
        }else {
            throw new DataNotFoundException("Department With Id : "+id + " Not Found",456L);
        }
    }
}
