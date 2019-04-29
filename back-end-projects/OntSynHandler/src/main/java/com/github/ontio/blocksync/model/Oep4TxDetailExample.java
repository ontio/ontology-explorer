package com.github.ontio.blocksync.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Oep4TxDetailExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public Oep4TxDetailExample() {
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

        public Criteria andTxHashIsNull() {
            addCriterion("tx_hash is null");
            return (Criteria) this;
        }

        public Criteria andTxHashIsNotNull() {
            addCriterion("tx_hash is not null");
            return (Criteria) this;
        }

        public Criteria andTxHashEqualTo(String value) {
            addCriterion("tx_hash =", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashNotEqualTo(String value) {
            addCriterion("tx_hash <>", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashGreaterThan(String value) {
            addCriterion("tx_hash >", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashGreaterThanOrEqualTo(String value) {
            addCriterion("tx_hash >=", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashLessThan(String value) {
            addCriterion("tx_hash <", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashLessThanOrEqualTo(String value) {
            addCriterion("tx_hash <=", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashLike(String value) {
            addCriterion("tx_hash like", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashNotLike(String value) {
            addCriterion("tx_hash not like", value, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashIn(List<String> values) {
            addCriterion("tx_hash in", values, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashNotIn(List<String> values) {
            addCriterion("tx_hash not in", values, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashBetween(String value1, String value2) {
            addCriterion("tx_hash between", value1, value2, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxHashNotBetween(String value1, String value2) {
            addCriterion("tx_hash not between", value1, value2, "txHash");
            return (Criteria) this;
        }

        public Criteria andTxIndexIsNull() {
            addCriterion("tx_index is null");
            return (Criteria) this;
        }

        public Criteria andTxIndexIsNotNull() {
            addCriterion("tx_index is not null");
            return (Criteria) this;
        }

        public Criteria andTxIndexEqualTo(Integer value) {
            addCriterion("tx_index =", value, "txIndex");
            return (Criteria) this;
        }

        public Criteria andTxIndexNotEqualTo(Integer value) {
            addCriterion("tx_index <>", value, "txIndex");
            return (Criteria) this;
        }

        public Criteria andTxIndexGreaterThan(Integer value) {
            addCriterion("tx_index >", value, "txIndex");
            return (Criteria) this;
        }

        public Criteria andTxIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("tx_index >=", value, "txIndex");
            return (Criteria) this;
        }

        public Criteria andTxIndexLessThan(Integer value) {
            addCriterion("tx_index <", value, "txIndex");
            return (Criteria) this;
        }

        public Criteria andTxIndexLessThanOrEqualTo(Integer value) {
            addCriterion("tx_index <=", value, "txIndex");
            return (Criteria) this;
        }

        public Criteria andTxIndexIn(List<Integer> values) {
            addCriterion("tx_index in", values, "txIndex");
            return (Criteria) this;
        }

        public Criteria andTxIndexNotIn(List<Integer> values) {
            addCriterion("tx_index not in", values, "txIndex");
            return (Criteria) this;
        }

        public Criteria andTxIndexBetween(Integer value1, Integer value2) {
            addCriterion("tx_index between", value1, value2, "txIndex");
            return (Criteria) this;
        }

        public Criteria andTxIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("tx_index not between", value1, value2, "txIndex");
            return (Criteria) this;
        }

        public Criteria andTxTypeIsNull() {
            addCriterion("tx_type is null");
            return (Criteria) this;
        }

        public Criteria andTxTypeIsNotNull() {
            addCriterion("tx_type is not null");
            return (Criteria) this;
        }

        public Criteria andTxTypeEqualTo(Integer value) {
            addCriterion("tx_type =", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeNotEqualTo(Integer value) {
            addCriterion("tx_type <>", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeGreaterThan(Integer value) {
            addCriterion("tx_type >", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("tx_type >=", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeLessThan(Integer value) {
            addCriterion("tx_type <", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeLessThanOrEqualTo(Integer value) {
            addCriterion("tx_type <=", value, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeIn(List<Integer> values) {
            addCriterion("tx_type in", values, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeNotIn(List<Integer> values) {
            addCriterion("tx_type not in", values, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeBetween(Integer value1, Integer value2) {
            addCriterion("tx_type between", value1, value2, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("tx_type not between", value1, value2, "txType");
            return (Criteria) this;
        }

        public Criteria andTxTimeIsNull() {
            addCriterion("tx_time is null");
            return (Criteria) this;
        }

        public Criteria andTxTimeIsNotNull() {
            addCriterion("tx_time is not null");
            return (Criteria) this;
        }

        public Criteria andTxTimeEqualTo(Integer value) {
            addCriterion("tx_time =", value, "txTime");
            return (Criteria) this;
        }

        public Criteria andTxTimeNotEqualTo(Integer value) {
            addCriterion("tx_time <>", value, "txTime");
            return (Criteria) this;
        }

        public Criteria andTxTimeGreaterThan(Integer value) {
            addCriterion("tx_time >", value, "txTime");
            return (Criteria) this;
        }

        public Criteria andTxTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("tx_time >=", value, "txTime");
            return (Criteria) this;
        }

        public Criteria andTxTimeLessThan(Integer value) {
            addCriterion("tx_time <", value, "txTime");
            return (Criteria) this;
        }

        public Criteria andTxTimeLessThanOrEqualTo(Integer value) {
            addCriterion("tx_time <=", value, "txTime");
            return (Criteria) this;
        }

        public Criteria andTxTimeIn(List<Integer> values) {
            addCriterion("tx_time in", values, "txTime");
            return (Criteria) this;
        }

        public Criteria andTxTimeNotIn(List<Integer> values) {
            addCriterion("tx_time not in", values, "txTime");
            return (Criteria) this;
        }

        public Criteria andTxTimeBetween(Integer value1, Integer value2) {
            addCriterion("tx_time between", value1, value2, "txTime");
            return (Criteria) this;
        }

        public Criteria andTxTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("tx_time not between", value1, value2, "txTime");
            return (Criteria) this;
        }

        public Criteria andBlockHeightIsNull() {
            addCriterion("block_height is null");
            return (Criteria) this;
        }

        public Criteria andBlockHeightIsNotNull() {
            addCriterion("block_height is not null");
            return (Criteria) this;
        }

        public Criteria andBlockHeightEqualTo(Integer value) {
            addCriterion("block_height =", value, "blockHeight");
            return (Criteria) this;
        }

        public Criteria andBlockHeightNotEqualTo(Integer value) {
            addCriterion("block_height <>", value, "blockHeight");
            return (Criteria) this;
        }

        public Criteria andBlockHeightGreaterThan(Integer value) {
            addCriterion("block_height >", value, "blockHeight");
            return (Criteria) this;
        }

        public Criteria andBlockHeightGreaterThanOrEqualTo(Integer value) {
            addCriterion("block_height >=", value, "blockHeight");
            return (Criteria) this;
        }

        public Criteria andBlockHeightLessThan(Integer value) {
            addCriterion("block_height <", value, "blockHeight");
            return (Criteria) this;
        }

        public Criteria andBlockHeightLessThanOrEqualTo(Integer value) {
            addCriterion("block_height <=", value, "blockHeight");
            return (Criteria) this;
        }

        public Criteria andBlockHeightIn(List<Integer> values) {
            addCriterion("block_height in", values, "blockHeight");
            return (Criteria) this;
        }

        public Criteria andBlockHeightNotIn(List<Integer> values) {
            addCriterion("block_height not in", values, "blockHeight");
            return (Criteria) this;
        }

        public Criteria andBlockHeightBetween(Integer value1, Integer value2) {
            addCriterion("block_height between", value1, value2, "blockHeight");
            return (Criteria) this;
        }

        public Criteria andBlockHeightNotBetween(Integer value1, Integer value2) {
            addCriterion("block_height not between", value1, value2, "blockHeight");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(BigDecimal value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(BigDecimal value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(BigDecimal value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(BigDecimal value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<BigDecimal> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<BigDecimal> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andFeeIsNull() {
            addCriterion("fee is null");
            return (Criteria) this;
        }

        public Criteria andFeeIsNotNull() {
            addCriterion("fee is not null");
            return (Criteria) this;
        }

        public Criteria andFeeEqualTo(BigDecimal value) {
            addCriterion("fee =", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotEqualTo(BigDecimal value) {
            addCriterion("fee <>", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThan(BigDecimal value) {
            addCriterion("fee >", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fee >=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThan(BigDecimal value) {
            addCriterion("fee <", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fee <=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeIn(List<BigDecimal> values) {
            addCriterion("fee in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotIn(List<BigDecimal> values) {
            addCriterion("fee not in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fee between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fee not between", value1, value2, "fee");
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

        public Criteria andFromAddressIsNull() {
            addCriterion("from_address is null");
            return (Criteria) this;
        }

        public Criteria andFromAddressIsNotNull() {
            addCriterion("from_address is not null");
            return (Criteria) this;
        }

        public Criteria andFromAddressEqualTo(String value) {
            addCriterion("from_address =", value, "fromAddress");
            return (Criteria) this;
        }

        public Criteria andFromAddressNotEqualTo(String value) {
            addCriterion("from_address <>", value, "fromAddress");
            return (Criteria) this;
        }

        public Criteria andFromAddressGreaterThan(String value) {
            addCriterion("from_address >", value, "fromAddress");
            return (Criteria) this;
        }

        public Criteria andFromAddressGreaterThanOrEqualTo(String value) {
            addCriterion("from_address >=", value, "fromAddress");
            return (Criteria) this;
        }

        public Criteria andFromAddressLessThan(String value) {
            addCriterion("from_address <", value, "fromAddress");
            return (Criteria) this;
        }

        public Criteria andFromAddressLessThanOrEqualTo(String value) {
            addCriterion("from_address <=", value, "fromAddress");
            return (Criteria) this;
        }

        public Criteria andFromAddressLike(String value) {
            addCriterion("from_address like", value, "fromAddress");
            return (Criteria) this;
        }

        public Criteria andFromAddressNotLike(String value) {
            addCriterion("from_address not like", value, "fromAddress");
            return (Criteria) this;
        }

        public Criteria andFromAddressIn(List<String> values) {
            addCriterion("from_address in", values, "fromAddress");
            return (Criteria) this;
        }

        public Criteria andFromAddressNotIn(List<String> values) {
            addCriterion("from_address not in", values, "fromAddress");
            return (Criteria) this;
        }

        public Criteria andFromAddressBetween(String value1, String value2) {
            addCriterion("from_address between", value1, value2, "fromAddress");
            return (Criteria) this;
        }

        public Criteria andFromAddressNotBetween(String value1, String value2) {
            addCriterion("from_address not between", value1, value2, "fromAddress");
            return (Criteria) this;
        }

        public Criteria andToAddressIsNull() {
            addCriterion("to_address is null");
            return (Criteria) this;
        }

        public Criteria andToAddressIsNotNull() {
            addCriterion("to_address is not null");
            return (Criteria) this;
        }

        public Criteria andToAddressEqualTo(String value) {
            addCriterion("to_address =", value, "toAddress");
            return (Criteria) this;
        }

        public Criteria andToAddressNotEqualTo(String value) {
            addCriterion("to_address <>", value, "toAddress");
            return (Criteria) this;
        }

        public Criteria andToAddressGreaterThan(String value) {
            addCriterion("to_address >", value, "toAddress");
            return (Criteria) this;
        }

        public Criteria andToAddressGreaterThanOrEqualTo(String value) {
            addCriterion("to_address >=", value, "toAddress");
            return (Criteria) this;
        }

        public Criteria andToAddressLessThan(String value) {
            addCriterion("to_address <", value, "toAddress");
            return (Criteria) this;
        }

        public Criteria andToAddressLessThanOrEqualTo(String value) {
            addCriterion("to_address <=", value, "toAddress");
            return (Criteria) this;
        }

        public Criteria andToAddressLike(String value) {
            addCriterion("to_address like", value, "toAddress");
            return (Criteria) this;
        }

        public Criteria andToAddressNotLike(String value) {
            addCriterion("to_address not like", value, "toAddress");
            return (Criteria) this;
        }

        public Criteria andToAddressIn(List<String> values) {
            addCriterion("to_address in", values, "toAddress");
            return (Criteria) this;
        }

        public Criteria andToAddressNotIn(List<String> values) {
            addCriterion("to_address not in", values, "toAddress");
            return (Criteria) this;
        }

        public Criteria andToAddressBetween(String value1, String value2) {
            addCriterion("to_address between", value1, value2, "toAddress");
            return (Criteria) this;
        }

        public Criteria andToAddressNotBetween(String value1, String value2) {
            addCriterion("to_address not between", value1, value2, "toAddress");
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

        public Criteria andBlockIndexIsNull() {
            addCriterion("block_index is null");
            return (Criteria) this;
        }

        public Criteria andBlockIndexIsNotNull() {
            addCriterion("block_index is not null");
            return (Criteria) this;
        }

        public Criteria andBlockIndexEqualTo(Integer value) {
            addCriterion("block_index =", value, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexNotEqualTo(Integer value) {
            addCriterion("block_index <>", value, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexGreaterThan(Integer value) {
            addCriterion("block_index >", value, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("block_index >=", value, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexLessThan(Integer value) {
            addCriterion("block_index <", value, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexLessThanOrEqualTo(Integer value) {
            addCriterion("block_index <=", value, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexIn(List<Integer> values) {
            addCriterion("block_index in", values, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexNotIn(List<Integer> values) {
            addCriterion("block_index not in", values, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexBetween(Integer value1, Integer value2) {
            addCriterion("block_index between", value1, value2, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("block_index not between", value1, value2, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagIsNull() {
            addCriterion("confirm_flag is null");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagIsNotNull() {
            addCriterion("confirm_flag is not null");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagEqualTo(Integer value) {
            addCriterion("confirm_flag =", value, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagNotEqualTo(Integer value) {
            addCriterion("confirm_flag <>", value, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagGreaterThan(Integer value) {
            addCriterion("confirm_flag >", value, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("confirm_flag >=", value, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagLessThan(Integer value) {
            addCriterion("confirm_flag <", value, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagLessThanOrEqualTo(Integer value) {
            addCriterion("confirm_flag <=", value, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagIn(List<Integer> values) {
            addCriterion("confirm_flag in", values, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagNotIn(List<Integer> values) {
            addCriterion("confirm_flag not in", values, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagBetween(Integer value1, Integer value2) {
            addCriterion("confirm_flag between", value1, value2, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andConfirmFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("confirm_flag not between", value1, value2, "confirmFlag");
            return (Criteria) this;
        }

        public Criteria andEventTypeIsNull() {
            addCriterion("event_type is null");
            return (Criteria) this;
        }

        public Criteria andEventTypeIsNotNull() {
            addCriterion("event_type is not null");
            return (Criteria) this;
        }

        public Criteria andEventTypeEqualTo(Integer value) {
            addCriterion("event_type =", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotEqualTo(Integer value) {
            addCriterion("event_type <>", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeGreaterThan(Integer value) {
            addCriterion("event_type >", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("event_type >=", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLessThan(Integer value) {
            addCriterion("event_type <", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLessThanOrEqualTo(Integer value) {
            addCriterion("event_type <=", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeIn(List<Integer> values) {
            addCriterion("event_type in", values, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotIn(List<Integer> values) {
            addCriterion("event_type not in", values, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeBetween(Integer value1, Integer value2) {
            addCriterion("event_type between", value1, value2, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("event_type not between", value1, value2, "eventType");
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

        public Criteria andPayerIsNull() {
            addCriterion("payer is null");
            return (Criteria) this;
        }

        public Criteria andPayerIsNotNull() {
            addCriterion("payer is not null");
            return (Criteria) this;
        }

        public Criteria andPayerEqualTo(String value) {
            addCriterion("payer =", value, "payer");
            return (Criteria) this;
        }

        public Criteria andPayerNotEqualTo(String value) {
            addCriterion("payer <>", value, "payer");
            return (Criteria) this;
        }

        public Criteria andPayerGreaterThan(String value) {
            addCriterion("payer >", value, "payer");
            return (Criteria) this;
        }

        public Criteria andPayerGreaterThanOrEqualTo(String value) {
            addCriterion("payer >=", value, "payer");
            return (Criteria) this;
        }

        public Criteria andPayerLessThan(String value) {
            addCriterion("payer <", value, "payer");
            return (Criteria) this;
        }

        public Criteria andPayerLessThanOrEqualTo(String value) {
            addCriterion("payer <=", value, "payer");
            return (Criteria) this;
        }

        public Criteria andPayerLike(String value) {
            addCriterion("payer like", value, "payer");
            return (Criteria) this;
        }

        public Criteria andPayerNotLike(String value) {
            addCriterion("payer not like", value, "payer");
            return (Criteria) this;
        }

        public Criteria andPayerIn(List<String> values) {
            addCriterion("payer in", values, "payer");
            return (Criteria) this;
        }

        public Criteria andPayerNotIn(List<String> values) {
            addCriterion("payer not in", values, "payer");
            return (Criteria) this;
        }

        public Criteria andPayerBetween(String value1, String value2) {
            addCriterion("payer between", value1, value2, "payer");
            return (Criteria) this;
        }

        public Criteria andPayerNotBetween(String value1, String value2) {
            addCriterion("payer not between", value1, value2, "payer");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashIsNull() {
            addCriterion("called_contract_hash is null");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashIsNotNull() {
            addCriterion("called_contract_hash is not null");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashEqualTo(String value) {
            addCriterion("called_contract_hash =", value, "calledContractHash");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashNotEqualTo(String value) {
            addCriterion("called_contract_hash <>", value, "calledContractHash");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashGreaterThan(String value) {
            addCriterion("called_contract_hash >", value, "calledContractHash");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashGreaterThanOrEqualTo(String value) {
            addCriterion("called_contract_hash >=", value, "calledContractHash");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashLessThan(String value) {
            addCriterion("called_contract_hash <", value, "calledContractHash");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashLessThanOrEqualTo(String value) {
            addCriterion("called_contract_hash <=", value, "calledContractHash");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashLike(String value) {
            addCriterion("called_contract_hash like", value, "calledContractHash");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashNotLike(String value) {
            addCriterion("called_contract_hash not like", value, "calledContractHash");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashIn(List<String> values) {
            addCriterion("called_contract_hash in", values, "calledContractHash");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashNotIn(List<String> values) {
            addCriterion("called_contract_hash not in", values, "calledContractHash");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashBetween(String value1, String value2) {
            addCriterion("called_contract_hash between", value1, value2, "calledContractHash");
            return (Criteria) this;
        }

        public Criteria andCalledContractHashNotBetween(String value1, String value2) {
            addCriterion("called_contract_hash not between", value1, value2, "calledContractHash");
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