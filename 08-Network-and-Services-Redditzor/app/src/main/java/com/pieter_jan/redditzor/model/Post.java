package com.pieter_jan.redditzor.model;

import com.pieter_jan.redditzor.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import de.greenrobot.dao.AbstractDao;
import android.os.Parcel;
import android.os.Parcelable;
// KEEP INCLUDES END
/**
 * Entity mapped to table "POST".
 */
public class Post implements android.os.Parcelable {

    private Long id;
    private String title;
    private String text;
    private String url;
    private String thumbnail;
    private String image;
    private Long listingId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PostDao myDao;

    private Listing listing;
    private Long listing__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel in) {
            return new Post(in.readString(), in.readString(), in.readString(), in.readString(), in.readString());
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
    // KEEP FIELDS END

    public Post() {
    }

    public Post(Long id) {
        this.id = id;
    }

    public Post(Long id, String title, String text, String url, String thumbnail, String image, Long listingId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.url = url;
        this.thumbnail = thumbnail;
        this.image = image;
        this.listingId = listingId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPostDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    /** To-one relationship, resolved on first access. */
    public Listing getListing() {
        Long __key = this.listingId;
        if (listing__resolvedKey == null || !listing__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ListingDao targetDao = daoSession.getListingDao();
            Listing listingNew = targetDao.load(__key);
            synchronized (this) {
                listing = listingNew;
            	listing__resolvedKey = __key;
            }
        }
        return listing;
    }

    public void setListing(Listing listing) {
        synchronized (this) {
            this.listing = listing;
            listingId = listing == null ? null : listing.getId();
            listing__resolvedKey = listingId;
        }
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
    public Post(String title, String text, String url, String thumbnail, String image) {
        this.title = title;
        this.text = text;
        this.url = url;
        this.thumbnail = thumbnail;
        this.image = image;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(url);
        dest.writeString(thumbnail);
        dest.writeString(image);
    }
    // KEEP METHODS END

}