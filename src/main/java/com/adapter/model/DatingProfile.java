package com.adapter.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = DatingProfileDeserializer.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "dating_profiles")
@Builder
public class DatingProfile {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(unique = true)
    @Setter(AccessLevel.NONE)
    private long profileId;

    @NotBlank
    private String name;

    @Column(nullable = false)
    @NotBlank
    private String purpose;

    @Column(nullable = false)
    @NotEmpty
    private String[] photos;

    @Column(name="isVerified", nullable = false)
    @NotBlank
    private boolean isVerified;

    private String[] tags;

    @Column(nullable = false)
    @NotBlank
    private int age;

    @Column(length = 300)
    private String description;

    @NotBlank
    private String platform;

    private Integer photoRate = null;

    private Integer descriptionRate = null;

    private Boolean isAccepted = null;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DatingProfile that = (DatingProfile) object;
        return profileId == that.profileId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId);
    }
}

