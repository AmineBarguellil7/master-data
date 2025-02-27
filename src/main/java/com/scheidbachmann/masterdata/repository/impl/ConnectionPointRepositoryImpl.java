package com.scheidbachmann.masterdata.repository.impl;

import com.scheidbachmann.masterdata.entity.ConnectionPoint;
import com.scheidbachmann.masterdata.enums.ConnectionPointTypeEnum;
import com.scheidbachmann.masterdata.enums.ServiceConsumerRoleEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.scheidbachmann.masterdata.utils.Constants.*;

/**
 * @author KaouechHaythem
 */
@Service
public class ConnectionPointRepositoryImpl {

  private final EntityManager entityManager;

  public ConnectionPointRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public Page<ConnectionPoint> search(Map<String, Object> filters, Pageable page) {
    StringBuilder jpql = getJpqlQuery(filters, false);
    Sort sort = page.getSort();
    if (sort != null && sort.isSorted()) {
      String orderBy = " ORDER BY";
      for (Sort.Order order : sort) {
        orderBy += " cp." + order.getProperty() + " " + order.getDirection();
      }
      jpql.append(orderBy);
    }
    StringBuilder jpqlCount = getJpqlQuery(filters, true);
    Query query = entityManager.createQuery(jpql.toString(), ConnectionPoint.class);
    Query queryCount = entityManager.createQuery(jpqlCount.toString());
    addParams(query, queryCount, filters);
    query.setMaxResults(page.getPageSize());
    query.setFirstResult(page.getPageSize() * page.getPageNumber());
    Long count = (Long) queryCount.getSingleResult();
    List<ConnectionPoint> connectionPoints = query.getResultList();
    return new PageImpl<>(connectionPoints, page, count);
  }

  private StringBuilder getJpqlQuery(Map<String, Object> map, boolean forCount) {
    StringBuilder jpql = new StringBuilder();
    if (forCount) {
      jpql.append(" select  count(cp)");
    } else {
      jpql.append(" select  cp");
    }
    jpql.append(" from ConnectionPoint cp");
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_SERVICE_ID)) || StringUtils.isNotEmpty((String) map.get(PARAMETER_CP_ROLE))) {
      jpql.append(" JOIN cp.connectionPointServices cps");
    }
    jpql.append(" where cp.deletedAt IS NULL");
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_TENANT_NAME))) {
      jpql.append(" and lower(cp.tenantName) like :tenantName");
    }
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_GEOMETRY_PATH))) {
      jpql.append(" and lower(cp.geometryPath) like :geometryPath");
    }
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_BP_ID))) {
      jpql.append(" and cp.businessPartnerId = :businessPartnerId");
    }
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_CP_TYPE))) {
      jpql.append(" and cp.type = :type");
    }
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_NAME))) {
      jpql.append(" and lower(cp.name) like :name");
    }
    if ((List<String>) map.get(PARAMETER_LOCATION_IDS) != null) {
      jpql.append(" and cp.locationId in :locationIds");
    }
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_SERVICE_ID))) {
      jpql.append(" and cps.serviceId = :serviceId ");
    }
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_CP_ROLE))) {
      jpql.append(" and cps.endpointRole = :endpointRole");
    }
    if ((List<String>) map.get(PARAMETER_IDS) != null) {
      jpql.append(" and cp.id in :ids");
    }
    return jpql;
  }

  private void addParams(Query query, Query queryCount, Map<String, Object> filters) {

    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_TENANT_NAME))) {
      String tenantName = (String) filters.get(PARAMETER_TENANT_NAME);
      final String tenantNameFilter = "%" + tenantName.toLowerCase() + "%";
      query.setParameter(PARAMETER_TENANT_NAME, tenantNameFilter);
      queryCount.setParameter(PARAMETER_TENANT_NAME, tenantNameFilter);
    }
    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_GEOMETRY_PATH))) {
      String gp = (String) filters.get(PARAMETER_GEOMETRY_PATH);
      final String gpFilter = "%" + gp.toLowerCase() + "%";
      query.setParameter(PARAMETER_GEOMETRY_PATH, gpFilter);
      queryCount.setParameter(PARAMETER_GEOMETRY_PATH, gpFilter);
    }
    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_BP_ID))) {
      query.setParameter(PARAMETER_BP_ID, (String) filters.get(PARAMETER_BP_ID));
      queryCount.setParameter(PARAMETER_BP_ID, (String) filters.get(PARAMETER_BP_ID));
    }
    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_CP_TYPE))) {
      query.setParameter(PARAMETER_CP_TYPE, (ConnectionPointTypeEnum) ConnectionPointTypeEnum.valueOf((String) filters.get(PARAMETER_CP_TYPE)) );
      queryCount.setParameter(PARAMETER_CP_TYPE, (ConnectionPointTypeEnum) ConnectionPointTypeEnum.valueOf((String) filters.get(PARAMETER_CP_TYPE)) );
    }
    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_NAME))) {
      String name = (String) filters.get(PARAMETER_NAME);
      final String nameFilter = "%" + name.toLowerCase() + "%";
      query.setParameter(PARAMETER_NAME, nameFilter);
      queryCount.setParameter(PARAMETER_NAME, nameFilter);
    }

    if ((List<String>) filters.get(PARAMETER_LOCATION_IDS) != null) {
      query.setParameter(PARAMETER_LOCATION_IDS, (List<String>) filters.get(PARAMETER_LOCATION_IDS));
      queryCount.setParameter(PARAMETER_LOCATION_IDS, (List<String>) filters.get(PARAMETER_LOCATION_IDS));
    }

    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_SERVICE_ID))) {
      query.setParameter(PARAMETER_SERVICE_ID, (String) filters.get(PARAMETER_SERVICE_ID));
      queryCount.setParameter(PARAMETER_SERVICE_ID, (String) filters.get(PARAMETER_SERVICE_ID));
    }

    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_CP_ROLE))) {
      query.setParameter(PARAMETER_CP_ROLE, ServiceConsumerRoleEnum.valueOf((String) filters.get(PARAMETER_CP_ROLE)));
      queryCount.setParameter(PARAMETER_CP_ROLE, ServiceConsumerRoleEnum.valueOf((String) filters.get(PARAMETER_CP_ROLE)));
    }

    if ((List<String>) filters.get(PARAMETER_IDS) != null) {
      query.setParameter(PARAMETER_IDS, (List<String>) filters.get(PARAMETER_IDS));
      queryCount.setParameter(PARAMETER_IDS, (List<String>) filters.get(PARAMETER_IDS));
    }
  }
}
