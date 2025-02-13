package com.sales_scout.controller;


import com.sales_scout.dto.request.UserRequestDto;
import com.sales_scout.dto.request.create.UserRightsRequestDto;
import com.sales_scout.dto.response.UserResponseDto;
import com.sales_scout.exception.DataAlreadyExistsException;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.exception.DuplicateKeyValueException;
import com.sales_scout.exception.UserAlreadyExistsException;
import com.sales_scout.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * get List Of Users
     * @return {ResponseEntity<List<UserResponseDto>>}
     * @throws {DataNotFoundException}
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUser() throws DataNotFoundException {
        try{
            List<UserResponseDto> users = userService.getAllUser();
            return ResponseEntity.ok(users);
        }catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    /**
     * get User By Id
     * @param {id}
     * @return {ResponseEntity<UserResponseDto>}
     * @throws {DataNotFoundException}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) throws DataNotFoundException{
        try {
            UserResponseDto user= userService.findById(id);
            return ResponseEntity.ok(user);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    /**
     * soft delete User By Id
     * @param {id}
     * @return {ResponseEntity<Boolean>}
     * @throws {EntityNotFoundException}
     */
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Boolean> softDeleteUser(@PathVariable Long id) throws EntityNotFoundException{
        try {
            return ResponseEntity.ok(userService.softDeleteUser(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    /**
     * restore User By Id
     * @param {id}
     * @return {ResponseEntity<Boolean>}
     * @throws {EntityNotFoundException}
     */
    @PutMapping("/restore/{id}")
    public ResponseEntity<Boolean> restoreUser(@PathVariable Long id) throws EntityNotFoundException{
        try {
            return ResponseEntity.ok(userService.restoreUser(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    /**
     * create User
     * @param {userRequestDto}
     * @return {ResponseEntity<UserResponseDto>}
     * @throws {UserAlreadyExistsException}
     */
    @PostMapping("/create-user")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) throws UserAlreadyExistsException {
        try {
            return ResponseEntity.ok(userService.createUser(userRequestDto));
        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException(e.getMessage());
        }
    }

    /**
     * update User By Id
     * @param {id}
     * @param {userRequestDto}
     * @return {ResponseEntity<UserResponseDto>}
     * @throws {DataNotFoundException}
     * @throws {DuplicateKeyValueException}
     */
    @PutMapping("/update-user/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,@RequestBody UserRequestDto userRequestDto) throws DataNotFoundException, DuplicateKeyValueException {
        try {
            return ResponseEntity.ok(userService.updateUser(id,userRequestDto));
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }catch (DuplicateKeyValueException e){
            throw new DuplicateKeyValueException(e.getMessage(),e.getCause());
        }
    }

    /**
     * update User Role By Id
     * @param {id}
     * @param {userRequestDto}
     * @return {ResponseEntity<UserResponseDto>}
     * @throws {DataNotFoundException}
     */
    @PutMapping("/update-user-role/{id}")
    public ResponseEntity<UserResponseDto> updateUserRole(@PathVariable Long id , @RequestBody UserRequestDto userRequestDto) throws DataNotFoundException{
        try {
            return ResponseEntity.ok(userService.updateUserRole(id,userRequestDto));
        }catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    /**
     * Add Rights To User By userId And rightsId
     * @param {userRightsRequestDto}
     * @return {ResponseEntity<UserResponseDto>}
     * @throws {DataNotFoundException}
     * @throws {DataAlreadyExistsException}
     */
    @PostMapping("/add-rights")
    public ResponseEntity<UserResponseDto> addRightsToUser(@RequestBody UserRightsRequestDto userRightsRequestDto)throws DataNotFoundException, DataAlreadyExistsException{
        try{
            return ResponseEntity.ok(userService.addRightsToUser(userRightsRequestDto));
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage() , e.getCode());
        }
    }

    /**
     * Remove Rights From User By userId And rightsId
     * @param {userRightsRequestDto}
     * @return {ResponseEntity<UserResponseDto>}
     * @throws {DataNotFoundException}
     */
    @PostMapping("/remove-rights")
    public ResponseEntity<UserResponseDto> removeRightsFromUser(@RequestBody UserRightsRequestDto userRightsRequestDto)throws DataNotFoundException{
        try{
            return ResponseEntity.ok(userService.removeRightsFromUser(userRightsRequestDto));
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(), e.getCode());
        }
    }

}
