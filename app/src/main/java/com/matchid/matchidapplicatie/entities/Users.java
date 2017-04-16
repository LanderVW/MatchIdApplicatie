package com.matchid.matchidapplicatie.entities;

import java.util.Collection;

/**
 * Created by lander on 16/04/2017.
 */

public class Users {

    private static final long serialVersionUID = 1L;

    private Integer userID;
    private Boolean allowLogin;
    private Boolean allowNewProject;
    private String password;
    private String roles;
    private String username;
    private Collection<Project> projectCollection;
    //private Collection<Groups> groupsCollection;
    //private Company company;
    private boolean valid;





    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private String projectlijst;

    public int[] getProjectlijstArray() {
        String[] strArray = projectlijst.split(",");
        System.out.println("projectlijst eerst: " + projectlijst);
        int[] intArray = new int[strArray.length];
        int a;
        for (a = 0; a < strArray.length; a++) {

            System.out.println(a + " : " + strArray[a]);
            intArray[a] = Integer.parseInt(strArray[a]);
        }

        return intArray;
    }

    public String getProjectlijst(){
        return projectlijst;
    }

    public Users(String a){
        this.username = a;
    }


    public void setProjectlijst(String projectlijst) {
        this.projectlijst = projectlijst;
    }

    public void addProjectlijst(Integer i) {
        System.out.println("toe te voegen: " + i);
        if (projectlijst == null) {
            System.out.println("inde if");

            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append(",");

            projectlijst = sb.toString();
            System.out.println("de uiteindelijke projectlijst " + projectlijst);
        } else {
            System.out.println("in de else");
            String[] strArray = projectlijst.split(",");
            System.out.println("projectlijst eerst: " + projectlijst);
            int[] intArray = new int[strArray.length + 1];
            int a;
            for (a = 0; a < strArray.length; a++) {
                System.out.println(a + " : " + strArray[a]);
                intArray[a] = Integer.parseInt(strArray[a]);
            }
            //eraan toevoegen
            intArray[a] = i;
            System.out.println(intArray[a]);
            //lijst omzetten naar string
            StringBuilder builder = new StringBuilder();
            for (a = 0; a <= strArray.length; a++) {
                builder.append(intArray[a]);
                if (a != strArray.length) {
                    builder.append(",");
                }
            }
            projectlijst = builder.toString();
            System.out.println("de uiteindelijke lijst: " + projectlijst);
        }

    }

    public Users() {
    }

    public Users(Integer id, String uname, String pwd, Boolean allowlogin, Boolean allowproject, String role) {
        userID = id;
        username = uname;
        password = pwd;
        allowLogin = allowlogin;
        allowNewProject = allowproject;
       // company = c;
        roles = role;
    }

    public Users(Integer userID) {
        this.userID = userID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Boolean getAllowLogin() {
        return allowLogin;
    }

    public void setAllowLogin(Boolean allowLogin) {
        this.allowLogin = allowLogin;
    }

    public Boolean getAllowNewProject() {
        return allowNewProject;
    }

    public void setAllowNewProject(Boolean allowNewProject) {
        this.allowNewProject = allowNewProject;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Collection<Project> getProjectCollection() {
        return projectCollection;
    }

    public void setProjectCollection(Collection<Project> projectCollection) {
        this.projectCollection = projectCollection;
    }
/*
    public Collection<Groups> getGroupsCollection() {
        return groupsCollection;
    }

    public void setGroupsCollection(Collection<Groups> groupsCollection) {
        this.groupsCollection = groupsCollection;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userID != null ? userID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userID == null && other.userID != null) || (this.userID != null && !this.userID.equals(other.userID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Users[ userID=" + userID + " ]";
    }

}
