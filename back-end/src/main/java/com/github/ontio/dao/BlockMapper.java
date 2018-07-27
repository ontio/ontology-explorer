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

import com.github.ontio.model.Block;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "BlockMapper")
public interface BlockMapper {
    int deleteByPrimaryKey(Integer height);

    int insert(Block record);

    int insertSelective(Block record);

    Block selectByPrimaryKey(Integer height);

    int updateByPrimaryKeySelective(Block record);

    int updateByPrimaryKey(Block record);

    List<Map> selectBlockByPage(int start, int pageSize);

    Map selectBlockByHash(String hash);

    Map selectBlockByHeight(int height);

    void updateNextBlockHash(String blockHash, int height);

    List<Map> selectHeightAndTime(int amount);

}