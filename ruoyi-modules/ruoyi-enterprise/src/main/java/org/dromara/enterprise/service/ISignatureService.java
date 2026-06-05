package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.SignatureBo;
import org.dromara.enterprise.domain.vo.SignatureVo;

import java.util.List;

public interface ISignatureService {

    TableDataInfo<SignatureVo> selectPageSignatureList(SignatureBo bo, PageQuery pageQuery);

    List<SignatureVo> selectSignatureList(SignatureBo bo);

    SignatureVo selectSignatureById(Long id);

    int insertSignature(SignatureBo bo);

    int updateSignature(SignatureBo bo);

    int deleteSignatureByIds(List<Long> ids);
}
