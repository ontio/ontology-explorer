package com.github.ontio.mapper;

import com.github.ontio.model.dto.GovernanceInfoDto;
import com.github.ontio.model.dto.StakingRewardsDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LiuQi
 */
@Repository
public interface GovernanceMapper {

    List<GovernanceInfoDto> findGovernanceInfo(@Param("pubKey") String pubKey, @Param("start") int start, @Param("size") int size);

    int countGovernanceInfo(@Param("pubKey") String pubKey);

    List<GovernanceInfoDto> getStakingInfoByAddress(@Param("address") String address);

    List<String> getStakingAddressByPublicKey(@Param("pubKey") String pubKey);

    List<String> getAllStakingAddress();

    List<StakingRewardsDto> getStakingRewardsByAddress(String address, String publicKey, Integer round);
}
