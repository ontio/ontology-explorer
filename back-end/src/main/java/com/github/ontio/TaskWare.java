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


package com.github.ontio;

import com.github.ontio.task.BlockHandleTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/3/13
 */
@Component
public class TaskWare {

    @Autowired
    private ApplicationContextProvider applicationContextProvider;

    @PostConstruct
    public void init() {

        BlockHandleTask blockHandleTask = applicationContextProvider.getBean("BlockHandleTask", BlockHandleTask.class);
        blockHandleTask.start();

    }


}