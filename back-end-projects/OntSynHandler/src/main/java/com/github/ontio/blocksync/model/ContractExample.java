package com.github.ontio.blocksync.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ContractExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ContractExample() {
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

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Integer value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Integer value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Integer value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Integer value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Integer value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Integer> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Integer> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Integer value1, Integer value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Integer value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Integer value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Integer value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Integer value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Integer value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Integer> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Integer> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Integer value1, Integer value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andAuditFlagIsNull() {
            addCriterion("audit_flag is null");
            return (Criteria) this;
        }

        public Criteria andAuditFlagIsNotNull() {
            addCriterion("audit_flag is not null");
            return (Criteria) this;
        }

        public Criteria andAuditFlagEqualTo(Integer value) {
            addCriterion("audit_flag =", value, "auditFlag");
            return (Criteria) this;
        }

        public Criteria andAuditFlagNotEqualTo(Integer value) {
            addCriterion("audit_flag <>", value, "auditFlag");
            return (Criteria) this;
        }

        public Criteria andAuditFlagGreaterThan(Integer value) {
            addCriterion("audit_flag >", value, "auditFlag");
            return (Criteria) this;
        }

        public Criteria andAuditFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("audit_flag >=", value, "auditFlag");
            return (Criteria) this;
        }

        public Criteria andAuditFlagLessThan(Integer value) {
            addCriterion("audit_flag <", value, "auditFlag");
            return (Criteria) this;
        }

        public Criteria andAuditFlagLessThanOrEqualTo(Integer value) {
            addCriterion("audit_flag <=", value, "auditFlag");
            return (Criteria) this;
        }

        public Criteria andAuditFlagIn(List<Integer> values) {
            addCriterion("audit_flag in", values, "auditFlag");
            return (Criteria) this;
        }

        public Criteria andAuditFlagNotIn(List<Integer> values) {
            addCriterion("audit_flag not in", values, "auditFlag");
            return (Criteria) this;
        }

        public Criteria andAuditFlagBetween(Integer value1, Integer value2) {
            addCriterion("audit_flag between", value1, value2, "auditFlag");
            return (Criteria) this;
        }

        public Criteria andAuditFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("audit_flag not between", value1, value2, "auditFlag");
            return (Criteria) this;
        }

        public Criteria andContactInfoIsNull() {
            addCriterion("contact_info is null");
            return (Criteria) this;
        }

        public Criteria andContactInfoIsNotNull() {
            addCriterion("contact_info is not null");
            return (Criteria) this;
        }

        public Criteria andContactInfoEqualTo(String value) {
            addCriterion("contact_info =", value, "contactInfo");
            return (Criteria) this;
        }

        public Criteria andContactInfoNotEqualTo(String value) {
            addCriterion("contact_info <>", value, "contactInfo");
            return (Criteria) this;
        }

        public Criteria andContactInfoGreaterThan(String value) {
            addCriterion("contact_info >", value, "contactInfo");
            return (Criteria) this;
        }

        public Criteria andContactInfoGreaterThanOrEqualTo(String value) {
            addCriterion("contact_info >=", value, "contactInfo");
            return (Criteria) this;
        }

        public Criteria andContactInfoLessThan(String value) {
            addCriterion("contact_info <", value, "contactInfo");
            return (Criteria) this;
        }

        public Criteria andContactInfoLessThanOrEqualTo(String value) {
            addCriterion("contact_info <=", value, "contactInfo");
            return (Criteria) this;
        }

        public Criteria andContactInfoLike(String value) {
            addCriterion("contact_info like", value, "contactInfo");
            return (Criteria) this;
        }

        public Criteria andContactInfoNotLike(String value) {
            addCriterion("contact_info not like", value, "contactInfo");
            return (Criteria) this;
        }

        public Criteria andContactInfoIn(List<String> values) {
            addCriterion("contact_info in", values, "contactInfo");
            return (Criteria) this;
        }

        public Criteria andContactInfoNotIn(List<String> values) {
            addCriterion("contact_info not in", values, "contactInfo");
            return (Criteria) this;
        }

        public Criteria andContactInfoBetween(String value1, String value2) {
            addCriterion("contact_info between", value1, value2, "contactInfo");
            return (Criteria) this;
        }

        public Criteria andContactInfoNotBetween(String value1, String value2) {
            addCriterion("contact_info not between", value1, value2, "contactInfo");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("type like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("type not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andLogoIsNull() {
            addCriterion("logo is null");
            return (Criteria) this;
        }

        public Criteria andLogoIsNotNull() {
            addCriterion("logo is not null");
            return (Criteria) this;
        }

        public Criteria andLogoEqualTo(String value) {
            addCriterion("logo =", value, "logo");
            return (Criteria) this;
        }

        public Criteria andLogoNotEqualTo(String value) {
            addCriterion("logo <>", value, "logo");
            return (Criteria) this;
        }

        public Criteria andLogoGreaterThan(String value) {
            addCriterion("logo >", value, "logo");
            return (Criteria) this;
        }

        public Criteria andLogoGreaterThanOrEqualTo(String value) {
            addCriterion("logo >=", value, "logo");
            return (Criteria) this;
        }

        public Criteria andLogoLessThan(String value) {
            addCriterion("logo <", value, "logo");
            return (Criteria) this;
        }

        public Criteria andLogoLessThanOrEqualTo(String value) {
            addCriterion("logo <=", value, "logo");
            return (Criteria) this;
        }

        public Criteria andLogoLike(String value) {
            addCriterion("logo like", value, "logo");
            return (Criteria) this;
        }

        public Criteria andLogoNotLike(String value) {
            addCriterion("logo not like", value, "logo");
            return (Criteria) this;
        }

        public Criteria andLogoIn(List<String> values) {
            addCriterion("logo in", values, "logo");
            return (Criteria) this;
        }

        public Criteria andLogoNotIn(List<String> values) {
            addCriterion("logo not in", values, "logo");
            return (Criteria) this;
        }

        public Criteria andLogoBetween(String value1, String value2) {
            addCriterion("logo between", value1, value2, "logo");
            return (Criteria) this;
        }

        public Criteria andLogoNotBetween(String value1, String value2) {
            addCriterion("logo not between", value1, value2, "logo");
            return (Criteria) this;
        }

        public Criteria andCreatorIsNull() {
            addCriterion("creator is null");
            return (Criteria) this;
        }

        public Criteria andCreatorIsNotNull() {
            addCriterion("creator is not null");
            return (Criteria) this;
        }

        public Criteria andCreatorEqualTo(String value) {
            addCriterion("creator =", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotEqualTo(String value) {
            addCriterion("creator <>", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorGreaterThan(String value) {
            addCriterion("creator >", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorGreaterThanOrEqualTo(String value) {
            addCriterion("creator >=", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLessThan(String value) {
            addCriterion("creator <", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLessThanOrEqualTo(String value) {
            addCriterion("creator <=", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLike(String value) {
            addCriterion("creator like", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotLike(String value) {
            addCriterion("creator not like", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorIn(List<String> values) {
            addCriterion("creator in", values, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotIn(List<String> values) {
            addCriterion("creator not in", values, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorBetween(String value1, String value2) {
            addCriterion("creator between", value1, value2, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotBetween(String value1, String value2) {
            addCriterion("creator not between", value1, value2, "creator");
            return (Criteria) this;
        }

        public Criteria andAddressCountIsNull() {
            addCriterion("address_count is null");
            return (Criteria) this;
        }

        public Criteria andAddressCountIsNotNull() {
            addCriterion("address_count is not null");
            return (Criteria) this;
        }

        public Criteria andAddressCountEqualTo(Integer value) {
            addCriterion("address_count =", value, "addressCount");
            return (Criteria) this;
        }

        public Criteria andAddressCountNotEqualTo(Integer value) {
            addCriterion("address_count <>", value, "addressCount");
            return (Criteria) this;
        }

        public Criteria andAddressCountGreaterThan(Integer value) {
            addCriterion("address_count >", value, "addressCount");
            return (Criteria) this;
        }

        public Criteria andAddressCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("address_count >=", value, "addressCount");
            return (Criteria) this;
        }

        public Criteria andAddressCountLessThan(Integer value) {
            addCriterion("address_count <", value, "addressCount");
            return (Criteria) this;
        }

        public Criteria andAddressCountLessThanOrEqualTo(Integer value) {
            addCriterion("address_count <=", value, "addressCount");
            return (Criteria) this;
        }

        public Criteria andAddressCountIn(List<Integer> values) {
            addCriterion("address_count in", values, "addressCount");
            return (Criteria) this;
        }

        public Criteria andAddressCountNotIn(List<Integer> values) {
            addCriterion("address_count not in", values, "addressCount");
            return (Criteria) this;
        }

        public Criteria andAddressCountBetween(Integer value1, Integer value2) {
            addCriterion("address_count between", value1, value2, "addressCount");
            return (Criteria) this;
        }

        public Criteria andAddressCountNotBetween(Integer value1, Integer value2) {
            addCriterion("address_count not between", value1, value2, "addressCount");
            return (Criteria) this;
        }

        public Criteria andTxCountIsNull() {
            addCriterion("tx_count is null");
            return (Criteria) this;
        }

        public Criteria andTxCountIsNotNull() {
            addCriterion("tx_count is not null");
            return (Criteria) this;
        }

        public Criteria andTxCountEqualTo(Integer value) {
            addCriterion("tx_count =", value, "txCount");
            return (Criteria) this;
        }

        public Criteria andTxCountNotEqualTo(Integer value) {
            addCriterion("tx_count <>", value, "txCount");
            return (Criteria) this;
        }

        public Criteria andTxCountGreaterThan(Integer value) {
            addCriterion("tx_count >", value, "txCount");
            return (Criteria) this;
        }

        public Criteria andTxCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("tx_count >=", value, "txCount");
            return (Criteria) this;
        }

        public Criteria andTxCountLessThan(Integer value) {
            addCriterion("tx_count <", value, "txCount");
            return (Criteria) this;
        }

        public Criteria andTxCountLessThanOrEqualTo(Integer value) {
            addCriterion("tx_count <=", value, "txCount");
            return (Criteria) this;
        }

        public Criteria andTxCountIn(List<Integer> values) {
            addCriterion("tx_count in", values, "txCount");
            return (Criteria) this;
        }

        public Criteria andTxCountNotIn(List<Integer> values) {
            addCriterion("tx_count not in", values, "txCount");
            return (Criteria) this;
        }

        public Criteria andTxCountBetween(Integer value1, Integer value2) {
            addCriterion("tx_count between", value1, value2, "txCount");
            return (Criteria) this;
        }

        public Criteria andTxCountNotBetween(Integer value1, Integer value2) {
            addCriterion("tx_count not between", value1, value2, "txCount");
            return (Criteria) this;
        }

        public Criteria andOntSumIsNull() {
            addCriterion("ont_sum is null");
            return (Criteria) this;
        }

        public Criteria andOntSumIsNotNull() {
            addCriterion("ont_sum is not null");
            return (Criteria) this;
        }

        public Criteria andOntSumEqualTo(BigDecimal value) {
            addCriterion("ont_sum =", value, "ontSum");
            return (Criteria) this;
        }

        public Criteria andOntSumNotEqualTo(BigDecimal value) {
            addCriterion("ont_sum <>", value, "ontSum");
            return (Criteria) this;
        }

        public Criteria andOntSumGreaterThan(BigDecimal value) {
            addCriterion("ont_sum >", value, "ontSum");
            return (Criteria) this;
        }

        public Criteria andOntSumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ont_sum >=", value, "ontSum");
            return (Criteria) this;
        }

        public Criteria andOntSumLessThan(BigDecimal value) {
            addCriterion("ont_sum <", value, "ontSum");
            return (Criteria) this;
        }

        public Criteria andOntSumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ont_sum <=", value, "ontSum");
            return (Criteria) this;
        }

        public Criteria andOntSumIn(List<BigDecimal> values) {
            addCriterion("ont_sum in", values, "ontSum");
            return (Criteria) this;
        }

        public Criteria andOntSumNotIn(List<BigDecimal> values) {
            addCriterion("ont_sum not in", values, "ontSum");
            return (Criteria) this;
        }

        public Criteria andOntSumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ont_sum between", value1, value2, "ontSum");
            return (Criteria) this;
        }

        public Criteria andOntSumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ont_sum not between", value1, value2, "ontSum");
            return (Criteria) this;
        }

        public Criteria andOngSumIsNull() {
            addCriterion("ong_sum is null");
            return (Criteria) this;
        }

        public Criteria andOngSumIsNotNull() {
            addCriterion("ong_sum is not null");
            return (Criteria) this;
        }

        public Criteria andOngSumEqualTo(BigDecimal value) {
            addCriterion("ong_sum =", value, "ongSum");
            return (Criteria) this;
        }

        public Criteria andOngSumNotEqualTo(BigDecimal value) {
            addCriterion("ong_sum <>", value, "ongSum");
            return (Criteria) this;
        }

        public Criteria andOngSumGreaterThan(BigDecimal value) {
            addCriterion("ong_sum >", value, "ongSum");
            return (Criteria) this;
        }

        public Criteria andOngSumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ong_sum >=", value, "ongSum");
            return (Criteria) this;
        }

        public Criteria andOngSumLessThan(BigDecimal value) {
            addCriterion("ong_sum <", value, "ongSum");
            return (Criteria) this;
        }

        public Criteria andOngSumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ong_sum <=", value, "ongSum");
            return (Criteria) this;
        }

        public Criteria andOngSumIn(List<BigDecimal> values) {
            addCriterion("ong_sum in", values, "ongSum");
            return (Criteria) this;
        }

        public Criteria andOngSumNotIn(List<BigDecimal> values) {
            addCriterion("ong_sum not in", values, "ongSum");
            return (Criteria) this;
        }

        public Criteria andOngSumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ong_sum between", value1, value2, "ongSum");
            return (Criteria) this;
        }

        public Criteria andOngSumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ong_sum not between", value1, value2, "ongSum");
            return (Criteria) this;
        }

        public Criteria andTokenSumIsNull() {
            addCriterion("token_sum is null");
            return (Criteria) this;
        }

        public Criteria andTokenSumIsNotNull() {
            addCriterion("token_sum is not null");
            return (Criteria) this;
        }

        public Criteria andTokenSumEqualTo(String value) {
            addCriterion("token_sum =", value, "tokenSum");
            return (Criteria) this;
        }

        public Criteria andTokenSumNotEqualTo(String value) {
            addCriterion("token_sum <>", value, "tokenSum");
            return (Criteria) this;
        }

        public Criteria andTokenSumGreaterThan(String value) {
            addCriterion("token_sum >", value, "tokenSum");
            return (Criteria) this;
        }

        public Criteria andTokenSumGreaterThanOrEqualTo(String value) {
            addCriterion("token_sum >=", value, "tokenSum");
            return (Criteria) this;
        }

        public Criteria andTokenSumLessThan(String value) {
            addCriterion("token_sum <", value, "tokenSum");
            return (Criteria) this;
        }

        public Criteria andTokenSumLessThanOrEqualTo(String value) {
            addCriterion("token_sum <=", value, "tokenSum");
            return (Criteria) this;
        }

        public Criteria andTokenSumLike(String value) {
            addCriterion("token_sum like", value, "tokenSum");
            return (Criteria) this;
        }

        public Criteria andTokenSumNotLike(String value) {
            addCriterion("token_sum not like", value, "tokenSum");
            return (Criteria) this;
        }

        public Criteria andTokenSumIn(List<String> values) {
            addCriterion("token_sum in", values, "tokenSum");
            return (Criteria) this;
        }

        public Criteria andTokenSumNotIn(List<String> values) {
            addCriterion("token_sum not in", values, "tokenSum");
            return (Criteria) this;
        }

        public Criteria andTokenSumBetween(String value1, String value2) {
            addCriterion("token_sum between", value1, value2, "tokenSum");
            return (Criteria) this;
        }

        public Criteria andTokenSumNotBetween(String value1, String value2) {
            addCriterion("token_sum not between", value1, value2, "tokenSum");
            return (Criteria) this;
        }

        public Criteria andCategoryIsNull() {
            addCriterion("category is null");
            return (Criteria) this;
        }

        public Criteria andCategoryIsNotNull() {
            addCriterion("category is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryEqualTo(String value) {
            addCriterion("category =", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotEqualTo(String value) {
            addCriterion("category <>", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryGreaterThan(String value) {
            addCriterion("category >", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryGreaterThanOrEqualTo(String value) {
            addCriterion("category >=", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLessThan(String value) {
            addCriterion("category <", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLessThanOrEqualTo(String value) {
            addCriterion("category <=", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLike(String value) {
            addCriterion("category like", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotLike(String value) {
            addCriterion("category not like", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryIn(List<String> values) {
            addCriterion("category in", values, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotIn(List<String> values) {
            addCriterion("category not in", values, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryBetween(String value1, String value2) {
            addCriterion("category between", value1, value2, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotBetween(String value1, String value2) {
            addCriterion("category not between", value1, value2, "category");
            return (Criteria) this;
        }

        public Criteria andDappNameIsNull() {
            addCriterion("dapp_name is null");
            return (Criteria) this;
        }

        public Criteria andDappNameIsNotNull() {
            addCriterion("dapp_name is not null");
            return (Criteria) this;
        }

        public Criteria andDappNameEqualTo(String value) {
            addCriterion("dapp_name =", value, "dappName");
            return (Criteria) this;
        }

        public Criteria andDappNameNotEqualTo(String value) {
            addCriterion("dapp_name <>", value, "dappName");
            return (Criteria) this;
        }

        public Criteria andDappNameGreaterThan(String value) {
            addCriterion("dapp_name >", value, "dappName");
            return (Criteria) this;
        }

        public Criteria andDappNameGreaterThanOrEqualTo(String value) {
            addCriterion("dapp_name >=", value, "dappName");
            return (Criteria) this;
        }

        public Criteria andDappNameLessThan(String value) {
            addCriterion("dapp_name <", value, "dappName");
            return (Criteria) this;
        }

        public Criteria andDappNameLessThanOrEqualTo(String value) {
            addCriterion("dapp_name <=", value, "dappName");
            return (Criteria) this;
        }

        public Criteria andDappNameLike(String value) {
            addCriterion("dapp_name like", value, "dappName");
            return (Criteria) this;
        }

        public Criteria andDappNameNotLike(String value) {
            addCriterion("dapp_name not like", value, "dappName");
            return (Criteria) this;
        }

        public Criteria andDappNameIn(List<String> values) {
            addCriterion("dapp_name in", values, "dappName");
            return (Criteria) this;
        }

        public Criteria andDappNameNotIn(List<String> values) {
            addCriterion("dapp_name not in", values, "dappName");
            return (Criteria) this;
        }

        public Criteria andDappNameBetween(String value1, String value2) {
            addCriterion("dapp_name between", value1, value2, "dappName");
            return (Criteria) this;
        }

        public Criteria andDappNameNotBetween(String value1, String value2) {
            addCriterion("dapp_name not between", value1, value2, "dappName");
            return (Criteria) this;
        }

        public Criteria andDappstoreFlagIsNull() {
            addCriterion("dappstore_flag is null");
            return (Criteria) this;
        }

        public Criteria andDappstoreFlagIsNotNull() {
            addCriterion("dappstore_flag is not null");
            return (Criteria) this;
        }

        public Criteria andDappstoreFlagEqualTo(Integer value) {
            addCriterion("dappstore_flag =", value, "dappstoreFlag");
            return (Criteria) this;
        }

        public Criteria andDappstoreFlagNotEqualTo(Integer value) {
            addCriterion("dappstore_flag <>", value, "dappstoreFlag");
            return (Criteria) this;
        }

        public Criteria andDappstoreFlagGreaterThan(Integer value) {
            addCriterion("dappstore_flag >", value, "dappstoreFlag");
            return (Criteria) this;
        }

        public Criteria andDappstoreFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("dappstore_flag >=", value, "dappstoreFlag");
            return (Criteria) this;
        }

        public Criteria andDappstoreFlagLessThan(Integer value) {
            addCriterion("dappstore_flag <", value, "dappstoreFlag");
            return (Criteria) this;
        }

        public Criteria andDappstoreFlagLessThanOrEqualTo(Integer value) {
            addCriterion("dappstore_flag <=", value, "dappstoreFlag");
            return (Criteria) this;
        }

        public Criteria andDappstoreFlagIn(List<Integer> values) {
            addCriterion("dappstore_flag in", values, "dappstoreFlag");
            return (Criteria) this;
        }

        public Criteria andDappstoreFlagNotIn(List<Integer> values) {
            addCriterion("dappstore_flag not in", values, "dappstoreFlag");
            return (Criteria) this;
        }

        public Criteria andDappstoreFlagBetween(Integer value1, Integer value2) {
            addCriterion("dappstore_flag between", value1, value2, "dappstoreFlag");
            return (Criteria) this;
        }

        public Criteria andDappstoreFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("dappstore_flag not between", value1, value2, "dappstoreFlag");
            return (Criteria) this;
        }

        public Criteria andTotalRewardIsNull() {
            addCriterion("total_reward is null");
            return (Criteria) this;
        }

        public Criteria andTotalRewardIsNotNull() {
            addCriterion("total_reward is not null");
            return (Criteria) this;
        }

        public Criteria andTotalRewardEqualTo(BigDecimal value) {
            addCriterion("total_reward =", value, "totalReward");
            return (Criteria) this;
        }

        public Criteria andTotalRewardNotEqualTo(BigDecimal value) {
            addCriterion("total_reward <>", value, "totalReward");
            return (Criteria) this;
        }

        public Criteria andTotalRewardGreaterThan(BigDecimal value) {
            addCriterion("total_reward >", value, "totalReward");
            return (Criteria) this;
        }

        public Criteria andTotalRewardGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total_reward >=", value, "totalReward");
            return (Criteria) this;
        }

        public Criteria andTotalRewardLessThan(BigDecimal value) {
            addCriterion("total_reward <", value, "totalReward");
            return (Criteria) this;
        }

        public Criteria andTotalRewardLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total_reward <=", value, "totalReward");
            return (Criteria) this;
        }

        public Criteria andTotalRewardIn(List<BigDecimal> values) {
            addCriterion("total_reward in", values, "totalReward");
            return (Criteria) this;
        }

        public Criteria andTotalRewardNotIn(List<BigDecimal> values) {
            addCriterion("total_reward not in", values, "totalReward");
            return (Criteria) this;
        }

        public Criteria andTotalRewardBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_reward between", value1, value2, "totalReward");
            return (Criteria) this;
        }

        public Criteria andTotalRewardNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_reward not between", value1, value2, "totalReward");
            return (Criteria) this;
        }

        public Criteria andLastweekRewardIsNull() {
            addCriterion("lastweek_reward is null");
            return (Criteria) this;
        }

        public Criteria andLastweekRewardIsNotNull() {
            addCriterion("lastweek_reward is not null");
            return (Criteria) this;
        }

        public Criteria andLastweekRewardEqualTo(BigDecimal value) {
            addCriterion("lastweek_reward =", value, "lastweekReward");
            return (Criteria) this;
        }

        public Criteria andLastweekRewardNotEqualTo(BigDecimal value) {
            addCriterion("lastweek_reward <>", value, "lastweekReward");
            return (Criteria) this;
        }

        public Criteria andLastweekRewardGreaterThan(BigDecimal value) {
            addCriterion("lastweek_reward >", value, "lastweekReward");
            return (Criteria) this;
        }

        public Criteria andLastweekRewardGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("lastweek_reward >=", value, "lastweekReward");
            return (Criteria) this;
        }

        public Criteria andLastweekRewardLessThan(BigDecimal value) {
            addCriterion("lastweek_reward <", value, "lastweekReward");
            return (Criteria) this;
        }

        public Criteria andLastweekRewardLessThanOrEqualTo(BigDecimal value) {
            addCriterion("lastweek_reward <=", value, "lastweekReward");
            return (Criteria) this;
        }

        public Criteria andLastweekRewardIn(List<BigDecimal> values) {
            addCriterion("lastweek_reward in", values, "lastweekReward");
            return (Criteria) this;
        }

        public Criteria andLastweekRewardNotIn(List<BigDecimal> values) {
            addCriterion("lastweek_reward not in", values, "lastweekReward");
            return (Criteria) this;
        }

        public Criteria andLastweekRewardBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lastweek_reward between", value1, value2, "lastweekReward");
            return (Criteria) this;
        }

        public Criteria andLastweekRewardNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("lastweek_reward not between", value1, value2, "lastweekReward");
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