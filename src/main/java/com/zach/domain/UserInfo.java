package com.zach.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "UserInfo")
@Table(name = "tb_user_info")
public class UserInfo {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "user_info__user_fk"
            )
    )
    private User user;

    @Column(name = "city", columnDefinition = "VARCHAR(64) DEFAULT ''")
    private String city;

    @Column(name = "introduction", columnDefinition = "VARCHAR(128) DEFAULT NULL COMMENT 'self introduction, no more than 128 characters'")
    private String intro;

    @Column(name = "fans", columnDefinition = "INT(8) UNSIGNED DEFAULT 0 COMMENT 'Number of fans'")
    private Integer fans;

    @Column(name = "followings", columnDefinition = "INT(8) UNSIGNED DEFAULT 0 COMMENT 'Number of followings'")
    private Integer followings;

    @Column(name = "gender", columnDefinition = "TINYINT(1) UNSIGNED DEFAULT 0 COMMENT 'Gender, 0:Male, 1:Female'")
    private Integer gender;

    @Column(name = "birthday", columnDefinition = "DATE DEFAULT NULL")
    private LocalDate birthday;

    @Column(name = "credits", columnDefinition = "INT(8) DEFAULT 0 COMMENT 'points earned' ")
    private Integer credits;

    @Column(name = "tier", columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT 'Membership tier, 0-9, 0 means unsubscribed'")
    private Integer tier;

    @Column(name = "create_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;
}
