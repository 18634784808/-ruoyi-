package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.IssueBo;
import org.dromara.enterprise.domain.vo.IssueVo;

import java.util.List;

public interface IIssueService {

    TableDataInfo<IssueVo> selectPageIssueList(IssueBo bo, PageQuery pageQuery);

    List<IssueVo> selectIssueList(IssueBo bo);

    IssueVo selectIssueById(Long id);

    int insertIssue(IssueBo bo);

    int updateIssue(IssueBo bo);

    int deleteIssueByIds(List<Long> ids);
}
