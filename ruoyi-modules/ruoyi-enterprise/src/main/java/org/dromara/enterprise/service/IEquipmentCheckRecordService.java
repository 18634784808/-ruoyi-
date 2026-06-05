package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.EquipmentCheckRecordBo;
import org.dromara.enterprise.domain.vo.EquipmentCheckRecordVo;

import java.util.List;

public interface IEquipmentCheckRecordService {

    TableDataInfo<EquipmentCheckRecordVo> selectPageList(EquipmentCheckRecordBo bo, PageQuery pageQuery);

    List<EquipmentCheckRecordVo> selectList(EquipmentCheckRecordBo bo);

    EquipmentCheckRecordVo selectById(Long id);

    int insert(EquipmentCheckRecordBo bo);

    int update(EquipmentCheckRecordBo bo);

    int deleteByIds(List<Long> ids);
}
