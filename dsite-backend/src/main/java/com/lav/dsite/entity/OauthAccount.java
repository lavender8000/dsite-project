package com.lav.dsite.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.lav.dsite.enums.OauthProvider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.persistence.ForeignKey;

import lombok.Data;

@Data
@Entity
@Table(name = "oauth_accounts", 
        indexes = {
            @Index(name = "idx_oauth_accounts_user_id", columnList = "user_id"),
            @Index(name = "idx_oauth_accounts_oauth_email", columnList = "oauth_email")
        },
        uniqueConstraints = {
            @UniqueConstraint(name = "uq_oauth_accounts_oauth_provider_oauth_id", columnNames = {"oauth_provider", "oauth_id"})
        }
)
@SQLDelete(sql = "UPDATE oauth_accounts SET deleted_at = CURRENT_TIMESTAMP, version = version + 1 WHERE id = ? AND version = ?")
@SQLRestriction("deleted_at IS NULL")
public class OauthAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_oauth_accounts_users_id"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider", columnDefinition = "ENUM('GOOGLE')")
    private OauthProvider oauthProvider;

    @Column(name = "oauth_id", length = 255)
    private String oauthId;

    @Column(name = "oauth_email", length = 320)
    private String oauthEmail;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    @Version
    private Integer version;

}