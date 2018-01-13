package com.github.eoinf.jiggen;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class CachedPuzzle implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Basic
    private LocalDateTime created;

    public LocalDateTime getCreated() {
        return created;
    }
    public void setCreated(LocalDateTime dateTime) {
        created = dateTime;
    }
}
