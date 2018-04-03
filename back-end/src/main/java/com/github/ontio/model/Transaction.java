/*
 * Copyright (C) 2018 The ontology Authors
 * This file is part of The ontology library.
 *
 * The ontology is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ontology is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with The ontology.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.github.ontio.model;

public class Transaction {
    private String txnhash;

    private Integer height;

    private Integer txntype;

    private Integer txntime;

    private String description;

    private Integer blockindex;

    private Integer confirmflag;

    private Long fee;

    public String getTxnhash() {
        return txnhash;
    }

    public void setTxnhash(String txnhash) {
        this.txnhash = txnhash == null ? null : txnhash.trim();
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getTxntype() {
        return txntype;
    }

    public void setTxntype(Integer txntype) {
        this.txntype = txntype;
    }

    public Integer getTxntime() {
        return txntime;
    }

    public void setTxntime(Integer txntime) {
        this.txntime = txntime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getBlockindex() {
        return blockindex;
    }

    public void setBlockindex(Integer blockindex) {
        this.blockindex = blockindex;
    }

    public Integer getConfirmflag() {
        return confirmflag;
    }

    public void setConfirmflag(Integer confirmflag) {
        this.confirmflag = confirmflag;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }
}