package com.github.ontio.mapper;

import com.github.ontio.model.dao.NodeInfoOnChain;
import com.github.ontio.model.dto.NodeInfoOnChainDto;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface NodeInfoOnChainMapper extends Mapper<NodeInfoOnChain> {
    NodeInfoOnChainDto selectByPublicKey(String publicKey);
}