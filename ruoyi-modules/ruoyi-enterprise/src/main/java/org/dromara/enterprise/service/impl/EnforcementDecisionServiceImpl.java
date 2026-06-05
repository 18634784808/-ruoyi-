package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.EnforcementDecision;
import org.dromara.enterprise.domain.bo.EnforcementDecisionBo;
import org.dromara.enterprise.domain.vo.EnforcementDecisionVo;
import org.dromara.enterprise.mapper.EnforcementDecisionMapper;
import org.dromara.enterprise.service.IEnforcementDecisionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EnforcementDecisionServiceImpl implements IEnforcementDecisionService {

    private final EnforcementDecisionMapper mapper;

    @Override
    public TableDataInfo<EnforcementDecisionVo> selectPageList(EnforcementDecisionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<EnforcementDecision> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(bo.getPartyName() != null, EnforcementDecision::getPartyName, bo.getPartyName());
        wrapper.orderByDesc(EnforcementDecision::getCreateTime);
        Page<EnforcementDecisionVo> page = mapper.selectVoPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(page);
    }

    @Override
    public List<EnforcementDecisionVo> selectList(EnforcementDecisionBo bo) {
        LambdaQueryWrapper<EnforcementDecision> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(EnforcementDecision::getCreateTime);
        return mapper.selectVoList(wrapper);
    }

    @Override
    public EnforcementDecisionVo selectById(Long id) {
        return mapper.selectVoById(id);
    }

    @Override
    @Transactional
    public int insert(EnforcementDecisionBo bo) {
        EnforcementDecision entity = MapstructUtils.convert(bo, EnforcementDecision.class);
        return mapper.insert(entity);
    }

    @Override
    @Transactional
    public int update(EnforcementDecisionBo bo) {
        EnforcementDecision entity = MapstructUtils.convert(bo, EnforcementDecision.class);
        return mapper.updateById(entity);
    }

    @Override
    @Transactional
    public int deleteByIds(List<Long> ids) {
        return mapper.deleteByIds(ids);
    }
}
