package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.BizDictBo;
import org.dromara.enterprise.domain.vo.BizDictVo;

import java.util.List;

public interface IBizDictService {

    TableDataInfo<BizDictVo> selectPageBizDictList(BizDictBo bo, PageQuery pageQuery);

    List<BizDictVo> selectBizDictList(BizDictBo bo);

    BizDictVo selectBizDictById(Long id);

    int insertBizDict(BizDictBo bo);

    int updateBizDict(BizDictBo bo);

    int deleteBizDictByIds(List<Long> ids);
}
