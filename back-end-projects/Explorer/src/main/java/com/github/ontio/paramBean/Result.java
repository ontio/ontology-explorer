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



package com.github.ontio.paramBean;

/**
 * @author zhouq
 * @date 2018/2/27
 */
public class Result {

    public String Action;

    public Long Error;

    public String Desc;

    public String Version;

    public Object Result;

    @Override
    public String toString() {
        return "Result{" +
                "Action='" + Action + '\'' +
                ", Error=" + Error +
                ", Desc='" + Desc + '\'' +
                ", Version='" + Version + '\'' +
                ", Result=" + Result +
                '}';
    }
}
