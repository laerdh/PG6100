package no.westerdals.pg6100.rest.api.dto;

public class UserDto {

    public Long id;

    public String name;

    public String surname;

    public String address;


    public UserDto() {}


    public UserDto(Long id, String name, String surname, String address) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
    }
}
