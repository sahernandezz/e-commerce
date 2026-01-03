package com.challenge.ecommercebackend.modules.user.persisten.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer", schema = "auth")
public class User implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    @Column
    private String password;

    @JsonIgnore
    @Column
    private String recoveryCode;

    @JsonIgnore
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date recoveryDate;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "auth.user_status")
    private UserStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * Obtiene el nombre del rol como String para GraphQL
     */
    public String getRoleName() {
        return role != null ? role.getName() : null;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return this.status.equals(UserStatus.ACTIVE);
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return this.status.equals(UserStatus.ACTIVE);
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return this.status.equals(UserStatus.ACTIVE);
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return this.status.equals(UserStatus.ACTIVE);
    }
}