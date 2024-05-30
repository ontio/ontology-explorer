package com.github.ontio.model.dto;

import com.github.ontio.common.Helper;
import com.github.ontio.io.BinaryReader;
import com.github.ontio.io.BinaryWriter;
import com.github.ontio.io.Serializable;

import java.io.IOException;
import java.math.BigInteger;

public class PromisePosInfo implements Serializable {
    public String peerPubkey;
    public BigInteger promisePos;

    public PromisePosInfo() {
    }

    public void deserialize(BinaryReader reader) throws IOException {
        this.peerPubkey = reader.readVarString();
        this.promisePos = Helper.BigIntFromNeoBytes(reader.readVarBytes());
    }

    public void serialize(BinaryWriter writer) throws IOException {
    }
}