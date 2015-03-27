package com.onpoint.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ClaimCase.
 */
@Entity
@Table(name = "T_CLAIMCASE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClaimCase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "legacy_case_number", nullable = false)
    private String legacyCaseNumber;

    @ManyToOne
    private Employer employer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLegacyCaseNumber() {
        return legacyCaseNumber;
    }

    public void setLegacyCaseNumber(String legacyCaseNumber) {
        this.legacyCaseNumber = legacyCaseNumber;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClaimCase claimCase = (ClaimCase) o;

        if ( ! Objects.equals(id, claimCase.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClaimCase{" +
                "id=" + id +
                ", legacyCaseNumber='" + legacyCaseNumber + "'" +
                '}';
    }
}
