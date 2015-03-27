package com.onpoint.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.onpoint.domain.util.CustomDateTimeDeserializer;
import com.onpoint.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Hearing.
 */
@Entity
@Table(name = "T_HEARING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Hearing implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "hearing_date", nullable = false)
    private DateTime hearingDate;

    @Column(name = "location")
    private String location;

    @ManyToOne
    private ClaimCase claimCase;

    @OneToOne
    private User hearingRep;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getHearingDate() {
        return hearingDate;
    }

    public void setHearingDate(DateTime hearingDate) {
        this.hearingDate = hearingDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ClaimCase getClaimCase() {
        return claimCase;
    }

    public void setClaimCase(ClaimCase claimCase) {
        this.claimCase = claimCase;
    }

    public User getHearingRep() {
        return hearingRep;
    }

    public void setHearingRep(User user) {
        this.hearingRep = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Hearing hearing = (Hearing) o;

        if ( ! Objects.equals(id, hearing.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Hearing{" +
                "id=" + id +
                ", hearingDate='" + hearingDate + "'" +
                ", location='" + location + "'" +
                '}';
    }
}
