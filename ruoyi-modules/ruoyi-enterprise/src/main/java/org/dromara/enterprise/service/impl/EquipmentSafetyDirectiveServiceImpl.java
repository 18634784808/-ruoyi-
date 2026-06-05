package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.EquipmentSafetyDirective;
import org.dromara.enterprise.domain.bo.EquipmentSafetyDirectiveBo;
import org.dromara.enterprise.domain.vo.EquipmentSafetyDirectiveVo;
import org.dromara.enterprise.mapper.EquipmentSafetyDirectiveMapper;
import org.dromara.enterprise.service.IEquipmentSafetyDirectiveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EquipmentSafetyDirectiveServiceImpl implements IEquipmentSafetyDirectiveService {

    private final EquipmentSafetyDirectiveMapper mapper;

    @Override
    public TableDataInfo<EquipmentSafetyDirectiveVo> selectPageList(EquipmentSafetyDirectiveBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<EquipmentSafetyDirective> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(bo.getInspectedUnit() != null, EquipmentSafetyDirective::getInspectedUnit, bo.getInspectedUnit());
        wrapper.orderByDesc(EquipmentSafetyDirective::getCreateTime);
        Page<EquipmentSafetyDirectiveVo> page = mapper.selectVoPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(page);
    }

    @Override
    public List<EquipmentSafetyDirectiveVo> selectList(EquipmentSafetyDirectiveBo bo) {
        LambdaQueryWrapper<EquipmentSafetyDirective> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(EquipmentSafetyDirective::getCreateTime);
        return mapper.selectVoList(wrapper);
    }

    @Override
    public EquipmentSafetyDirectiveVo selectById(Long id) {
        return mapper.selectVoById(id);
    }

    @Override
    @Transactional
    public int insert(EquipmentSafetyDirectiveBo bo) {
        EquipmentSafetyDirective entity = MapstructUtils.convert(bo, EquipmentSafetyDirective.class);
        return mapper.insert(entity);
    }

    @Override
    @Transactional
    public int update(EquipmentSafetyDirectiveBo bo) {
        EquipmentSafetyDirective entity = MapstructUtils.convert(bo, EquipmentSafetyDirective.class);
        return mapper.updateById(entity);
    }

    @Override
    @Transactional
    public int deleteByIds(List<Long> ids) {
        return mapper.deleteByIds(ids);
    }
}
