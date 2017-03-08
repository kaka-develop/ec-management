/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.group2.webapp.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dfChicken
 */
@Entity
@Table(name = "claim_circumstances")
public class ClaimCircumstances implements Serializable {

    @EmbeddedId
    private ClaimCircumstancesPK id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(columnDefinition = "TEXT", unique = true, nullable = true)
    private String other_circumstances;
}

@Embeddable
class ClaimCircumstancesPK implements Serializable{

    @OneToOne
    @JoinColumn(name = "claim_id")
    private Claim claim_id;
    
    @OneToOne
    @JoinColumn(name = "circumstances_id")
    private Circumstances circumstances_id;
}
