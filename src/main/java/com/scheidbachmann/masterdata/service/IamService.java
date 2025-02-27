package com.scheidbachmann.masterdata.service;

/**
 * @author KaouechHaythem
 */
public interface IamService {
  void createUser(String tenantName, String connectionPointId, String inboundUsername, String inboundPassword) throws Exception;

  void deleteUser(String tenantName, String inboundUsername);

  String readFileFromResources();

  String buildBody(String userId, String connectionPointId, String userPassword) throws Exception;
}
