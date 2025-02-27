package com.scheidbachmann.masterdata.service.impl;

import com.scheidbachmann.masterdata.client.IamClient;
import com.scheidbachmann.masterdata.service.IamService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;

import static com.scheidbachmann.masterdata.utils.Constants.*;

/**
 * @author KaouechHaythem
 */
@Service
@Slf4j
public class IamServiceImpl implements IamService {



  @Value("${services.iam.payload-path}")
  private String iamUserPayloadFile;
  private final IamClient iamClient;
    public IamServiceImpl(IamClient iamClient) {
        this.iamClient = iamClient;
    }


    @Override
  public void createUser(String tenantName, String connectionPointId, String inboundUsername, String inboundPassword) throws Exception {
    if (!iamClient.checkUser(tenantName, inboundUsername)) {
      ResponseEntity<String> response = this.iamClient.addUser(tenantName, true, buildBody(inboundUsername, connectionPointId, inboundPassword));
      if (!HttpStatus.OK.equals(response.getStatusCode())) {
        log.warn("Error during IAM call {} ", response.getStatusCode());
      }
    }
  }
  @Override
  public void deleteUser(String tenantName, String inboundUsername) {
    if (StringUtils.isNotEmpty(tenantName) && StringUtils.isNotEmpty(inboundUsername)) {
      try {
        iamClient.deleteUser(tenantName, inboundUsername);
      } catch (Exception ex) {
        log.warn("Unable to delete user from IAM service {}", ex.getMessage());
      }

    }
  }
  @Override
  public String readFileFromResources() {
    try {
      ClassPathResource classPathResource = new ClassPathResource(iamUserPayloadFile);
      byte[] data = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
      return new String(data, StandardCharsets.UTF_8);
    } catch (Exception ex) {
      log.error("exception parsing resources when saving connection point {}", ex);
      return null;
    }
  }
  @Override
  public String buildBody(String userId, String connectionPointId, String userPassword) throws Exception {
    return readFileFromResources().replace(USER_ACCOUNT_NAME_KEY, userId)
      .replace(CONNECTION_POINT_ID_VALUE_KEY, connectionPointId)
      .replace(USER_PASSWORD_VALUE_KEY, userPassword)
      .replace(KC_ROLE_INBOUND_SERVICE_KEY, KC_ROLE_INBOUND_SERVICE_DEFAULT);
  }
}
