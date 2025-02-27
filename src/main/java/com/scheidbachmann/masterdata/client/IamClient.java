package com.scheidbachmann.masterdata.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.scheidbachmann.masterdata.utils.Constants.IAM_USER_ADD_PARAM_KEY;

/**
 * @author KaouechHaythem
 */
@FeignClient(name = "${services.iam.name}", url = "${services.iam.url}", configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface IamClient {
  @PutMapping(value = "/v1/{tenantName}/users", consumes = {"application/json"})
  ResponseEntity<String> addUser(@PathVariable("tenantName") String tenantName, @RequestParam(IAM_USER_ADD_PARAM_KEY) boolean iamUserAddParam,
                                 @RequestBody String body);

  @GetMapping(value = "/v1/{tenantName}/users/exists", consumes = {"application/json"})
  boolean checkUser(@PathVariable("tenantName") String tenantName, @RequestParam("account") String userId);

  @DeleteMapping (value = "/v1/{tenantName}/users", consumes = {"application/json"})
  boolean deleteUser(@PathVariable("tenantName") String tenantName, @RequestParam("account") String userId);
}
