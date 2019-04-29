package com.github.ontio.blocksync.model;

import java.util.ArrayList;
import java.util.List;

public class Oep5DragonExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public Oep5DragonExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andContractHashIsNull() {
            addCriterion("contract_hash is null");
            return (Criteria) this;
        }

        public Criteria andContractHashIsNotNull() {
            addCriterion("contract_hash is not null");
            return (Criteria) this;
        }

        public Criteria andContractHashEqualTo(String value) {
            addCriterion("contract_hash =", value, "contractHash");
            return (Criteria) this;
        }

        public Criteria andContractHashNotEqualTo(String value) {
            addCriterion("contract_hash <>", value, "contractHash");
            return (Criteria) this;
        }

        public Criteria andContractHashGreaterThan(String value) {
            addCriterion("contract_hash >", value, "contractHash");
            return (Criteria) this;
        }

        public Criteria andContractHashGreaterThanOrEqualTo(String value) {
            addCriterion("contract_hash >=", value, "contractHash");
            return (Criteria) this;
        }

        public Criteria andContractHashLessThan(String value) {
            addCriterion("contract_hash <", value, "contractHash");
            return (Criteria) this;
        }

        public Criteria andContractHashLessThanOrEqualTo(String value) {
            addCriterion("contract_hash <=", value, "contractHash");
            return (Criteria) this;
        }

        public Criteria andContractHashLike(String value) {
            addCriterion("contract_hash like", value, "contractHash");
            return (Criteria) this;
        }

        public Criteria andContractHashNotLike(String value) {
            addCriterion("contract_hash not like", value, "contractHash");
            return (Criteria) this;
        }

        public Criteria andContractHashIn(List<String> values) {
            addCriterion("contract_hash in", values, "contractHash");
            return (Criteria) this;
        }

        public Criteria andContractHashNotIn(List<String> values) {
            addCriterion("contract_hash not in", values, "contractHash");
            return (Criteria) this;
        }

        public Criteria andContractHashBetween(String value1, String value2) {
            addCriterion("contract_hash between", value1, value2, "contractHash");
            return (Criteria) this;
        }

        public Criteria andContractHashNotBetween(String value1, String value2) {
            addCriterion("contract_hash not between", value1, value2, "contractHash");
            return (Criteria) this;
        }

        public Criteria andAssetNameIsNull() {
            addCriterion("asset_name is null");
            return (Criteria) this;
        }

        public Criteria andAssetNameIsNotNull() {
            addCriterion("asset_name is not null");
            return (Criteria) this;
        }

        public Criteria andAssetNameEqualTo(String value) {
            addCriterion("asset_name =", value, "assetName");
            return (Criteria) this;
        }

        public Criteria andAssetNameNotEqualTo(String value) {
            addCriterion("asset_name <>", value, "assetName");
            return (Criteria) this;
        }

        public Criteria andAssetNameGreaterThan(String value) {
            addCriterion("asset_name >", value, "assetName");
            return (Criteria) this;
        }

        public Criteria andAssetNameGreaterThanOrEqualTo(String value) {
            addCriterion("asset_name >=", value, "assetName");
            return (Criteria) this;
        }

        public Criteria andAssetNameLessThan(String value) {
            addCriterion("asset_name <", value, "assetName");
            return (Criteria) this;
        }

        public Criteria andAssetNameLessThanOrEqualTo(String value) {
            addCriterion("asset_name <=", value, "assetName");
            return (Criteria) this;
        }

        public Criteria andAssetNameLike(String value) {
            addCriterion("asset_name like", value, "assetName");
            return (Criteria) this;
        }

        public Criteria andAssetNameNotLike(String value) {
            addCriterion("asset_name not like", value, "assetName");
            return (Criteria) this;
        }

        public Criteria andAssetNameIn(List<String> values) {
            addCriterion("asset_name in", values, "assetName");
            return (Criteria) this;
        }

        public Criteria andAssetNameNotIn(List<String> values) {
            addCriterion("asset_name not in", values, "assetName");
            return (Criteria) this;
        }

        public Criteria andAssetNameBetween(String value1, String value2) {
            addCriterion("asset_name between", value1, value2, "assetName");
            return (Criteria) this;
        }

        public Criteria andAssetNameNotBetween(String value1, String value2) {
            addCriterion("asset_name not between", value1, value2, "assetName");
            return (Criteria) this;
        }

        public Criteria andJsonUrlIsNull() {
            addCriterion("json_url is null");
            return (Criteria) this;
        }

        public Criteria andJsonUrlIsNotNull() {
            addCriterion("json_url is not null");
            return (Criteria) this;
        }

        public Criteria andJsonUrlEqualTo(String value) {
            addCriterion("json_url =", value, "jsonUrl");
            return (Criteria) this;
        }

        public Criteria andJsonUrlNotEqualTo(String value) {
            addCriterion("json_url <>", value, "jsonUrl");
            return (Criteria) this;
        }

        public Criteria andJsonUrlGreaterThan(String value) {
            addCriterion("json_url >", value, "jsonUrl");
            return (Criteria) this;
        }

        public Criteria andJsonUrlGreaterThanOrEqualTo(String value) {
            addCriterion("json_url >=", value, "jsonUrl");
            return (Criteria) this;
        }

        public Criteria andJsonUrlLessThan(String value) {
            addCriterion("json_url <", value, "jsonUrl");
            return (Criteria) this;
        }

        public Criteria andJsonUrlLessThanOrEqualTo(String value) {
            addCriterion("json_url <=", value, "jsonUrl");
            return (Criteria) this;
        }

        public Criteria andJsonUrlLike(String value) {
            addCriterion("json_url like", value, "jsonUrl");
            return (Criteria) this;
        }

        public Criteria andJsonUrlNotLike(String value) {
            addCriterion("json_url not like", value, "jsonUrl");
            return (Criteria) this;
        }

        public Criteria andJsonUrlIn(List<String> values) {
            addCriterion("json_url in", values, "jsonUrl");
            return (Criteria) this;
        }

        public Criteria andJsonUrlNotIn(List<String> values) {
            addCriterion("json_url not in", values, "jsonUrl");
            return (Criteria) this;
        }

        public Criteria andJsonUrlBetween(String value1, String value2) {
            addCriterion("json_url between", value1, value2, "jsonUrl");
            return (Criteria) this;
        }

        public Criteria andJsonUrlNotBetween(String value1, String value2) {
            addCriterion("json_url not between", value1, value2, "jsonUrl");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}