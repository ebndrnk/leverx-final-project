package org.ebndrnk.leverxfinalproject.model.projection;


public interface ProfilePreview {
    Long getId();
    String getUsername();
    String getFirstname();
    String getEmail();
    String getLastname();
    boolean isConfirmedByAdmin();
    byte getRating();
}
