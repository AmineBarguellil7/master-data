/**
 * Created By Amine Barguellil
 * Date : 2/15/2024
 * Time : 3:07 PM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BusinessPartnerContactAlreadyExistsException extends RuntimeException {

    protected final String message;
    protected final String id;

    protected BusinessPartnerContactAlreadyExistsException(final String id) {
        this.id = requireNonNull(id, "The id must not be null!");
        this.message = String.format("The business partner with ID '%s' already has a contact person. "
                + "Use PUT to update the existing contact person.", id);
    }

    public static BusinessPartnerContactAlreadyExistsException with(final String id) {
        return new BusinessPartnerContactAlreadyExistsException(id);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

}
