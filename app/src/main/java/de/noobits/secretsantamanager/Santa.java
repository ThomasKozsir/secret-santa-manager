package de.noobits.secretsantamanager;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thomas on 06.07.2017.
 */

public class Santa{
    private String firstName, lastName, email;

    Santa(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public String toString(){
        return this.firstName + " " + this.lastName + " " + this.email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
