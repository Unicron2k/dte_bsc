package com.example.a8_3_db_comms_room.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ContactTable",
        foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "idCategory",
        childColumns = "idCategory",
        onDelete = ForeignKey.CASCADE))
public class Contact implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idContact")
    private int idContact = 0;

    @NonNull
    @ColumnInfo(name = "lastname")
    private String lastname = "lastname";

    @NonNull
    @ColumnInfo(name = "firstname")
    private String firstname = "firstname";

    @NonNull
    @ColumnInfo(name = "fblink")
    private String fblink = "fb_link";

    @NonNull
    @ColumnInfo(name = "email")
    private String email = "email";

    @NonNull
    @ColumnInfo(name = "phone")
    private String phone = "phone";

    @NonNull
    @ColumnInfo(name = "birthyear")
    private String birthyear = "birthyear";

    @ColumnInfo(name = "idCategory")
    private int idCategory = 0;

    public Contact(int idContact, @NonNull String lastname, @NonNull String firstname, @NonNull String fblink, @NonNull String email, @NonNull String phone, @NonNull String birthyear, int idCategory) {
        this.idContact = idContact;
        this.lastname = lastname;
        this.firstname = firstname;
        this.fblink = fblink;
        this.email = email;
        this.phone = phone;
        this.birthyear = birthyear;
        this.idCategory = idCategory;
    }

    public int getIdContact() { return idContact; }

    @NonNull
    public String getLastname() { return lastname; }

    @NonNull
    public String getFirstname() { return firstname; }

    @NonNull
    public String getFblink() { return fblink; }

    @NonNull
    public String getEmail() { return email; }

    @NonNull
    public String getPhone() { return phone; }

    @NonNull
    public String getBirthyear() { return birthyear; }

    public int getIdCategory() { return idCategory; }

    public void setIdContact(int idContact) { this.idContact = idContact; }

    public void setLastname(@NonNull String lastname) { this.lastname = lastname; }

    public void setFirstname(@NonNull String firstname) { this.firstname = firstname; }

    public void setFblink(@NonNull String fblink) { this.fblink = fblink; }

    public void setEmail(@NonNull String email) { this.email = email; }

    public void setPhone(@NonNull String phone) { this.phone = phone; }

    public void setBirthyear(@NonNull String birthyear) { this.birthyear = birthyear; }

    public void setIdCategory(int idCategory) { this.idCategory = idCategory; }
}
