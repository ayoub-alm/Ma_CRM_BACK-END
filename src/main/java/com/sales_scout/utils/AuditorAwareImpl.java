//package com.sales_scout.utils;
//
//import com.sales_scout.Auth.SecurityUtils;
//import com.sales_scout.entity.UserEntity;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.stereotype.Component;
//import java.util.Optional;
//
//@Component
//public class AuditorAwareImpl implements AuditorAware<UserEntity> {
//
//    @Override
//    public Optional<UserEntity> getCurrentAuditor() {
//        return Optional.ofNullable(SecurityUtils.getCurrentUser());
//    }
//}
