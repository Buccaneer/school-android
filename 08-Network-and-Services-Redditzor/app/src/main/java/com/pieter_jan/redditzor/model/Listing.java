package com.pieter_jan.redditzor.model;

import java.util.List;
import com.pieter_jan.redditzor.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import de.greenrobot.dao.AbstractDao;
// KEEP INCLUDES END
/**
 * Entity mapped to table "LISTING".
 */
public class Listing {

    private Long id;
    private String before;
    private String after;
    private String category;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ListingDao myDao;

    private List<Post> posts;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Listing() {
    }

    public Listing(Long id) {
        this.id = id;
    }

    public Listing(Long id, String before, String after, String category) {
        this.id = id;
        this.before = before;
        this.after = after;
        this.category = category;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getListingDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Post> getPosts() {
        if (posts == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PostDao targetDao = daoSession.getPostDao();
            List<Post> postsNew = targetDao._queryListing_Posts(id);
            synchronized (this) {
                if(posts == null) {
                    posts = postsNew;
                }
            }
        }
        return posts;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetPosts() {
        posts = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    public Listing(List<Post> posts, String category)
    {
        this.posts = posts;
        this.category = category;
    }

    public Listing(List<Post> posts, String before, String after)
    {
        this.posts = posts;
        this.before = before;
        this.after = after;
    }
    // KEEP METHODS END

}
