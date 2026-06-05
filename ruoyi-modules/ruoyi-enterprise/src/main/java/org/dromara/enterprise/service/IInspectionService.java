package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.InspectionBo;
import org.dromara.enterprise.domain.vo.InspectionVo;

import java.util.List;

public interface IInspectionService {

    TableDataInfo<InspectionVo> selectPageInspectionList(InspectionBo bo, PageQuery pageQuery);

    List<InspectionVo> selectInspectionList(InspectionBo bo);

    InspectionVo selectInspectionById(Long id);

    int insertInspection(InspectionBo bo);

    int updateInspection(InspectionBo bo);

    int deleteInspectionByIds(List<Long> ids);
}
