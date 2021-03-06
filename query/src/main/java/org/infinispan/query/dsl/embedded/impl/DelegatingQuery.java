package org.infinispan.query.dsl.embedded.impl;

import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.dsl.impl.BaseQuery;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author anistor@redhat.com
 * @since 8.0
 */
final class DelegatingQuery extends BaseQuery {

   private final QueryEngine queryEngine;

   /**
    * The actual query object to which execution will be delegated.
    */
   private BaseQuery query;

   DelegatingQuery(QueryEngine queryEngine, QueryFactory queryFactory,
                   String jpaQuery, Map<String, Object> namedParameters, String[] projection,
                   long startOffset, int maxResults) {
      super(queryFactory, jpaQuery, namedParameters, projection, startOffset, maxResults);
      this.queryEngine = queryEngine;
   }

   @Override
   public void resetQuery() {
      if (query != null) {
         // reset the delegate but do not discard it!
         query.resetQuery();
      }
   }

   private Query createQuery() {
      // the query is created first time only
      if (query == null) {
         query = queryEngine.buildQuery(queryFactory, jpaQuery, namedParameters, startOffset, maxResults);
      }
      return query;
   }

   @Override
   public <T> List<T> list() {
      return createQuery().list();
   }

   @Override
   public int getResultSize() {
      return createQuery().getResultSize();
   }

   @Override
   public String toString() {
      return "DelegatingQuery{" +
            "jpaQuery=" + jpaQuery +
            ", namedParameters=" + namedParameters +
            ", projection=" + Arrays.toString(projection) +
            ", startOffset=" + startOffset +
            ", maxResults=" + maxResults +
            '}';
   }
}
