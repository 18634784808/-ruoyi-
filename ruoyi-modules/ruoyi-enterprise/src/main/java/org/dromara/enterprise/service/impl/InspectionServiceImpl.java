package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.Inspection;
import org.dromara.enterprise.domain.bo.InspectionBo;
import org.dromara.enterprise.domain.vo.InspectionVo;
import org.dromara.enterprise.mapper.InspectionMapper;
import org.dromara.enterprise.service.IInspectionService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InspectionServiceImpl implements IInspectionService {

    private final InspectionMapper baseMapper;

    private LambdaQueryWrapper<Inspection> buildQueryWrapper(InspectionBo bo) {
        return new LambdaQueryWrapper<Inspection>()
            .like(StringUtils.isNotBlank(bo.getCategory()), Inspection::getCategory, bo.getCategory())
            .like(StringUtils.isNotBlank(bo.getInspectedUnit()), Inspection::getInspectedUnit, bo.getInspectedUnit())
            .like(StringUtils.isNotBlank(bo.getInspectionType()), Inspection::getInspectionType, bo.getInspectionType())
            .eq(StringUtils.isNotBlank(bo.getStatus()), Inspection::getStatus, bo.getStatus())
            .orderByDesc(Inspection::getCreateTime);
    }

    @Override
    public TableDataInfo<InspectionVo> selectPageInspectionList(InspectionBo bo, PageQuery pageQuery) {
        Page<InspectionVo> page = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(page);
    }

    @Override
    public List<InspectionVo> selectInspectionList(InspectionBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public InspectionVo selectInspectionById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public int insertInspection(InspectionBo bo) {
        Inspection inspection = MapstructUtils.convert(bo, Inspection.class);
        return baseMapper.insert(inspection);
    }

    @Override
    public int updateInspection(InspectionBo bo) {
        Inspection inspection = MapstructUtils.convert(bo, Inspection.class);
        return baseMapper.updateById(inspection);
    }

    @Override
    public int deleteInspectionByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }
}
