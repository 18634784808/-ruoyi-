package org.dromara.enterprise.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.enterprise.domain.bo.IssueBo;
import org.dromara.enterprise.domain.vo.IssueVo;
import org.dromara.enterprise.service.IIssueService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/issue")
public class IssueController extends BaseController {

    private final IIssueService issueService;

    @SaCheckPermission("business:table:issue:list")
    @GetMapping("/list")
    public TableDataInfo<IssueVo> list(IssueBo bo, PageQuery pageQuery) {
        return issueService.selectPageIssueList(bo, pageQuery);
    }

    @Log(title = "问题清单管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("business:table:issue:export")
    @PostMapping("/export")
    public void export(IssueBo bo, HttpServletResponse response) {
        List<IssueVo> list = issueService.selectIssueList(bo);
        ExcelUtil.exportExcel(list, "问题清单数据", IssueVo.class, response);
    }

    @SaCheckPermission("business:table:issue:query")
    @GetMapping("/{id}")
    public R<IssueVo> getInfo(@PathVariable Long id) {
        return R.ok(issueService.selectIssueById(id));
    }

    @SaCheckPermission("business:table:issue:add")
    @Log(title = "问题清单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody IssueBo bo) {
        return toAjax(issueService.insertIssue(bo));
    }

    @SaCheckPermission("business:table:issue:edit")
    @Log(title = "问题清单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody IssueBo bo) {
        return toAjax(issueService.updateIssue(bo));
    }

    @SaCheckPermission("business:table:issue:remove")
    @Log(title = "问题清单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(issueService.deleteIssueByIds(Arrays.asList(ids)));
    }
}
