package com.github.ontio.mapper;

import com.github.ontio.model.dao.NodeAuthorizeInfo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface NodeAuthorizeInfoMapper extends Mapper<NodeAuthorizeInfo> {

    void batchInsert(List<NodeAuthorizeInfo> list);

}