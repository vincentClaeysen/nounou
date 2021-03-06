package org.mat.nounou.vo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Value Object for a child
 * ChildVO: mlecoutre
 * Date: 28/10/12
 * Time: 12:25
 */
@XmlRootElement
public class ChildVO {
    private Integer childId;
    private String firstName;
    private String lastName;
    private String birthday;
    private String nurseName;
    private Integer nurseId;
    private Integer accountId;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    @Override
    public String toString() {
        return "ChildVO{" +
                "childId=" + childId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nurseName='" + nurseName + '\'' +
                ", nurseId='" + nurseId + '\'' +
                ", accountId=" + accountId +
                '}';
    }
}
