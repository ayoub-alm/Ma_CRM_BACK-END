package com.sales_scout.controller.data;


import com.sales_scout.dto.request.DepartmentRequestDto;
import com.sales_scout.dto.response.DepartmentResponseDto;
import com.sales_scout.entity.data.Department;
import com.sales_scout.exception.DataAlreadyExistsException;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.service.data.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Get all department
     * @return {List<DepartmentResponseDto>}
     * @throws {DataNotFoundException}
     */
    @GetMapping
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments() throws DataNotFoundException {
        try {
            return ResponseEntity.ok(departmentService.findAll());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(), e.getCode());
        }
    }

    /**
     * Get Department With id
     * @param {id}
     * @return {DepartmentResponseDto}
     * @throws {DataNotFoundException}
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> getDepartmentById(@PathVariable Long id) throws DataNotFoundException {
        try{
            DepartmentResponseDto department = departmentService.findById(id);
            return ResponseEntity.ok(department);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(), e.getCode());
        }
    }

    /**
     * create Department
     * @param {departmentRequestDto}
     * @return {DepartmentResponseDto}
     * @throws {DataAlreadyExistsException}
     */
    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(@RequestBody DepartmentRequestDto departmentRequestDto) throws DataAlreadyExistsException {
       try{
           return ResponseEntity.ok(departmentService.save(departmentRequestDto));
       } catch (DataAlreadyExistsException e) {
           throw new DataAlreadyExistsException(e.getMessage(),e.getCode());
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
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentRequestDto departmentRequestDto) throws DataNotFoundException, DataAlreadyExistsException {
        try{
            DepartmentResponseDto updatedDepartment = departmentService.updateDepartment(id,departmentRequestDto);
            return ResponseEntity.ok(updatedDepartment);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(), e.getCode());
        } catch (DataAlreadyExistsException e) {
            throw new DataAlreadyExistsException(e.getMessage(), e.getCode());
        }
    }

    /**
     * delete Department
     * @param {id}
     * @return {Boolean}
     * @throws {DataNotFoundException}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteDepartment(@PathVariable Long id) throws DataNotFoundException {
        try{
            return ResponseEntity.ok(departmentService.deleteById(id));
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(), e.getCode());
        }
    }
}
