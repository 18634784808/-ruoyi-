package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.FacilityChecklistBo;
import org.dromara.enterprise.domain.vo.FacilityChecklistVo;

import java.util.List;

public interface IFacilityChecklistService {

    TableDataInfo<FacilityChecklistVo> selectPageList(FacilityChecklistBo bo, PageQuery pageQuery);

    List<FacilityChecklistVo> selectList(FacilityChecklistBo bo);

    FacilityChecklistVo selectById(Long id);

    int insert(FacilityChecklistBo bo);

    int update(FacilityChecklistBo bo);

    int deleteByIds(List<Long> ids);
}
