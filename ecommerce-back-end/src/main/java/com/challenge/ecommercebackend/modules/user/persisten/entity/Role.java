package com.challenge.ecommercebackend.modules.user.persisten.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role", schema = "auth")
public class Role implements Serializable, GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @deprecated Usar RoleName.ADMIN.getCode() en su lugar
     */
    @Deprecated
    public static final String ADMIN = RoleName.ADMIN.getCode();

    /**
     * @deprecated Usar RoleName.USER.getCode() en su lugar
     */
    @Deprecated
    public static final String USER = RoleName.USER.getCode();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    @Column(length = 25, nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "auth.role_status")
    private RoleStatus status;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return name;
    }
}