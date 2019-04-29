package com.github.ontio.blocksync.model;

import java.util.ArrayList;
import java.util.List;

public class BlockExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BlockExample() {
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

        public Criteria andBlockHashIsNull() {
            addCriterion("block_hash is null");
            return (Criteria) this;
        }

        public Criteria andBlockHashIsNotNull() {
            addCriterion("block_hash is not null");
            return (Criteria) this;
        }

        public Criteria andBlockHashEqualTo(String value) {
            addCriterion("block_hash =", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashNotEqualTo(String value) {
            addCriterion("block_hash <>", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashGreaterThan(String value) {
            addCriterion("block_hash >", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashGreaterThanOrEqualTo(String value) {
            addCriterion("block_hash >=", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashLessThan(String value) {
            addCriterion("block_hash <", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashLessThanOrEqualTo(String value) {
            addCriterion("block_hash <=", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashLike(String value) {
            addCriterion("block_hash like", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashNotLike(String value) {
            addCriterion("block_hash not like", value, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashIn(List<String> values) {
            addCriterion("block_hash in", values, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashNotIn(List<String> values) {
            addCriterion("block_hash not in", values, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashBetween(String value1, String value2) {
            addCriterion("block_hash between", value1, value2, "blockHash");
            return (Criteria) this;
        }

        public Criteria andBlockHashNotBetween(String value1, String value2) {
            addCriterion("block_hash not between", value1, value2, "blockHash");
            return (Criteria) this;
        }

        public Criteria andTxsRootIsNull() {
            addCriterion("txs_root is null");
            return (Criteria) this;
        }

        public Criteria andTxsRootIsNotNull() {
            addCriterion("txs_root is not null");
            return (Criteria) this;
        }

        public Criteria andTxsRootEqualTo(String value) {
            addCriterion("txs_root =", value, "txsRoot");
            return (Criteria) this;
        }

        public Criteria andTxsRootNotEqualTo(String value) {
            addCriterion("txs_root <>", value, "txsRoot");
            return (Criteria) this;
        }

        public Criteria andTxsRootGreaterThan(String value) {
            addCriterion("txs_root >", value, "txsRoot");
            return (Criteria) this;
        }

        public Criteria andTxsRootGreaterThanOrEqualTo(String value) {
            addCriterion("txs_root >=", value, "txsRoot");
            return (Criteria) this;
        }

        public Criteria andTxsRootLessThan(String value) {
            addCriterion("txs_root <", value, "txsRoot");
            return (Criteria) this;
        }

        public Criteria andTxsRootLessThanOrEqualTo(String value) {
            addCriterion("txs_root <=", value, "txsRoot");
            return (Criteria) this;
        }

        public Criteria andTxsRootLike(String value) {
            addCriterion("txs_root like", value, "txsRoot");
            return (Criteria) this;
        }

        public Criteria andTxsRootNotLike(String value) {
            addCriterion("txs_root not like", value, "txsRoot");
            return (Criteria) this;
        }

        public Criteria andTxsRootIn(List<String> values) {
            addCriterion("txs_root in", values, "txsRoot");
            return (Criteria) this;
        }

        public Criteria andTxsRootNotIn(List<String> values) {
            addCriterion("txs_root not in", values, "txsRoot");
            return (Criteria) this;
        }

        public Criteria andTxsRootBetween(String value1, String value2) {
            addCriterion("txs_root between", value1, value2, "txsRoot");
            return (Criteria) this;
        }

        public Criteria andTxsRootNotBetween(String value1, String value2) {
            addCriterion("txs_root not between", value1, value2, "txsRoot");
            return (Criteria) this;
        }

        public Criteria andBlockTimeIsNull() {
            addCriterion("block_time is null");
            return (Criteria) this;
        }

        public Criteria andBlockTimeIsNotNull() {
            addCriterion("block_time is not null");
            return (Criteria) this;
        }

        public Criteria andBlockTimeEqualTo(Integer value) {
            addCriterion("block_time =", value, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeNotEqualTo(Integer value) {
            addCriterion("block_time <>", value, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeGreaterThan(Integer value) {
            addCriterion("block_time >", value, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("block_time >=", value, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeLessThan(Integer value) {
            addCriterion("block_time <", value, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeLessThanOrEqualTo(Integer value) {
            addCriterion("block_time <=", value, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeIn(List<Integer> values) {
            addCriterion("block_time in", values, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeNotIn(List<Integer> values) {
            addCriterion("block_time not in", values, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeBetween(Integer value1, Integer value2) {
            addCriterion("block_time between", value1, value2, "blockTime");
            return (Criteria) this;
        }

        public Criteria andBlockTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("block_time not between", value1, value2, "blockTime");
            return (Criteria) this;
        }

        public Criteria andConsensusDataIsNull() {
            addCriterion("consensus_data is null");
            return (Criteria) this;
        }

        public Criteria andConsensusDataIsNotNull() {
            addCriterion("consensus_data is not null");
            return (Criteria) this;
        }

        public Criteria andConsensusDataEqualTo(String value) {
            addCriterion("consensus_data =", value, "consensusData");
            return (Criteria) this;
        }

        public Criteria andConsensusDataNotEqualTo(String value) {
            addCriterion("consensus_data <>", value, "consensusData");
            return (Criteria) this;
        }

        public Criteria andConsensusDataGreaterThan(String value) {
            addCriterion("consensus_data >", value, "consensusData");
            return (Criteria) this;
        }

        public Criteria andConsensusDataGreaterThanOrEqualTo(String value) {
            addCriterion("consensus_data >=", value, "consensusData");
            return (Criteria) this;
        }

        public Criteria andConsensusDataLessThan(String value) {
            addCriterion("consensus_data <", value, "consensusData");
            return (Criteria) this;
        }

        public Criteria andConsensusDataLessThanOrEqualTo(String value) {
            addCriterion("consensus_data <=", value, "consensusData");
            return (Criteria) this;
        }

        public Criteria andConsensusDataLike(String value) {
            addCriterion("consensus_data like", value, "consensusData");
            return (Criteria) this;
        }

        public Criteria andConsensusDataNotLike(String value) {
            addCriterion("consensus_data not like", value, "consensusData");
            return (Criteria) this;
        }

        public Criteria andConsensusDataIn(List<String> values) {
            addCriterion("consensus_data in", values, "consensusData");
            return (Criteria) this;
        }

        public Criteria andConsensusDataNotIn(List<String> values) {
            addCriterion("consensus_data not in", values, "consensusData");
            return (Criteria) this;
        }

        public Criteria andConsensusDataBetween(String value1, String value2) {
            addCriterion("consensus_data between", value1, value2, "consensusData");
            return (Criteria) this;
        }

        public Criteria andConsensusDataNotBetween(String value1, String value2) {
            addCriterion("consensus_data not between", value1, value2, "consensusData");
            return (Criteria) this;
        }

        public Criteria andBookkeepersIsNull() {
            addCriterion("bookkeepers is null");
            return (Criteria) this;
        }

        public Criteria andBookkeepersIsNotNull() {
            addCriterion("bookkeepers is not null");
            return (Criteria) this;
        }

        public Criteria andBookkeepersEqualTo(String value) {
            addCriterion("bookkeepers =", value, "bookkeepers");
            return (Criteria) this;
        }

        public Criteria andBookkeepersNotEqualTo(String value) {
            addCriterion("bookkeepers <>", value, "bookkeepers");
            return (Criteria) this;
        }

        public Criteria andBookkeepersGreaterThan(String value) {
            addCriterion("bookkeepers >", value, "bookkeepers");
            return (Criteria) this;
        }

        public Criteria andBookkeepersGreaterThanOrEqualTo(String value) {
            addCriterion("bookkeepers >=", value, "bookkeepers");
            return (Criteria) this;
        }

        public Criteria andBookkeepersLessThan(String value) {
            addCriterion("bookkeepers <", value, "bookkeepers");
            return (Criteria) this;
        }

        public Criteria andBookkeepersLessThanOrEqualTo(String value) {
            addCriterion("bookkeepers <=", value, "bookkeepers");
            return (Criteria) this;
        }

        public Criteria andBookkeepersLike(String value) {
            addCriterion("bookkeepers like", value, "bookkeepers");
            return (Criteria) this;
        }

        public Criteria andBookkeepersNotLike(String value) {
            addCriterion("bookkeepers not like", value, "bookkeepers");
            return (Criteria) this;
        }

        public Criteria andBookkeepersIn(List<String> values) {
            addCriterion("bookkeepers in", values, "bookkeepers");
            return (Criteria) this;
        }

        public Criteria andBookkeepersNotIn(List<String> values) {
            addCriterion("bookkeepers not in", values, "bookkeepers");
            return (Criteria) this;
        }

        public Criteria andBookkeepersBetween(String value1, String value2) {
            addCriterion("bookkeepers between", value1, value2, "bookkeepers");
            return (Criteria) this;
        }

        public Criteria andBookkeepersNotBetween(String value1, String value2) {
            addCriterion("bookkeepers not between", value1, value2, "bookkeepers");
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

        public Criteria andBlockSizeIsNull() {
            addCriterion("block_size is null");
            return (Criteria) this;
        }

        public Criteria andBlockSizeIsNotNull() {
            addCriterion("block_size is not null");
            return (Criteria) this;
        }

        public Criteria andBlockSizeEqualTo(Integer value) {
            addCriterion("block_size =", value, "blockSize");
            return (Criteria) this;
        }

        public Criteria andBlockSizeNotEqualTo(Integer value) {
            addCriterion("block_size <>", value, "blockSize");
            return (Criteria) this;
        }

        public Criteria andBlockSizeGreaterThan(Integer value) {
            addCriterion("block_size >", value, "blockSize");
            return (Criteria) this;
        }

        public Criteria andBlockSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("block_size >=", value, "blockSize");
            return (Criteria) this;
        }

        public Criteria andBlockSizeLessThan(Integer value) {
            addCriterion("block_size <", value, "blockSize");
            return (Criteria) this;
        }

        public Criteria andBlockSizeLessThanOrEqualTo(Integer value) {
            addCriterion("block_size <=", value, "blockSize");
            return (Criteria) this;
        }

        public Criteria andBlockSizeIn(List<Integer> values) {
            addCriterion("block_size in", values, "blockSize");
            return (Criteria) this;
        }

        public Criteria andBlockSizeNotIn(List<Integer> values) {
            addCriterion("block_size not in", values, "blockSize");
            return (Criteria) this;
        }

        public Criteria andBlockSizeBetween(Integer value1, Integer value2) {
            addCriterion("block_size between", value1, value2, "blockSize");
            return (Criteria) this;
        }

        public Criteria andBlockSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("block_size not between", value1, value2, "blockSize");
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