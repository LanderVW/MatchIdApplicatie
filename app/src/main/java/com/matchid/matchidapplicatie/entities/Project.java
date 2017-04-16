package com.matchid.matchidapplicatie.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lander on 16/04/2017.
 */

public class Project {

    private Integer projectID;

    private List<Component> componentlijst;

    private Boolean active;
    private String description;
    private String location;
    private Integer numberAnalysis;
    private String title;
    private Integer companyID;
    public List<Component> getComponentlijst() {
        return this.componentlijst;
    }


    public Project(Integer pid, String title){
        projectID = pid;
        title = this.title;
    }

    public void setComponentlijst(List<Component> componentlijst) {
        this.componentlijst = componentlijst;
    }

    public void addComponentlijst(Component c){
        this.componentlijst.add(c);
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }


    public Project() {
        this.componentlijst= new ArrayList();
    }

    public Project(Integer projectID) {
        this.componentlijst= new ArrayList();
        this.projectID = projectID;
    }


    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getNumberAnalysis() {
        return numberAnalysis;
    }

    public void setNumberAnalysis(Integer numberAnalysis) {
        this.numberAnalysis = numberAnalysis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }




    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectID != null ? projectID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.projectID == null && other.projectID != null) || (this.projectID != null && !this.projectID.equals(other.projectID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Project[ projectID=" + projectID + " ]";
    }


}
