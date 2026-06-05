package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.LedgerBo;
import org.dromara.enterprise.domain.vo.LedgerVo;

import java.util.List;

public interface ILedgerService {

    TableDataInfo<LedgerVo> selectPageLedgerList(LedgerBo bo, PageQuery pageQuery);

    List<LedgerVo> selectLedgerList(LedgerBo bo);

    LedgerVo selectLedgerById(Long id);

    int insertLedger(LedgerBo bo);

    int updateLedger(LedgerBo bo);

    int deleteLedgerByIds(List<Long> ids);
}
