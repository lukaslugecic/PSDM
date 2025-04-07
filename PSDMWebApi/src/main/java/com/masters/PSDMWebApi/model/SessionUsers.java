package com.masters.PSDMWebApi.model;

import jakarta.persistence.*;
import com.masters.PSDMWebApi.model.id.SessionUsersId;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "session_users")
public class SessionUsers {

    @EmbeddedId
    private SessionUsersId id;

    @ManyToOne
    @MapsId("sessionId")
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public SessionUsers() {
    }

    public SessionUsers(Session session, User user) {
        this.session = session;
        this.user = user;
        this.id = new SessionUsersId(session.getId(), user.getId());
    }

    // Getters and Setters
}

