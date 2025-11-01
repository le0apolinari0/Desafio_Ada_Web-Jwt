package br.com.ada.adaApi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "pokemon")
@Cacheable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_poke_api", unique = true, nullable = false)
    private Integer idPokeApi;

    @Column(nullable = false)
    private String name;

    private Integer height;
    private Integer weight;

    @Column(name = "first_ability")
    private String firstAbility;

    @Column(length = 1000)
    private String types;

    @CreationTimestamp
    @Column(name = "cached_at", nullable = false, updatable = false)
    private LocalDateTime cachedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    private Boolean favorite = false;

    @Column(length = 500)
    private String note;
}

