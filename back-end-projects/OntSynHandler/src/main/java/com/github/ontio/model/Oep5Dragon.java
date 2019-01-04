package com.github.ontio.model;

public class Oep5Dragon {
    private Long id;

    private String contract;

    private String assertname;

    private String jsonurl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract == null ? null : contract.trim();
    }

    public String getAssertname() {
        return assertname;
    }

    public void setAssertname(String assertname) {
        this.assertname = assertname == null ? null : assertname.trim();
    }

    public String getJsonurl() {
        return jsonurl;
    }

    public void setJsonurl(String jsonurl) {
        this.jsonurl = jsonurl == null ? null : jsonurl.trim();
    }
}