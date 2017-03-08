/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.group2.webapp.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author dfChicken
 */
@Entity
@Table(name = "assessment_claim")
public class AssessmentClaim implements Serializable{

    @EmbeddedId
    private AssessmentClaimPK id;

    @Column(name = "solution1")
    private int solution1;

    @Column(name = "solution2")
    private int solution2;
}

class AssessmentClaimPK implements Serializable {

    @OneToOne
    @JoinColumn(name = "claim_id")
    private Claim claim_id;

    @OneToOne
    @JoinColumn(name = "assessment_crn")
    private Assessment assessment_crn;
}
