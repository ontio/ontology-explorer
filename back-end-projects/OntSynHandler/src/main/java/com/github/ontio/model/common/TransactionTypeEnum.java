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


package com.github.ontio.model.common;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
public enum TransactionTypeEnum {

    /**
     * DeployCodeTransaction
     */
    DEPLOYCODE(208),

    /**
     * InvokeCodeTransaction
     */
    INVOKECODE(209);


    private int type;

    TransactionTypeEnum(int type) {
        this.type = type;
    }

    public int type() {
        return this.type;
    }


}
