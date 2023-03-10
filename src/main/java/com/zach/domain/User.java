package com.zach.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity(name = "User")
@Table(name = "tb_user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "password must be provided")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "username must be provided")
    @Column(name = "username", unique = true, nullable = false, columnDefinition = "VARCHAR(32)")
    private String username;

    @Column(name = "phone", columnDefinition = "VARCHAR(10) DEFAULT ''")
    private String phone;

    @Column(name = "avatar", columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String avatar;

    @Column(name = "create_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;

    @Column(name = "update_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

    @OneToOne(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private UserInfo userInfo;

}
