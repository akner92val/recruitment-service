package com.mlavrenko.api.domain;

import com.mlavrenko.api.domain.enums.ApplicationStatus;

import javax.persistence.*;

@Entity
@Table
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String candidateEmail;
    @Column(nullable = false)
    private String resume;
    @Column(nullable = false)
    @Enumerated
    private ApplicationStatus applicationStatus = ApplicationStatus.APPLIED;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "offer_id", columnDefinition = "integer", referencedColumnName = "id")
    private Offer offer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
