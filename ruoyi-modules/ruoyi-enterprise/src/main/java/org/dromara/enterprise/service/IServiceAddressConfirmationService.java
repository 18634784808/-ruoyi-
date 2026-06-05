package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.ServiceAddressConfirmationBo;
import org.dromara.enterprise.domain.vo.ServiceAddressConfirmationVo;

import java.util.List;

public interface IServiceAddressConfirmationService {

    TableDataInfo<ServiceAddressConfirmationVo> selectPageList(ServiceAddressConfirmationBo bo, PageQuery pageQuery);

    List<ServiceAddressConfirmationVo> selectList(ServiceAddressConfirmationBo bo);

    ServiceAddressConfirmationVo selectById(Long id);

    int insert(ServiceAddressConfirmationBo bo);

    int update(ServiceAddressConfirmationBo bo);

    int deleteByIds(List<Long> ids);
}
