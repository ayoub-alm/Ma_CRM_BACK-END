package com.sales_scout.service.imp;

import com.sales_scout.config.AuthConfig;
import com.sales_scout.dto.request.UserRequestDto;
import com.sales_scout.dto.request.create.UserRightsRequestDto;
import com.sales_scout.dto.response.UserResponseDto;
import com.sales_scout.entity.CompanyDepartment;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.entity.UserRights;
import com.sales_scout.exception.DataAlreadyExistsException;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.exception.DuplicateKeyValueException;
import com.sales_scout.exception.UserAlreadyExistsException;
import com.sales_scout.mapper.UserMapper;
import com.sales_scout.repository.*;
import com.sales_scout.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserImp implements UserService {

    private static final String IMAGE_DIRECTORY ="src/main/resources/static/images/";


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RightRepository rightRepository;
    private final UserRightsRepository userRightsRepository;
    private final CompanyDepartmentRepository companyDepartmentRepository;
    private final AuthConfig authConfig;
    private final UserMapper userMapper;

    public UserImp(UserRepository userRepository, RoleRepository roleRepository, RightRepository rightRepository, UserRightsRepository userRightsRepository, CompanyDepartmentRepository companyDepartmentRepository, AuthConfig authConfig, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.rightRepository = rightRepository;
        this.userRightsRepository = userRightsRepository;
        this.companyDepartmentRepository = companyDepartmentRepository;
        this.authConfig = authConfig;
        this.userMapper = userMapper;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

//        System.out.println("Retrieved Data");
//        System.out.println("Password: " + user.getPassword());
//        System.out.println("Username: " + user.getUsername());
//        System.out.println("ID: " + user.getId());
//        System.out.println("Email: " + user.getEmail());
//        System.out.println("-----");

        return user;
    }

    /**
     * get All users
     * @return List of {UserResponseDto}
     * @throws {DataNotFoundException}
     */
    @Override
    public List<UserResponseDto> getAllUser() throws DataNotFoundException {
       Optional<List<UserEntity>> userEntities = userRepository.findAllByDeletedAtIsNull();
       if(!userEntities.get().isEmpty()){
           return userEntities.get().stream()
                   .map(userMapper::fromEntity)
                   .collect(Collectors.toList());
       }else{
           throw new DataNotFoundException("Data of Users Not Found : List of Users Not Found",999L);
       }
    }

    /**
     * create user
     * @param {userRequestDto}
     * @return {UserResponseDto}
     * @throws {UserAlreadyExistsException}
     */
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws UserAlreadyExistsException {
        try{
            String imagePath = null;
            if (userRequestDto.getLogo() != null && !userRequestDto.getLogo().isEmpty()) {
                imagePath = saveImageFromBase64(userRequestDto.getLogo());
            } else if (userRequestDto.getLogo() == null) {
                imagePath = null;
            }
            Optional<UserEntity> foundUser = userRepository.findByEmail(userRequestDto.getEmail());
            if (!foundUser.isPresent()) {
                if (userRequestDto.getRole() == null) {
                    userRequestDto.setRole(roleRepository.findByRoleAndDeletedAtIsNull("User"));
                }
                userRequestDto.setLogo(imagePath != null && !imagePath.isEmpty() ? imagePath : "");
                UserEntity user = userMapper.fromDto(userRequestDto);
                user.setPassword(authConfig.passwordEncoder().encode(user.getPassword()));

                UserEntity createdUser = userRepository.save(user);

                return userMapper.fromEntity(createdUser);
            } else {
                throw new UserAlreadyExistsException("User with email " + userRequestDto.getEmail() + " already exists");
            }
        }catch (IOException e){
            throw new RuntimeException("Failed to save user logo for : "+ e.getMessage());
        }
    }

    /**
     * update user By Id
     * @param {id}
     * @param {userRequestDto}
     * @return {UserResponseDto}
     * @throws {DataNotFoundException}
     * @throws {DuplicateKeyValueException}
     */
    @Override
    public UserResponseDto updateUser(Long id , UserRequestDto userRequestDto) throws DataNotFoundException, DuplicateKeyValueException {
        try {
            Optional<UserEntity> user = userRepository.findByDeletedAtIsNullAndId(id);

            if (!user.isPresent()) {
                throw new DataNotFoundException("Data of User Not Found: User With Id " + id + " Not Found", 222L);
            }

            UserEntity userEntity = user.get();
            userEntity.setName(userRequestDto.getName());
            userEntity.setEmail(userRequestDto.getEmail());
            userEntity.setPhone(userRequestDto.getPhone());
            userEntity.setRole(userRequestDto.getRole());
            userEntity.setMatriculate(userRequestDto.getMatriculate());

            return userMapper.fromEntity(userRepository.save(userEntity));
        }catch (DataNotFoundException e){
            throw new DataNotFoundException("Data of User Not Found : User With Id "+id+" Not Found",222L);
        }catch (DataIntegrityViolationException e){
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new DuplicateKeyValueException("User with the same key already exists: " + userRequestDto.getEmail(),e.getCause());
            }
            throw new RuntimeException("Data integrity violation: " + e.getMessage(), e);
        }
    }

    /**
     *  get User By Id
     * @param {id}
     * @return {UserResponseDto}
     * @throws {DataNotFoundException}
     */
    @Override
    public UserResponseDto findById(Long id) throws DataNotFoundException {
        Optional<UserEntity> user = userRepository.findByDeletedAtIsNullAndId(id);
        if(!user.isEmpty() && user != null){
        return userMapper.fromEntity(user.get());
        }else{
         throw new DataNotFoundException("Data of User Not Found : User With ID "+ id + " Not Found",957L);
        }
    }

    /**
     * get user By Email
     * @param {email}
     * @return {UserResponseDto}
     * @throws {DataNotFoundException}
     */
    @Override
    public UserResponseDto findByEmail(String email) throws DataNotFoundException {
        Optional<UserEntity> user =  this.userRepository.findByEmailAndDeletedAtIsNull(email);
        if(!user.isEmpty() && user != null){
          return userMapper.fromEntity(user.get());
        }else {
            throw new DataNotFoundException("Data Of User Not Found : User With This Email " + email + " Not Found", 888L);
        }
    }

    /**
     * soft delete User By Id
     * @param {id}
     * @return {Boolean}
     * @throws {EntityNotFoundException}
     */
    @Override
    public Boolean softDeleteUser(Long id)throws EntityNotFoundException{
        Optional<UserEntity> user= userRepository.findByDeletedAtIsNullAndId(id);
        if(user.isPresent()){
            user.get().setDeletedAt(LocalDateTime.now());
            userRepository.save(user.get());
            return true;
        }else {
            throw new EntityNotFoundException("User with ID " + id + " not found or already deleted.");
        }
    }

    /**
     * restore User By Id
     * @param {id}
     * @return {Boolean}
     * @throws {EntityNotFoundException}
     */
    @Override
    public Boolean restoreUser(Long id)throws EntityNotFoundException{
        Optional<UserEntity> user= userRepository.findByDeletedAtIsNotNullAndId(id);
        if(user.isPresent()){
            user.get().setDeletedAt(null);
            userRepository.save(user.get());
            return true;
        }else {
            throw new EntityNotFoundException("User with ID " + id + " not found or already restored.");
        }
    }

    /**
     * update user Role By Id
     * @param {id}
     * @param {userRequestDto}
     * @return {UserResponseDto}
     * @throws {DataNotFoundException}
     */
    @Override
    public UserResponseDto updateUserRole(Long id , UserRequestDto userRequestDto) throws DataNotFoundException {
        Optional<UserEntity> user = userRepository.findByDeletedAtIsNullAndId(id);
        if (!user.isEmpty()){
            UserEntity userEntity = user.get();
            userEntity.setRole(roleRepository.findByIdAndDeletedAtIsNull(userEntity.getRole().getId()));
            return userMapper.fromEntity(userRepository.save(userEntity));
        }else{
            throw new DataNotFoundException("Data of User Not Found : User With Id "+id+" Not Found",222L);
        }
    }

    private String  saveImageFromBase64(String base64Image)throws IOException{
        if (base64Image == null || base64Image.isEmpty()){
            return null;
        }
        String[] parts = base64Image.split(",");
        if (parts.length != 1){
            throw  new IllegalArgumentException("Invalid Base64 image format");
        }
        byte[] decodeBytes = Base64.getDecoder().decode(parts[0]);

        String fileName = "user_logo_"+System.currentTimeMillis()+".png";
        Path imagePath = Paths.get(IMAGE_DIRECTORY, fileName);

        Files.createDirectories(imagePath.getParent());
        try(FileOutputStream fos = new FileOutputStream(imagePath.toFile())) {
            fos.write(decodeBytes);
        }
        return fileName;
    }

    /**
     * add Rights To User With userId And List of rightsId
     * @param {userRightsRequestDto}
     * @return {UserResponseDto}
     * @throws {DataNotFoundException}
     * @throws {DataAlreadyExistsException}
     */
    @Override
    public UserResponseDto addRightsToUser(UserRightsRequestDto userRightsRequestDto)throws DataNotFoundException, DataAlreadyExistsException {
        Optional<UserEntity> user= userRepository.findByDeletedAtIsNullAndId(userRightsRequestDto.getUserId());
        if(!user.isEmpty() && user != null){
        for (int i = 0 ; i < userRightsRequestDto.getRightsId().size() ; i++){
            Optional<UserRights> userRightsIsExisted = userRightsRepository.findByUserIdAndRightIdAndDeletedAtIsNull(userRightsRequestDto.getUserId(), userRightsRequestDto.getRightsId().get(i));
            if (!userRightsIsExisted.isPresent()){
                UserRights userRights = new UserRights();
                userRights.setRight(rightRepository.findByIdAndDeletedAtIsNull(userRightsRequestDto.getRightsId().get(i)));
                userRights.setUser(userRepository.findByDeletedAtIsNullAndId(userRightsRequestDto.getUserId()).get());
                userRightsRepository.save(userRights);
            }
        }
            return userMapper.fromEntity(user.get());
        }else {
            throw new DataNotFoundException("User Data Not Found : User with Id "+userRightsRequestDto.getUserId()+" Not Found" , 589L);
        }
    }

    /**
     * remove Rights From User With userId And List of rightsId
     * @param {userRightsRequestDto}
     * @return {UserResponseDto}
     * @throws {DataNotFoundException}
     */
    @Override
    public  UserResponseDto removeRightsFromUser(UserRightsRequestDto userRightsRequestDto)throws DataNotFoundException{
        Optional<UserEntity> user= userRepository.findByDeletedAtIsNullAndId(userRightsRequestDto.getUserId());
        if (!user.isEmpty() && user != null){
        for (int i = 0; i < userRightsRequestDto.getRightsId().size() ; i++) {
                Optional<UserRights> userRights = userRightsRepository.findByUserIdAndRightIdAndDeletedAtIsNull(userRightsRequestDto.getUserId(), userRightsRequestDto.getRightsId().get(i));
                if (!userRights.isEmpty() && userRights != null){
                    UserRights userRightsAction = userRights.get();
                    userRightsRepository.delete(userRightsAction);
                }else {
                }
            }
            return userMapper.fromEntity(user.get());
        }else {
            throw new DataNotFoundException("User Rights Data Not Found ",888L);
        }
    }



}
