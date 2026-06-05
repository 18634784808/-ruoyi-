package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.Ledger;
import org.dromara.enterprise.domain.bo.LedgerBo;
import org.dromara.enterprise.domain.vo.LedgerVo;
import org.dromara.enterprise.mapper.LedgerMapper;
import org.dromara.enterprise.service.ILedgerService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LedgerServiceImpl implements ILedgerService {

    private final LedgerMapper baseMapper;

    private LambdaQueryWrapper<Ledger> buildQueryWrapper(LedgerBo bo) {
        return new LambdaQueryWrapper<Ledger>()
            .like(StringUtils.isNotBlank(bo.getInspectedUnit()), Ledger::getInspectedUnit, bo.getInspectedUnit())
            .like(StringUtils.isNotBlank(bo.getLegalPerson()), Ledger::getLegalPerson, bo.getLegalPerson())
            .eq(StringUtils.isNotBlank(bo.getRectificationProgress()), Ledger::getRectificationProgress, bo.getRectificationProgress())
            .eq(StringUtils.isNotBlank(bo.getInspectorCategory()), Ledger::getInspectorCategory, bo.getInspectorCategory())
            .orderByDesc(Ledger::getCreateTime);
    }

    @Override
    public TableDataInfo<LedgerVo> selectPageLedgerList(LedgerBo bo, PageQuery pageQuery) {
        Page<LedgerVo> page = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(page);
    }

    @Override
    public List<LedgerVo> selectLedgerList(LedgerBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public LedgerVo selectLedgerById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public int insertLedger(LedgerBo bo) {
        Ledger ledger = MapstructUtils.convert(bo, Ledger.class);
        return baseMapper.insert(ledger);
    }

    @Override
    public int updateLedger(LedgerBo bo) {
        Ledger ledger = MapstructUtils.convert(bo, Ledger.class);
        return baseMapper.updateById(ledger);
    }

    @Override
    public int deleteLedgerByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }
}
