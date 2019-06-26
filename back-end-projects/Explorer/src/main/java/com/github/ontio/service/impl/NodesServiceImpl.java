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

package com.github.ontio.service.impl;

import com.github.ontio.mapper.CandidateNodeSummaryMapper;
import com.github.ontio.model.dao.CandidateNodeSummary;
import com.github.ontio.service.INodesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("NodesServiceImpl")
public class NodesServiceImpl implements INodesService {

    private final CandidateNodeSummaryMapper candidateNodeSummaryMapper;

    @Autowired
    public NodesServiceImpl(CandidateNodeSummaryMapper candidateNodeSummaryMapper) {
        this.candidateNodeSummaryMapper = candidateNodeSummaryMapper;
    }

    @Override
    public List<CandidateNodeSummary> getCandidateNodesInfo() {
        return candidateNodeSummaryMapper.selectAll();
    }

}
