package com.masters.PSDMWebApi.model.id;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class SessionUsersId implements Serializable {

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "user_id")
    private Long userId;

    public SessionUsersId() {
    }

    public SessionUsersId(Long sessionId, Long userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionUsersId)) return false;
        SessionUsersId that = (SessionUsersId) o;
        return Objects.equals(sessionId, that.sessionId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, userId);
    }

}

