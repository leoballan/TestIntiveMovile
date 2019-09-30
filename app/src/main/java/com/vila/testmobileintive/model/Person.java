package com.vila.testmobileintive.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Person
{

        @SerializedName("name")
        @Expose
        private Name name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("login")
        @Expose
        private Login login;
        @SerializedName("picture")
        @Expose
        private Picture picture;

        public Person (){}

        public Name getName() {
            return name;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Login getLogin() {
            return login;
        }

        public void setLogin(Login login) {
            this.login = login;
        }

        public Picture getPicture() {
            return picture;
        }

        public void setPicture(Picture picture) {
            this.picture = picture;
        }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) &&
                Objects.equals(email, person.email) &&
                Objects.equals(login, person.login) &&
                Objects.equals(picture, person.picture);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, email, login, picture);
    }
}
