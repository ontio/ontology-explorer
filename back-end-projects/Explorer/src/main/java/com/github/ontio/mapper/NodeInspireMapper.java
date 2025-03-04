package com.github.ontio.mapper;

import com.github.ontio.model.dao.NodeInspire;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


@Repository
public interface NodeInspireMapper extends Mapper<NodeInspire> {

    List<NodeInspire> selectNodesInspire(int start, Integer pageSize);

    int selectNodesInspireCount();

    String selectBestApr();

    Map<String,String> selectBestAprAndNode();
}