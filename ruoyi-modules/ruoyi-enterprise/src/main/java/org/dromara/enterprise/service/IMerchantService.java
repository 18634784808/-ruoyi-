package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.MerchantBo;
import org.dromara.enterprise.domain.vo.MerchantVo;

import java.util.List;

public interface IMerchantService {

    TableDataInfo<MerchantVo> selectPageMerchantList(MerchantBo bo, PageQuery pageQuery);

    List<MerchantVo> selectMerchantList(MerchantBo bo);

    MerchantVo selectMerchantById(Long id);

    MerchantVo selectMerchantByName(String name);

    int insertMerchant(MerchantBo bo);

    int updateMerchant(MerchantBo bo);

    int deleteMerchantByIds(List<Long> ids);
}
