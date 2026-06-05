package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.Signature;
import org.dromara.enterprise.domain.bo.SignatureBo;
import org.dromara.enterprise.domain.vo.SignatureVo;
import org.dromara.enterprise.mapper.SignatureMapper;
import org.dromara.enterprise.service.ISignatureService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SignatureServiceImpl implements ISignatureService {

    private final SignatureMapper baseMapper;

    private LambdaQueryWrapper<Signature> buildQueryWrapper(SignatureBo bo) {
        return new LambdaQueryWrapper<Signature>()
            .like(StringUtils.isNotBlank(bo.getName()), Signature::getName, bo.getName())
            .like(StringUtils.isNotBlank(bo.getDepartment()), Signature::getDepartment, bo.getDepartment())
            .eq(StringUtils.isNotBlank(bo.getStatus()), Signature::getStatus, bo.getStatus())
            .orderByDesc(Signature::getCreateTime);
    }

    @Override
    public TableDataInfo<SignatureVo> selectPageSignatureList(SignatureBo bo, PageQuery pageQuery) {
        Page<SignatureVo> page = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(page);
    }

    @Override
    public List<SignatureVo> selectSignatureList(SignatureBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public SignatureVo selectSignatureById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public int insertSignature(SignatureBo bo) {
        Signature signature = MapstructUtils.convert(bo, Signature.class);
        return baseMapper.insert(signature);
    }

    @Override
    public int updateSignature(SignatureBo bo) {
        Signature signature = MapstructUtils.convert(bo, Signature.class);
        return baseMapper.updateById(signature);
    }

    @Override
    public int deleteSignatureByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }
}
