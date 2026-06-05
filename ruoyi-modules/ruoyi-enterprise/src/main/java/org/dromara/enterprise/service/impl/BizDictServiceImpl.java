package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.BizDict;
import org.dromara.enterprise.domain.bo.BizDictBo;
import org.dromara.enterprise.domain.vo.BizDictVo;
import org.dromara.enterprise.mapper.BizDictMapper;
import org.dromara.enterprise.service.IBizDictService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BizDictServiceImpl implements IBizDictService {

    private final BizDictMapper baseMapper;

    private LambdaQueryWrapper<BizDict> buildQueryWrapper(BizDictBo bo) {
        return new LambdaQueryWrapper<BizDict>()
            .eq(StringUtils.isNotBlank(bo.getDictType()), BizDict::getDictType, bo.getDictType())
            .like(StringUtils.isNotBlank(bo.getLabel()), BizDict::getLabel, bo.getLabel())
            .eq(StringUtils.isNotBlank(bo.getStatus()), BizDict::getStatus, bo.getStatus())
            .orderByAsc(BizDict::getDictType)
            .orderByAsc(BizDict::getSort)
            .orderByAsc(BizDict::getId);
    }

    @Override
    public TableDataInfo<BizDictVo> selectPageBizDictList(BizDictBo bo, PageQuery pageQuery) {
        Page<BizDictVo> page = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(page);
    }

    @Override
    public List<BizDictVo> selectBizDictList(BizDictBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public BizDictVo selectBizDictById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public int insertBizDict(BizDictBo bo) {
        BizDict entity = MapstructUtils.convert(bo, BizDict.class);
        return baseMapper.insert(entity);
    }

    @Override
    public int updateBizDict(BizDictBo bo) {
        BizDict entity = MapstructUtils.convert(bo, BizDict.class);
        return baseMapper.updateById(entity);
    }

    @Override
    public int deleteBizDictByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }
}
