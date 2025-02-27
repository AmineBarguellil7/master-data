package com.scheidbachmann.masterdata.repository.impl;

import com.scheidbachmann.masterdata.entity.BusinessPartner;
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
public class BusinessPartnersRepositoryImpl {

    private final EntityManager entityManager;

    public BusinessPartnersRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

  public  Page<BusinessPartner> search(Map<String, Object> filters, Pageable page){
        StringBuilder jpql = getJpqlQuery(filters,false);
        Sort sort = page.getSort();
        if (sort != null && sort.isSorted()) {
            String orderBy = " ORDER BY";
            for (Sort.Order order : sort) {
                orderBy += " b." + order.getProperty() + " " + order.getDirection();
            }
            jpql.append(orderBy);
        }
        StringBuilder jpqlCount = getJpqlQuery(filters,true);
        Query query = entityManager.createQuery(jpql.toString(), BusinessPartner.class);
        Query queryCount = entityManager.createQuery(jpqlCount.toString());
        addParams(query, queryCount,filters);
        query.setMaxResults(page.getPageSize());
        query.setFirstResult(page.getPageSize() * page.getPageNumber());
        Long count = (Long) queryCount.getSingleResult();
        List<BusinessPartner> businessPartners = query.getResultList();
        return new PageImpl<>(businessPartners,page,count);
    }
    private StringBuilder getJpqlQuery(Map<String, Object> map ,boolean forCount) {
        StringBuilder jpql = new StringBuilder();
        if(forCount){
            jpql.append(" select  count(b)");
        }else {
            jpql.append(" select  b");
        }
        jpql.append(" from BusinessPartner b");
        jpql.append(" where b.deletedAt IS NULL");
        if (StringUtils.isNotEmpty((String) map.get(PARAMETER_TENANT))) {
            jpql.append(" and lower(b.tenantId) like :tenant");
        }
        if (map.get(PARAMETER_PROVIDER_ID) != null) {
            jpql.append(" and b.providerId = :providerId");
        }
        if (StringUtils.isNotEmpty((String) map.get(PARAMETER_FILTER_TERM))) {
            jpql.append(" and lower(b.name) like :filterTerm or lower(b.partnerNumber) like :filterTerm");
        }
      if (map.get(PARAMETER_ID) != null) {
        jpql.append(" and b.id = :id");
      }
        if ((List<String>)map.get(PARAMETER_IDS)!=null) {
            jpql.append(" and b.id in :ids");
        }
        return jpql;
    }
    private void addParams(Query query,Query queryCount, Map<String, Object> filters) {
        if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_ID))) {
          query.setParameter(PARAMETER_ID,(String) filters.get(PARAMETER_ID));
          queryCount.setParameter(PARAMETER_ID,(String) filters.get(PARAMETER_ID));
        }
      if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_TENANT))) {
        String filter = (String) filters.get(PARAMETER_TENANT);
        final String filterTerm = "%" + filter.toLowerCase() + "%";
        query.setParameter(PARAMETER_TENANT,filterTerm);
        queryCount.setParameter(PARAMETER_TENANT,filterTerm);
      }
        if (StringUtils.isNotEmpty((String) filters.get(PARAMETER_FILTER_TERM))) {
            String filter = (String) filters.get(PARAMETER_FILTER_TERM);
            final String filterTerm = "%" + filter.toLowerCase() + "%";
            query.setParameter(PARAMETER_FILTER_TERM,filterTerm);
            queryCount.setParameter(PARAMETER_FILTER_TERM,filterTerm);
        }
        if (filters.get(PARAMETER_PROVIDER_ID) != null) {
            query.setParameter(PARAMETER_PROVIDER_ID, (Integer) filters.get(PARAMETER_PROVIDER_ID));
            queryCount.setParameter(PARAMETER_PROVIDER_ID, (Integer) filters.get(PARAMETER_PROVIDER_ID));
        }
        if ((List<String>)filters.get(PARAMETER_IDS)!=null) {
            query.setParameter(PARAMETER_IDS,(List<String>)filters.get(PARAMETER_IDS));
            queryCount.setParameter(PARAMETER_IDS,(List<String>)filters.get(PARAMETER_IDS));
        }
    }
}
