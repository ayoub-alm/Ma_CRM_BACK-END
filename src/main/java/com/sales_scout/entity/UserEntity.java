package com.sales_scout.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class UserEntity extends BaseEntity implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String name;

        @Column(unique = true, nullable = false)
        private String password;

        @Email
        @Column(unique = true, nullable = false)
        private String email;

        private String phone;
        private String refreshToken;
        private String logo;
        private String matriculate;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "role_id")
        private Role role; // Each user has one role.

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<UserRights> userRights = new ArrayList<>(); // Each user can have multiple rights.

        @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinTable(
                name = "user_company",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "company_id")
        )
        private Set<Company> companies;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptyList(); // ✅ Change this to return actual roles if needed
        }

        @Override
        public String getUsername() {
                return this.email; // ✅ Use email as username
        }

        @Override
        public boolean isAccountNonExpired() {
                return true;
        }

        @Override
        public boolean isAccountNonLocked() {
                return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return true;
        }

        @Override
        public boolean isEnabled() {
                return true;
        }

        @Override
        public String getPassword() {
                return this.password;
        }
}
