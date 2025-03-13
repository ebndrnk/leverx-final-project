package org.ebndrnk.leverxfinalproject.model.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.ebndrnk.leverxfinalproject.model.entity.BasicEntity;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.hibernate.annotations.Comment;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "app_user")
@ToString
@Data
public class User extends BasicEntity {

    @Comment("The profile associated with the user. This field is linked to the Profile entity.")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @Comment("The username of the user. Must be unique and between 4 and 50 characters.")
    @Column(name = "USERNAME", length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String username;

    @Comment("The password of the user. Must be between 4 and 100 characters. This field is ignored in JSON serialization.")
    @Column(name = "PASSWORD", length = 100)
    @NotNull
    @Size(min = 4, max = 100)
    @JsonIgnore
    private String password;

    @Comment("The email address of the user. Must be a valid email and between 4 and 50 characters.")
    @Column(name = "EMAIL", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    @Email
    private String email;

    @Comment("The role of the user. This field is ignored in JSON serialization.")
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    @JsonIgnore
    private Role role;

    @Comment("A flag indicating whether the users email has been confirmed. Default is false.")
    @Column(name = "is_email_confirmed", nullable = false)
    @JsonIgnore
    private boolean isEmailConfirmed = false;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) object;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}