package br.com.thiagohora.infrastructure.jpa.repository.projection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface JpaSpecificationExecutorWithProjection<T> {
    <R> List<R> findAll(Specifications<T> specification, Class<R> projectionClass, String entityGraphName, EntityGraphType graphType);
    <R> Page<R> findAll(Specifications<T> specification, Class<R> projectionClass, Pageable pageable, String entityGraphName, EntityGraphType graphType);
}
