package com.sales_scout.service.imp;

import com.sales_scout.config.AuthConfig;
import com.sales_scout.dto.request.UserRequestDto;
import com.sales_scout.dto.response.UserResponseDto;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.exception.UserAlreadyExistsException;
import com.sales_scout.repository.UserRepository;
import com.sales_scout.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthConfig authConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        System.out.println("Retrieved Data");
        System.out.println("Password: " + user.getPassword());
        System.out.println("Username: " + user.getUsername());
        System.out.println("ID: " + user.getId());
        System.out.println("Email: " + user.getEmail());
        System.out.println("-----");

        return user;
    }

    @Override
    public List<UserResponseDto> getAllUser() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream()
                .map(this::userEntityToUserRespDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        Optional<UserEntity> foundUser = userRepository.findByEmail(userRequestDto.getEmail());

        if (foundUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + userRequestDto.getEmail() + " already exists");
        }

        UserEntity user = userReqDtoToUserEntity(userRequestDto);
        user.setPassword(authConfig.passwordEncoder().encode(user.getPassword()));

        UserEntity createdUser = userRepository.save(user);
        return userEntityToUserRespDto(createdUser);
    }

    @Override
    public UserResponseDto findById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));

        return userEntityToUserRespDto(user);
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        UserEntity user =  this.userRepository.findByEmail(email).orElseThrow(()->
            new UsernameNotFoundException("User not found with ID: " + email));
        return userEntityToUserRespDto(user);
    }


    private UserEntity userReqDtoToUserEntity(UserRequestDto userReqDto) {
        return UserEntity.builder()
                .email(userReqDto.getEmail())
                .password(userReqDto.getPassword())
                .name(userReqDto.getName())
//                .phone(userReqDto.getPhone())
                .build();
    }

    private UserResponseDto userEntityToUserRespDto(UserEntity user) {
        return UserResponseDto.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .aboutMe(user.getName())
                .id(user.getId())
                .role(user.getRole())
                .rights(user.getRights())
                .build();
    }
}
