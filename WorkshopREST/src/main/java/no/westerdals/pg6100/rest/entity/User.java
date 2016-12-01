package no.westerdals.pg6100.rest.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(max = 256)
    private String name;

    @NotNull
    @Size(max = 256)
    private String surname;

    @NotNull
    private String address;


    public User() {}


    public void setId(Long id) { this.id = id; }

    public Long getId() { return id; }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getSurname() { return surname; }

    public void setAddress(String address) { this.address = address; }

    public String getAddress() { return address; }
}
