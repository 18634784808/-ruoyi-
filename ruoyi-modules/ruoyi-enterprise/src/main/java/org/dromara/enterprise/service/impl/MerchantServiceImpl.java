package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.Merchant;
import org.dromara.enterprise.domain.bo.MerchantBo;
import org.dromara.enterprise.domain.vo.MerchantVo;
import org.dromara.enterprise.mapper.MerchantMapper;
import org.dromara.enterprise.service.IMerchantService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MerchantServiceImpl implements IMerchantService {

    private final MerchantMapper baseMapper;

    private LambdaQueryWrapper<Merchant> buildQueryWrapper(MerchantBo bo) {
        return new LambdaQueryWrapper<Merchant>()
            .like(StringUtils.isNotBlank(bo.getCategory()), Merchant::getCategory, bo.getCategory())
            .like(StringUtils.isNotBlank(bo.getUnitName()), Merchant::getUnitName, bo.getUnitName())
            .like(StringUtils.isNotBlank(bo.getLegalPerson()), Merchant::getLegalPerson, bo.getLegalPerson())
            .eq(StringUtils.isNotBlank(bo.getStatus()), Merchant::getStatus, bo.getStatus())
            .orderByDesc(Merchant::getCreateTime);
    }

    @Override
    public TableDataInfo<MerchantVo> selectPageMerchantList(MerchantBo bo, PageQuery pageQuery) {
        Page<MerchantVo> page = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(page);
    }

    @Override
    public List<MerchantVo> selectMerchantList(MerchantBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public MerchantVo selectMerchantById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public MerchantVo selectMerchantByName(String name) {
        List<MerchantVo> list = baseMapper.selectVoList(new LambdaQueryWrapper<Merchant>().eq(Merchant::getUnitName, name));
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public int insertMerchant(MerchantBo bo) {
        Merchant merchant = MapstructUtils.convert(bo, Merchant.class);
        return baseMapper.insert(merchant);
    }

    @Override
    public int updateMerchant(MerchantBo bo) {
        Merchant merchant = MapstructUtils.convert(bo, Merchant.class);
        return baseMapper.updateById(merchant);
    }

    @Override
    public int deleteMerchantByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }
}
