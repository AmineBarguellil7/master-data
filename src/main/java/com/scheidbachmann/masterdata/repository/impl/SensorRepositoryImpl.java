/**
 * Created By Amine Barguellil
 * Date : 3/1/2024
 * Time : 10:12 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.repository.impl;


import com.scheidbachmann.masterdata.entity.Sensor;
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

@Service
public class SensorRepositoryImpl {
  private final EntityManager entityManager;

  public SensorRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public Page<Sensor> search(Map<String, Object> filters, Pageable page) {
    StringBuilder jpql = getJpqlQuery(filters, false);
    Sort sort = page.getSort();
    if (sort != null && sort.isSorted()) {
      String orderBy = " ORDER BY";
      for (Sort.Order order : sort) {
        orderBy += " s." + order.getProperty() + " " + order.getDirection();
      }
      jpql.append(orderBy);
    }
    StringBuilder jpqlCount = getJpqlQuery(filters, true);
    Query query = entityManager.createQuery(jpql.toString(), Sensor.class);
    Query queryCount = entityManager.createQuery(jpqlCount.toString());
    addParams(query, queryCount, filters);
    query.setMaxResults(page.getPageSize());
    query.setFirstResult(page.getPageSize() * page.getPageNumber());
    Long count = (Long) queryCount.getSingleResult();
    List<Sensor> sensors = query.getResultList();
    return new PageImpl<>(sensors, page, count);
  }

  private StringBuilder getJpqlQuery(Map<String, Object> map, boolean forCount) {
    StringBuilder jpql = new StringBuilder();
    if (forCount) {
      jpql.append(" select  count(s)");
    } else {
      jpql.append(" select  s");
    }
    jpql.append(" from Sensor s");
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_BP_ID))) {
      jpql.append(" JOIN s.connectionPoint cp");
    }
    jpql.append(" where s.deletedAt IS NULL");
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_TENANT_NAME))) {
      jpql.append(" and lower(s.tenantName) like :tenantName");
    }
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_CONNECTIONP_ID))) {
      jpql.append(" and s.connectionPointId = :connectionPointId");
    }
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_SERIAL_NUMBER))) {
      jpql.append(" and s.serialNumber = :serialNumber");
    }
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_BP_ID))) {
      jpql.append(" and cp.businessPartnerId = :businessPartnerId");
    }
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_LOCATION_ID))) {
      jpql.append(" and lower(s.locationId) like :locationId");
    }

    if ((List<String>) map.get(PARAMETER_IDS) != null) {
      jpql.append(" and s.id in :ids");
    }

    return jpql;
  }

  private void addParams(Query query, Query queryCount, Map<String, Object> filters) {

    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_TENANT_NAME))) {
      String filter = (String) filters.get(PARAMETER_TENANT_NAME);
      final String filterTerm = "%" + filter.toLowerCase() + "%";
      query.setParameter(PARAMETER_TENANT_NAME, filterTerm);
      queryCount.setParameter(PARAMETER_TENANT_NAME, filterTerm);
    }
    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_CONNECTIONP_ID))) {
      query.setParameter(PARAMETER_CONNECTIONP_ID, (String) filters.get(PARAMETER_CONNECTIONP_ID));
      queryCount.setParameter(PARAMETER_CONNECTIONP_ID, (String) filters.get(PARAMETER_CONNECTIONP_ID));
    }
    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_SERIAL_NUMBER))) {
      query.setParameter(PARAMETER_SERIAL_NUMBER, (String) filters.get(PARAMETER_SERIAL_NUMBER));
      queryCount.setParameter(PARAMETER_SERIAL_NUMBER, (String) filters.get(PARAMETER_SERIAL_NUMBER));
    }
    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_BP_ID))) {
      query.setParameter(PARAMETER_BP_ID, (String) filters.get(PARAMETER_BP_ID));
      queryCount.setParameter(PARAMETER_BP_ID, (String) filters.get(PARAMETER_BP_ID));
    }
    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_LOCATION_ID))) {
      query.setParameter(PARAMETER_LOCATION_ID, (String) filters.get(PARAMETER_LOCATION_ID));
      queryCount.setParameter(PARAMETER_LOCATION_ID, (String) filters.get(PARAMETER_LOCATION_ID));
    }

    if ((List<String>) filters.get(PARAMETER_IDS) != null) {
      query.setParameter(PARAMETER_IDS, (List<String>) filters.get(PARAMETER_IDS));
      queryCount.setParameter(PARAMETER_IDS, (List<String>) filters.get(PARAMETER_IDS));
    }
  }
}
