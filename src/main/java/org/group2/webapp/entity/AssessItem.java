package org.group2.webapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "assessitem")
public class AssessItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, unique = true, nullable = false)
    private String title;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "assessment_assessitem",
            joinColumns = {@JoinColumn(name = "assessitem_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "assessment_crn", referencedColumnName = "crn")})
    private Set<Assessment> assessment = new HashSet<>();

    public AssessItem() {
    }

    public AssessItem(String title) {
        this.title  = title;
    }

    public void setAssessment(Set<Assessment> assessment) {
        this.assessment = assessment;
    }

    public Set<Assessment> getAssessment() {
        return assessment;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    @Override
    public String toString() {
        return "AssessItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
