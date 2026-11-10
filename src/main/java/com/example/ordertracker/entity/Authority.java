package com.example.ordertracker.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @Override
    public String getAuthority() {
        return authority;
    }
}
