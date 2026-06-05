package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.Issue;
import org.dromara.enterprise.domain.bo.IssueBo;
import org.dromara.enterprise.domain.vo.IssueVo;
import org.dromara.enterprise.mapper.IssueMapper;
import org.dromara.enterprise.service.IIssueService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IssueServiceImpl implements IIssueService {

    private final IssueMapper baseMapper;

    private LambdaQueryWrapper<Issue> buildQueryWrapper(IssueBo bo) {
        return new LambdaQueryWrapper<Issue>()
            .like(StringUtils.isNotBlank(bo.getIssueOption()), Issue::getIssueOption, bo.getIssueOption())
            .like(StringUtils.isNotBlank(bo.getAutoIncrementCode()), Issue::getAutoIncrementCode, bo.getAutoIncrementCode())
            .eq(StringUtils.isNotBlank(bo.getSeverity()), Issue::getSeverity, bo.getSeverity())
            .eq(StringUtils.isNotBlank(bo.getStatus()), Issue::getStatus, bo.getStatus())
            .orderByDesc(Issue::getCreateTime);
    }

    @Override
    public TableDataInfo<IssueVo> selectPageIssueList(IssueBo bo, PageQuery pageQuery) {
        Page<IssueVo> page = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(page);
    }

    @Override
    public List<IssueVo> selectIssueList(IssueBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public IssueVo selectIssueById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public int insertIssue(IssueBo bo) {
        Issue issue = MapstructUtils.convert(bo, Issue.class);
        return baseMapper.insert(issue);
    }

    @Override
    public int updateIssue(IssueBo bo) {
        Issue issue = MapstructUtils.convert(bo, Issue.class);
        return baseMapper.updateById(issue);
    }

    @Override
    public int deleteIssueByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }
}
