package com.github.ontio.model.dao;

import javax.persistence.*;

@Table(name = "tbl_config")
public class Config {
    @Id
    @GeneratedValue(generator = "JDBC")
    private String field;

    private String value;

    /**
     * @return field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field
     */
    public void setField(String field) {
        this.field = field == null ? null : field.trim();
    }

    /**
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }
}