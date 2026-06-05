package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.EquipmentSafetyDirectiveBo;
import org.dromara.enterprise.domain.vo.EquipmentSafetyDirectiveVo;

import java.util.List;

public interface IEquipmentSafetyDirectiveService {

    TableDataInfo<EquipmentSafetyDirectiveVo> selectPageList(EquipmentSafetyDirectiveBo bo, PageQuery pageQuery);

    List<EquipmentSafetyDirectiveVo> selectList(EquipmentSafetyDirectiveBo bo);

    EquipmentSafetyDirectiveVo selectById(Long id);

    int insert(EquipmentSafetyDirectiveBo bo);

    int update(EquipmentSafetyDirectiveBo bo);

    int deleteByIds(List<Long> ids);
}
