package com.github.ontio.mapper;

import com.github.ontio.model.dao.Erc20;
import com.github.ontio.model.dao.Erc20TxDetail;
import com.github.ontio.model.dao.Oep4;
import com.github.ontio.model.dto.Erc20DetailDto;
import com.github.ontio.model.dto.Oep4DetailDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface Erc20Mapper extends Mapper<Erc20> {

    List<Erc20DetailDto> selectErc20Tokens(@Param("ascending") List<String> ascending, @Param("descending") List<String> descending);


    Erc20DetailDto selectErc20TokenDetail(@Param("contractHash") String contractHash);
}