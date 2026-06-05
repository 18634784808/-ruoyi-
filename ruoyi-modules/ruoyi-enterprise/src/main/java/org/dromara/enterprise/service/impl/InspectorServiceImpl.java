package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.Inspector;
import org.dromara.enterprise.domain.bo.InspectorBo;
import org.dromara.enterprise.domain.vo.InspectorVo;
import org.dromara.enterprise.mapper.InspectorMapper;
import org.dromara.enterprise.service.IInspectorService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InspectorServiceImpl implements IInspectorService {

    private final InspectorMapper baseMapper;

    private LambdaQueryWrapper<Inspector> buildQueryWrapper(InspectorBo bo) {
        return new LambdaQueryWrapper<Inspector>()
            .like(StringUtils.isNotBlank(bo.getName()), Inspector::getName, bo.getName())
            .like(StringUtils.isNotBlank(bo.getDepartment()), Inspector::getDepartment, bo.getDepartment())
            .eq(StringUtils.isNotBlank(bo.getStatus()), Inspector::getStatus, bo.getStatus())
            .orderByAsc(Inspector::getId);
    }

    @Override
    public TableDataInfo<InspectorVo> selectPageInspectorList(InspectorBo bo, PageQuery pageQuery) {
        Page<InspectorVo> page = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(page);
    }

    @Override
    public List<InspectorVo> selectInspectorList(InspectorBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public InspectorVo selectInspectorById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public int insertInspector(InspectorBo bo) {
        Inspector inspector = MapstructUtils.convert(bo, Inspector.class);
        return baseMapper.insert(inspector);
    }

    @Override
    public int updateInspector(InspectorBo bo) {
        Inspector inspector = MapstructUtils.convert(bo, Inspector.class);
        return baseMapper.updateById(inspector);
    }

    @Override
    public int deleteInspectorByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }
}
