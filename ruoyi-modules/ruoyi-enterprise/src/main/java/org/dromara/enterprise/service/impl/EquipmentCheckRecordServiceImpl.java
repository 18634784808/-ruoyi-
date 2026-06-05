package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.EquipmentCheckRecord;
import org.dromara.enterprise.domain.bo.EquipmentCheckRecordBo;
import org.dromara.enterprise.domain.vo.EquipmentCheckRecordVo;
import org.dromara.enterprise.mapper.EquipmentCheckRecordMapper;
import org.dromara.enterprise.service.IEquipmentCheckRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EquipmentCheckRecordServiceImpl implements IEquipmentCheckRecordService {

    private final EquipmentCheckRecordMapper mapper;

    @Override
    public TableDataInfo<EquipmentCheckRecordVo> selectPageList(EquipmentCheckRecordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<EquipmentCheckRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(bo.getUnitName() != null, EquipmentCheckRecord::getUnitName, bo.getUnitName());
        wrapper.orderByDesc(EquipmentCheckRecord::getCreateTime);
        Page<EquipmentCheckRecordVo> page = mapper.selectVoPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(page);
    }

    @Override
    public List<EquipmentCheckRecordVo> selectList(EquipmentCheckRecordBo bo) {
        LambdaQueryWrapper<EquipmentCheckRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(EquipmentCheckRecord::getCreateTime);
        return mapper.selectVoList(wrapper);
    }

    @Override
    public EquipmentCheckRecordVo selectById(Long id) {
        return mapper.selectVoById(id);
    }

    @Override
    @Transactional
    public int insert(EquipmentCheckRecordBo bo) {
        EquipmentCheckRecord entity = MapstructUtils.convert(bo, EquipmentCheckRecord.class);
        return mapper.insert(entity);
    }

    @Override
    @Transactional
    public int update(EquipmentCheckRecordBo bo) {
        EquipmentCheckRecord entity = MapstructUtils.convert(bo, EquipmentCheckRecord.class);
        return mapper.updateById(entity);
    }

    @Override
    @Transactional
    public int deleteByIds(List<Long> ids) {
        return mapper.deleteByIds(ids);
    }
}
