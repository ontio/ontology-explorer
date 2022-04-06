package com.github.ontio.model.common;

public enum SearchCategoryEnum {

    Address("address"),
    Transaction("transaction"),
    BlockHeight("block height"),
    OntId("ont id"),
    Contract("contract"),
    Token("token");

    private String des;

    SearchCategoryEnum(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
