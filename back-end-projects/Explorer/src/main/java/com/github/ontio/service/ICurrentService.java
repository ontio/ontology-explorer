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


package com.github.ontio.service;

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.paramBean.Result;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/2/27
 */
public interface ICurrentService {

    /**
     * query current information
     *
     * @return
     */
    Result querySummaryInfo();




    Result registerOep4Info(JSONObject reqObj);


    Result queryDailyInfo(long startTime, long endTime);


    Result queryMarketingInfo();

}
