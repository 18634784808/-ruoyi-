package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.InspectorBo;
import org.dromara.enterprise.domain.vo.InspectorVo;

import java.util.List;

public interface IInspectorService {

    TableDataInfo<InspectorVo> selectPageInspectorList(InspectorBo bo, PageQuery pageQuery);

    List<InspectorVo> selectInspectorList(InspectorBo bo);

    InspectorVo selectInspectorById(Long id);

    int insertInspector(InspectorBo bo);

    int updateInspector(InspectorBo bo);

    int deleteInspectorByIds(List<Long> ids);
}
