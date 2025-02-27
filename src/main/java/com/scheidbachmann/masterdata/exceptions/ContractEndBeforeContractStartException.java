package com.scheidbachmann.masterdata.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)

public class ContractEndBeforeContractStartException extends RuntimeException{
  protected final String message;

  public ContractEndBeforeContractStartException(String message) {
    this.message = message;
  }
}
