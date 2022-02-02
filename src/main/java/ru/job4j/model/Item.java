package ru.job4j.model;

import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Shegai Evgenii
 * @since 02.02.2022
 * @version 1.0
 */

@Entity
@Table (name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private boolean done;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp created;

    public Item() {
    }

    public Item(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }


    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", description='" + description + '\''
                + ", done=" + done + ", created=" + created + '}';
    }
}
