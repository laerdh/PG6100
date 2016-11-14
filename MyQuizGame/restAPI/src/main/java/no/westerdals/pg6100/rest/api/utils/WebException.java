package no.westerdals.pg6100.rest.api.utils;

import com.google.common.base.Throwables;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;

public class WebException {

    public static WebApplicationException wrapException(Exception e) throws WebApplicationException {
        Throwable cause = Throwables.getRootCause(e);

        if (cause instanceof ConstraintViolationException) {
            return new WebApplicationException("Invalid constraints on input: " + cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}
