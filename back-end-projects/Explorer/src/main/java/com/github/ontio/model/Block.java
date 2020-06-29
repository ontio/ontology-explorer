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

public class Block {
    private Integer height;

    private String hash;

    private String prevblock;

    private String nextblock;

    private String txnsroot;

    private Integer blocktime;

    private String consensusdata;

    private String bookkeeper;

    private Integer txnnum;

    private Integer blocksize;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash == null ? null : hash.trim();
    }

    public String getPrevblock() {
        return prevblock;
    }

    public void setPrevblock(String prevblock) {
        this.prevblock = prevblock == null ? null : prevblock.trim();
    }

    public String getNextblock() {
        return nextblock;
    }

    public void setNextblock(String nextblock) {
        this.nextblock = nextblock == null ? null : nextblock.trim();
    }

    public String getTxnsroot() {
        return txnsroot;
    }

    public void setTxnsroot(String txnsroot) {
        this.txnsroot = txnsroot == null ? null : txnsroot.trim();
    }

    public Integer getBlocktime() {
        return blocktime;
    }

    public void setBlocktime(Integer blocktime) {
        this.blocktime = blocktime;
    }

    public String getConsensusdata() {
        return consensusdata;
    }

    public void setConsensusdata(String consensusdata) {
        this.consensusdata = consensusdata == null ? null : consensusdata.trim();
    }

    public String getBookkeeper() {
        return bookkeeper;
    }

    public void setBookkeeper(String bookkeeper) {
        this.bookkeeper = bookkeeper == null ? null : bookkeeper.trim();
    }

    public Integer getTxnnum() {
        return txnnum;
    }

    public void setTxnnum(Integer txnnum) {
        this.txnnum = txnnum;
    }

    public Integer getBlocksize() {
        return blocksize;
    }

    public void setBlocksize(Integer blocksize) {
        this.blocksize = blocksize;
    }
}