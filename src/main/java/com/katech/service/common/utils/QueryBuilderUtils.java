package com.katech.service.common.utils;

import static org.jooq.impl.DSL.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class QueryBuilderUtils<T> {
    private final DSLContext dsl;
    private final String baseSql;
    private final Class<T> entityClass;
    private final List<Condition> conditions = new ArrayList<>();

    // ====================== HELPER METHODS ====================== //

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private <V> QueryBuilderUtils<T> addCondition(Field<V> field, V value, Condition condition) {
        Optional.ofNullable(value).ifPresent(v -> conditions.add(condition));
        return this;
    }

    // ====================== LOGIC OPERATORS ====================== //

    public QueryBuilderUtils<T> addAndCondition(Condition... conditions) {
        if (conditions != null && conditions.length > 0) {
            this.conditions.add(DSL.and(conditions));
        }
        return this;
    }

    public QueryBuilderUtils<T> addOrCondition(Condition... conditions) {
        if (conditions != null && conditions.length > 0) {
            this.conditions.add(DSL.or(conditions));
        }
        return this;
    }

    // ====================== ARRAY CONDITIONS ====================== //
    public QueryBuilderUtils<T> addArrayCondition(
            Field<String[]> field, List<String> values, boolean condition) {
        if (values != null && !values.isEmpty()) {
            var filterValues =
                    values.stream()
                            .filter(Objects::nonNull)
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toArray(String[]::new);

            if (filterValues.length > 0) {
                String sqlCondition = condition ? "{0} && {1}" : "not {0} && {1}";
                addCondition(
                        field,
                        filterValues,
                        DSL.condition(sqlCondition, field, DSL.array(filterValues)));
            }
        }
        return this;
    }

    // ====================== STRING CONDITIONS ====================== //

    public QueryBuilderUtils<T> addLikeCondition(Field<String> field, String value) {
        return isNotEmpty(value) ? addCondition(field, value, field.like("%" + value + "%")) : this;
    }

    public QueryBuilderUtils<T> addEqualCondition(Field<String> field, String value) {
        return isNotEmpty(value) ? addCondition(field, value, field.eq(value)) : this;
    }

    public QueryBuilderUtils<T> addNotEqualCondition(Field<String> field, String value) {
        return isNotEmpty(value) ? addCondition(field, value, field.notEqual(value)) : this;
    }

    // ====================== IN CONDITIONS ====================== //

    public <V> QueryBuilderUtils<T> addInCondition(Field<V> field, List<V> values) {
        if (values != null && !values.isEmpty()) {
            conditions.add(field.in(values));
        }
        return this;
    }

    // ====================== GROUP NOT CONDITIONS ====================== //

    /*
     * nhóm dữ liệu theo điều kiện
     * vd: NOT (a = b AND c != d)
     */
    public <V> void addGroupNotCondition(Map<Field<V>, V> values, Set<Field<?>> fieldsToNotEqual) {
        if (values != null && !values.isEmpty()) {
            List<Condition> groupConditions = new ArrayList<>();

            for (Map.Entry<Field<V>, V> entry : values.entrySet()) {
                boolean isNotEqual =
                        fieldsToNotEqual != null && fieldsToNotEqual.contains(entry.getKey());
                groupConditions.add(
                        isNotEqual
                                ? entry.getKey().ne(entry.getValue()) // truyền '!=' vào trường dữ
                                // liệu muốn truyền (vd: a !=
                                // b)
                                : entry.getKey().eq(entry.getValue())); // truyền '=' vào trường dữ
                // liệu muốn truyền (vd: a =
                // b)
            }

            conditions.add(DSL.not(DSL.and(groupConditions)));
        }
    }

    // ====================== FULL TEXT SEARCH (FTS) ====================== //

    public QueryBuilderUtils<T> addMysqlFullTextSearchCondition(
            Field<String> field, String keyword) {
        if (isNotEmpty(keyword)) {
            Condition matchAgainst =
                    DSL.condition(
                            "MATCH({0}) AGAINST ({1} IN NATURAL LANGUAGE MODE)",
                            field, DSL.val(keyword));
            Condition likeFallback = field.like("%" + keyword + "%");
            conditions.add(matchAgainst.or(likeFallback));
        }
        return this;
    }

    public QueryBuilderUtils<T> addMysqlFullTextSearchCondition(
            Collection<Field<String>> fields, String keyword) {
        if (isNotEmpty(keyword) && fields != null && !fields.isEmpty()) {
            // Ghép các field thành một danh sách ngăn cách bằng dấu phẩy
            String fieldList =
                    fields.stream().map(Field::getName).collect(Collectors.joining(", "));

            // MATCH(field1, field2, ...) AGAINST ('keyword' IN NATURAL LANGUAGE MODE)
            Condition matchAgainst =
                    DSL.condition(
                            "MATCH(" + fieldList + ") AGAINST ({0} IN NATURAL LANGUAGE MODE)",
                            DSL.val(keyword));

            // Fallback bằng LIKE cho từng field
            Condition likeFallback =
                    fields.stream()
                            .map(f -> DSL.condition("{0} LIKE {1}", f, "%" + keyword + "%"))
                            .reduce(Condition::or)
                            .orElse(DSL.falseCondition());

            conditions.add(matchAgainst.or(likeFallback));
        }
        return this;
    }

    public QueryBuilderUtils<T> addFullTextSearchCondition(
            Field<?> tsvectorField, Field<?> tsvectorUnaccentField, String searchText) {
        if (isNotEmpty(searchText)) {
            String formattedSearch = buildFullTextSearchQuery(searchText);
            List<Condition> searchConditions = new ArrayList<>();

            if (tsvectorField != null) {
                searchConditions.add(
                        condition(
                                "{0} @@ to_tsquery('simple', {1})",
                                tsvectorField, val(formattedSearch)));
            }
            if (tsvectorUnaccentField != null) {
                searchConditions.add(
                        condition(
                                "{0} @@ to_tsquery('simple', {1})",
                                tsvectorUnaccentField, val(formattedSearch)));
            }

            if (!searchConditions.isEmpty()) {
                conditions.add(or(searchConditions));
            }
        }
        return this;
    }

    private String buildFullTextSearchQuery(String search) {
        String[] searchTerms = search.trim().split("\\s+");
        StringBuilder formattedSearch = new StringBuilder();

        for (String term : searchTerms) {
            if (!formattedSearch.isEmpty()) {
                formattedSearch.append(" & ");
            }
            formattedSearch.append(term).append(":*");
        }

        return formattedSearch.toString();
    }

    // ====================== NUMBER CONDITIONS ====================== //

    public <N extends Number> QueryBuilderUtils<T> addEqualCondition(Field<N> field, N value) {
        return addCondition(field, value, field.eq(value));
    }

    public <N extends Number> QueryBuilderUtils<T> addGreaterThanOrEqualCondition(
            Field<N> field, N value) {
        return addCondition(field, value, field.ge(value));
    }

    public <N extends Number> QueryBuilderUtils<T> addLessThanOrEqualCondition(
            Field<N> field, N value) {
        return addCondition(field, value, field.le(value));
    }

    public <N extends Number> QueryBuilderUtils<T> addBetweenCondition(
            Field<N> field, N start, N end) {
        if (start != null && end != null) {
            conditions.add(field.between(start, end));
        } else if (start != null) {
            conditions.add(field.ge(start));
        } else if (end != null) {
            conditions.add(field.le(end));
        }
        return this;
    }

    // ====================== DATE CONDITIONS ====================== //

    public QueryBuilderUtils<T> addGreaterThanOrEqualCondition(
            Field<LocalDateTime> field, LocalDateTime value) {
        return addCondition(field, value, field.ge(value));
    }

    public QueryBuilderUtils<T> addLessThanOrEqualCondition(
            Field<LocalDateTime> field, LocalDateTime value) {
        return addCondition(field, value, field.le(value));
    }

    public QueryBuilderUtils<T> addBetweenCondition(
            Field<LocalDateTime> createdAtField, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate != null && toDate != null) {
            conditions.add(createdAtField.between(fromDate, toDate));
        } else if (fromDate != null) {
            conditions.add(createdAtField.ge(fromDate));
        } else if (toDate != null) {
            conditions.add(createdAtField.le(toDate));
        }
        return this;
    }

    // ====================== BOOLEAN CONDITION ====================== //

    public QueryBuilderUtils<T> addEqualCondition(Field<Boolean> field, Boolean value) {
        return addCondition(field, value, field.eq(value));
    }

    // ====================== UUID CONDITION ====================== //

    public QueryBuilderUtils<T> addEqualCondition(Field<UUID> field, UUID value) {
        return addCondition(field, value, field.eq(value));
    }

    // ====================== ENUM CONDITION ====================== //

    public <E extends Enum<E>> QueryBuilderUtils<T> addEqualCondition(Field<E> field, E value) {
        return addCondition(field, value, field.eq(value));
    }

    // ====================== BUILD QUERY PAGE ====================== //

    public Page<T> buildPage(Pageable pageable, String orderByCondition) {
        StringBuilder sql = new StringBuilder(baseSql);

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(DSL.and(conditions));
        }
        if (orderByCondition != null && !orderByCondition.isEmpty()) {
            sql.append(" ORDER BY ").append(orderByCondition);
        }

        Long total =
                dsl.fetchOne("SELECT COUNT(*) FROM (" + sql + ") as subquery").into(Long.class);

        sql.append(" LIMIT ? OFFSET ?");
        List<T> results =
                dsl.fetch(sql.toString(), pageable.getPageSize(), pageable.getOffset())
                        .map(rc -> rc.into(entityClass));

        return new PageImpl<>(results, pageable, total);
    }

    public List<T> buildList(String orderByCondition) {
        StringBuilder sql = new StringBuilder(baseSql);

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(DSL.and(conditions));
        }
        if (orderByCondition != null && !orderByCondition.isEmpty()) {
            sql.append(" ORDER BY ").append(orderByCondition);
        }

        return dsl.fetch(sql.toString()).map(rc -> rc.into(entityClass));
    }
}
