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


package com.github.ontio.dao;

import com.github.ontio.model.OntId;

import java.util.List;
import java.util.Map;

public interface OntIdMapper {
    int deleteByPrimaryKey(String txnhash);

    int insert(OntId record);

    int insertSelective(OntId record);

    OntId selectByPrimaryKey(String txnhash);

    int updateByPrimaryKeySelective(OntId record);

    int updateByPrimaryKey(OntId record);

    List<Map> selectOntIdByPage(int start, int pageSize);

    int selectOntIdCount();

    List<Map> selectOntId(Map<String,Object> param);

    int selectCountByOntId(String ontId);
}