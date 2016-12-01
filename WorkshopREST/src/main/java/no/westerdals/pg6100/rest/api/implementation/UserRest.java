package no.westerdals.pg6100.rest.api.implementation;

import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import no.westerdals.pg6100.rest.ApplicationConfig;
import no.westerdals.pg6100.rest.api.dto.UserConverter;
import no.westerdals.pg6100.rest.api.dto.UserDto;
import no.westerdals.pg6100.rest.api.utils.Formats;
import no.westerdals.pg6100.rest.ejb.UserEJB;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

@Api(value = "/users", description = "API for Users")
@Path("/users")
@Produces(Formats.BASE_JSON)
@Consumes(Formats.BASE_JSON)
public class UserRest {

    @EJB
    private UserEJB userEJB;


    @ApiOperation("Get all users")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Returns a list of all users"),
            @ApiResponse(code = 404, message = "No users found")
    })
    @GET
    public List<UserDto> getAllUsers() {
        return UserConverter.transform(userEJB.getUsers());
    }


    @ApiOperation("Get a user by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Returns a user specified by id"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @GET
    @Path("/{id}")
    public UserDto getUser(@PathParam("id") Long id) {
        if (id == null) {
            throw new WebApplicationException("Invalid id", 400);
        }
        return UserConverter.transform(userEJB.getUser(id));
    }


    @ApiOperation("Create a user")
    @ApiResponse(code = 201, message = "Created new user")
    @POST
    public Response createUser(UserDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for newly created User", 400);
        }

        if (Strings.isNullOrEmpty(dto.name) || Strings.isNullOrEmpty(dto.surname) || Strings.isNullOrEmpty(dto.address)) {
            throw new WebApplicationException("Missing attributes: Name, surname or address", 400);
        }

        Long id;
        try {
            id = userEJB.createUser(dto.name, dto.surname, dto.address);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), 500);
        }

        return Response.status(201)
                .location(URI.create(ApplicationConfig.BASE_PATH + "/users/" + id))
                .build();
    }


    @ApiOperation("Update an existing user")
    @ApiResponse(code = 200, message = "Updated user")
    @PUT
    public Response updateUser(UserDto dto) {
        if (dto.id == null) {
            throw new WebApplicationException("Must provide an id for user to update", 400);
        }

        if (Strings.isNullOrEmpty(dto.name) || Strings.isNullOrEmpty(dto.surname) || Strings.isNullOrEmpty(dto.address)) {
            throw new WebApplicationException("Missing attributes: Name, surname or address", 400);
        }

        try {
            userEJB.updateUser(dto.id, dto.name, dto.surname, dto.address);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), 500);
        }

        return Response.status(200)
                .build();
    }
}
