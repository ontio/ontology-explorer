package com.github.ontio.mapper;

import com.github.ontio.model.dao.Ranking;
import com.github.ontio.model.dto.ranking.AddressRankingDto;
import com.github.ontio.model.dto.ranking.TokenRankingDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RankingMapper extends Mapper<Ranking> {

	/**
	 * 排名分组：地址排名
	 */
	short RANKING_GROUP_ADDRESS = 1;
	/**
	 * 排名分组：Token 排名
	 */
	short RANKING_GROUP_TOKEN = 2;

	List<AddressRankingDto> findAddressRankings(@Param("rankingIds") List<Short> rankingIds, @Param("duration") short duration);

	List<TokenRankingDto> findTokenRankings(@Param("rankingIds") List<Short> rankingIds, @Param("duration") short duration);

}