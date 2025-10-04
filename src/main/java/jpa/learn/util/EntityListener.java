package jpa.learn.util;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class EntityListener {

	@PrePersist
    public void prePersist(Object entity) {
        System.out.println("Before inserting: " + entity.toString());
    }

    @PostPersist
    public void postPersist(Object entity) {
        System.out.println("Inserted: " + entity.toString());
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        System.out.println("Before updating: " + entity.toString());
    }
    
    @PostUpdate
    public void postUpdate(Object entity) {
        System.out.println("Before updating: " + entity.toString());
    }
    
    @PreRemove
    public void preRemove(Object entity) {
        System.out.println("Before updating: " + entity.toString());
    }
    
    @PostRemove
    public void postRemove(Object entity) {
        System.out.println("Before updating: " + entity.toString());
    }

    @PostLoad
    public void postLoad(Object entity) {
        System.out.println("Loaded from DB: " + entity.toString());
    }
}
