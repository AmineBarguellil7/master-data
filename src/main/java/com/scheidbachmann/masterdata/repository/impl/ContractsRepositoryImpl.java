package com.scheidbachmann.masterdata.repository.impl;

import com.scheidbachmann.masterdata.entity.Contract;
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
public class ContractsRepositoryImpl {
  private final EntityManager entityManager;

  public ContractsRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }


  public Page<Contract> search(Map<String, Object> filters, Pageable page) {
    StringBuilder jpql = getJpqlQuery(filters, false);
    Sort sort = page.getSort();
    if (sort != null && sort.isSorted()) {
      String orderBy = " ORDER BY";
      for (Sort.Order order : sort) {
        orderBy += " c." + order.getProperty() + " " + order.getDirection();
      }
      jpql.append(orderBy);
    }
    StringBuilder jpqlCount = getJpqlQuery(filters, true);
    Query query = entityManager.createQuery(jpql.toString(), Contract.class);
    Query queryCount = entityManager.createQuery(jpqlCount.toString());
    addParams(query, queryCount, filters);
    query.setMaxResults(page.getPageSize());
    query.setFirstResult(page.getPageSize() * page.getPageNumber());
    Long count = (Long) queryCount.getSingleResult();
    List<Contract> contracts = query.getResultList();
    return new PageImpl<>(contracts, page, count);
  }

  private StringBuilder getJpqlQuery(Map<String, Object> map, boolean forCount) {
    StringBuilder jpql = new StringBuilder();
    if (forCount) {
      jpql.append(" select  count(c)");
    } else {
      jpql.append(" select  c");
    }
    jpql.append(" from Contract c");
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_TENANT))) {
      jpql.append(" JOIN c.supplier supp");
    }
    jpql.append(" where c.deletedAt IS NULL");
//    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_SUPPLIER_ID))) {
//      jpql.append(" and c.supplierId = :supplierId");
//    }
//    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_CONSUMER_ID))) {
//      jpql.append(" and c.consumerId = :consumerId");
//    }
//    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_SUPPLIER_NAME)) || StringUtils.isNotEmpty((String) map.get(PARAMETER_CONSUMER_NAME))) {
//      jpql.append(" and (c.supplier.name = :supplierName or c.consumer.name = :consumerName)");
//    }
        if (StringUtils.isNotEmpty((String) map.get(PARAMETER_SUPPLIER_ID)) || StringUtils.isNotEmpty((String) map.get(PARAMETER_CONSUMER_ID))) {
      jpql.append(" and (c.supplierId = :supplierId or c.consumerId = :consumerId)");
    }
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_SERVICE_ID))) {
      jpql.append(" and c.serviceId = :serviceId");
    }
    if (StringUtils.isNotEmpty((String) map.get(PARAMETER_TENANT))) {
      jpql.append(" and lower(supp.tenantId) like :tenant");
    }


    return jpql;
  }

  private void addParams(Query query, Query queryCount, Map<String, Object> filters) {
    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_SUPPLIER_ID))) {
      query.setParameter(PARAMETER_SUPPLIER_ID, (String) filters.get(PARAMETER_SUPPLIER_ID));
      queryCount.setParameter(PARAMETER_SUPPLIER_ID, (String) filters.get(PARAMETER_SUPPLIER_ID));
    }
    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_CONSUMER_ID))) {
      query.setParameter(PARAMETER_CONSUMER_ID, (String) filters.get(PARAMETER_CONSUMER_ID));
      queryCount.setParameter(PARAMETER_CONSUMER_ID, (String) filters.get(PARAMETER_CONSUMER_ID));
    }
//    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_SUPPLIER_NAME))) {
//      query.setParameter(PARAMETER_SUPPLIER_NAME, (String) filters.get(PARAMETER_SUPPLIER_NAME));
//      queryCount.setParameter(PARAMETER_SUPPLIER_NAME, (String) filters.get(PARAMETER_SUPPLIER_NAME));
//    }
//    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_CONSUMER_NAME))) {
//      query.setParameter(PARAMETER_CONSUMER_NAME, (String) filters.get(PARAMETER_CONSUMER_NAME));
//      queryCount.setParameter(PARAMETER_CONSUMER_NAME, (String) filters.get(PARAMETER_CONSUMER_NAME));
//    }
    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_SERVICE_ID))) {
      query.setParameter(PARAMETER_SERVICE_ID, (String) filters.get(PARAMETER_SERVICE_ID));
      queryCount.setParameter(PARAMETER_SERVICE_ID, (String) filters.get(PARAMETER_SERVICE_ID));
    }
    if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_TENANT))) {
      String tenantName = (String) filters.get(PARAMETER_TENANT);
      final String tenantNameFilter = "%" + tenantName.toLowerCase() + "%";
      query.setParameter(PARAMETER_TENANT, tenantNameFilter);
      queryCount.setParameter(PARAMETER_TENANT, tenantNameFilter);
    }
  }
}
