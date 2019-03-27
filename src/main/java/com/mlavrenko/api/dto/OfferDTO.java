package com.mlavrenko.api.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

public class OfferDTO extends RepresentationModel<OfferDTO> {
    private Long id;
    private String jobTitle;
    private Date startDate;
    private int numberOfApplications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getNumberOfApplications() {
        return numberOfApplications;
    }

    public void setNumberOfApplications(int numberOfApplications) {
        this.numberOfApplications = numberOfApplications;
    }
}
