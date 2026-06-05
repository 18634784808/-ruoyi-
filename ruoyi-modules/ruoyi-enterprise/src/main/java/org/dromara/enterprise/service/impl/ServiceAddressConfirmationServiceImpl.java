package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.ServiceAddressConfirmation;
import org.dromara.enterprise.domain.bo.ServiceAddressConfirmationBo;
import org.dromara.enterprise.domain.vo.ServiceAddressConfirmationVo;
import org.dromara.enterprise.mapper.ServiceAddressConfirmationMapper;
import org.dromara.enterprise.service.IServiceAddressConfirmationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ServiceAddressConfirmationServiceImpl implements IServiceAddressConfirmationService {

    private final ServiceAddressConfirmationMapper mapper;

    @Override
    public TableDataInfo<ServiceAddressConfirmationVo> selectPageList(ServiceAddressConfirmationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ServiceAddressConfirmation> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(bo.getRecipientName() != null, ServiceAddressConfirmation::getRecipientName, bo.getRecipientName());
        wrapper.orderByDesc(ServiceAddressConfirmation::getCreateTime);
        Page<ServiceAddressConfirmationVo> page = mapper.selectVoPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(page);
    }

    @Override
    public List<ServiceAddressConfirmationVo> selectList(ServiceAddressConfirmationBo bo) {
        LambdaQueryWrapper<ServiceAddressConfirmation> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ServiceAddressConfirmation::getCreateTime);
        return mapper.selectVoList(wrapper);
    }

    @Override
    public ServiceAddressConfirmationVo selectById(Long id) {
        return mapper.selectVoById(id);
    }

    @Override
    @Transactional
    public int insert(ServiceAddressConfirmationBo bo) {
        ServiceAddressConfirmation entity = MapstructUtils.convert(bo, ServiceAddressConfirmation.class);
        return mapper.insert(entity);
    }

    @Override
    @Transactional
    public int update(ServiceAddressConfirmationBo bo) {
        ServiceAddressConfirmation entity = MapstructUtils.convert(bo, ServiceAddressConfirmation.class);
        return mapper.updateById(entity);
    }

    @Override
    @Transactional
    public int deleteByIds(List<Long> ids) {
        return mapper.deleteByIds(ids);
    }
}
