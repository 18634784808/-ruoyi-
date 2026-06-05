package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.EnforcementDecisionBo;
import org.dromara.enterprise.domain.vo.EnforcementDecisionVo;

import java.util.List;

public interface IEnforcementDecisionService {

    TableDataInfo<EnforcementDecisionVo> selectPageList(EnforcementDecisionBo bo, PageQuery pageQuery);

    List<EnforcementDecisionVo> selectList(EnforcementDecisionBo bo);

    EnforcementDecisionVo selectById(Long id);

    int insert(EnforcementDecisionBo bo);

    int update(EnforcementDecisionBo bo);

    int deleteByIds(List<Long> ids);
}
