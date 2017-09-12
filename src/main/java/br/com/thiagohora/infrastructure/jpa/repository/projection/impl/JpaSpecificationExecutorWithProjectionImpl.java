package br.com.thiagohora.infrastructure.jpa.repository.projection.impl;

import br.com.thiagohora.infrastructure.jpa.repository.projection.JpaSpecificationExecutorWithProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.util.StringUtils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaSpecificationExecutorWithProjectionImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements JpaSpecificationExecutorWithProjection<T> {

    private final EntityManager entityManager;
    private final ProjectionFactory projectionFactory;

    public JpaSpecificationExecutorWithProjectionImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.projectionFactory = new SpelAwareProxyProjectionFactory();
    }


    private void buildEntityGraph(EntityGraph<T> entityGraph, String[] attributeGraph) {
        List<String> attributePaths = Arrays.asList(attributeGraph);

        // Sort to ensure that the intermediate entity subgraphs are created accordingly.
        Collections.sort(attributePaths);
        Collections.reverse(attributePaths);

        // We build the entity graph based on the paths with highest depth first
        for (String path : attributePaths) {

            // Fast path - just single attribute
            if (!path.contains(".")) {
                entityGraph.addAttributeNodes(path);
                continue;
            }

            // We need to build nested sub fetch graphs
            String[] pathComponents = StringUtils.delimitedListToStringArray(path, ".");
            Subgraph<?> parent = null;

            for (int c = 0; c < pathComponents.length - 1; c++) {
                parent = c == 0 ? entityGraph.addSubgraph(pathComponents[c]) : parent.addSubgraph(pathComponents[c]);
            }

            parent.addAttributeNodes(pathComponents[pathComponents.length - 1]);
        }
    }

    private void addEntityGraph(String entityGraphName, final EntityGraphType graphType, final TypedQuery<T> query) {
        Optional.ofNullable(entityGraphName)
                .filter(name -> graphType != null)
                .ifPresent(name -> query.setHint(graphType.getKey(), this.entityManager.createEntityGraph(name)));
    }

    @Override
    public <R> List<R> findAll(Specifications<T> specification, Class<R> projectionClass, String entityGraphName, EntityGraphType graphType) {
        final TypedQuery<T> query = this.getQuery(specification, this.getDomainClass(), (Sort) null);

        addEntityGraph(entityGraphName, graphType, query);

        return query.getResultList().stream()
                .map(row -> projectionFactory.createProjection(projectionClass, row))
                .collect(Collectors.toList());
    }

    public <R> Page<R> findAll(Specifications<T> specification, Class<R> projectionClass, Pageable pageable, String entityGraphName, EntityGraphType graphType) {
        final TypedQuery<T> query = this.getQuery(specification, this.getDomainClass(), (Sort) null);

        addEntityGraph(entityGraphName, graphType, query);

        Page<T> page = (pageable == null) ?
                new PageImpl<>(query.getResultList()) : this.readPage(query, getDomainClass(), pageable, specification);

        return page.map(row -> projectionFactory.createProjection(projectionClass, row));
    }
}
