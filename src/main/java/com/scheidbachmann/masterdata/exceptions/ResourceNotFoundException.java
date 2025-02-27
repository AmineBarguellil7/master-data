/**
 * Created By Amine Barguellil
 * Date : 2/15/2024
 * Time : 3:05 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    protected final String message;
    protected final String id;

    protected ResourceNotFoundException(final String id) {
        this.id = requireNonNull(id, "The id must not be null!");
        this.message = String.format("The resource with ID '%s' can not be found.", id);
    }

    public static ResourceNotFoundException with(final String id) {
        return new ResourceNotFoundException(id);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

}
