package no.westerdals.pg6100.rest.api.dto;

import no.westerdals.pg6100.rest.entity.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserConverter {

    private UserConverter() {}

    public static UserDto transform(User entity) {
        Objects.requireNonNull(entity);

        UserDto dto = new UserDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.surname = entity.getSurname();
        dto.address = entity.getAddress();

        return dto;
    }

    public static List<UserDto> transform(List<User> entities) {
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(UserConverter::transform)
                .collect(Collectors.toList());
    }
}
