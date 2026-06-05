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
import org.dromara.enterprise.domain.bo.InquiryNotesBo;
import org.dromara.enterprise.domain.vo.InquiryNotesVo;
import org.dromara.enterprise.service.IInquiryNotesService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/inquiry-notes")
public class InquiryNotesController extends BaseController {

    private final IInquiryNotesService inquiryNotesService;

    @SaCheckPermission("business:table:inquiry-notes:list")
    @GetMapping("/list")
    public TableDataInfo<InquiryNotesVo> list(InquiryNotesBo bo, PageQuery pageQuery) {
        return inquiryNotesService.selectPageInquiryNotesList(bo, pageQuery);
    }

    @Log(title = "询问笔录管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("business:table:inquiry-notes:export")
    @PostMapping("/export")
    public void export(InquiryNotesBo bo, HttpServletResponse response) {
        List<InquiryNotesVo> list = inquiryNotesService.selectInquiryNotesList(bo);
        ExcelUtil.exportExcel(list, "询问笔录数据", InquiryNotesVo.class, response);
    }

    @SaCheckPermission("business:table:inquiry-notes:query")
    @GetMapping("/{id}")
    public R<InquiryNotesVo> getInfo(@PathVariable Long id) {
        return R.ok(inquiryNotesService.selectInquiryNotesById(id));
    }

    @SaCheckPermission("business:table:inquiry-notes:add")
    @Log(title = "询问笔录管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody InquiryNotesBo bo) {
        return toAjax(inquiryNotesService.insertInquiryNotes(bo));
    }

    @SaCheckPermission("business:table:inquiry-notes:edit")
    @Log(title = "询问笔录管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody InquiryNotesBo bo) {
        return toAjax(inquiryNotesService.updateInquiryNotes(bo));
    }

    @SaCheckPermission("business:table:inquiry-notes:remove")
    @Log(title = "询问笔录管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(inquiryNotesService.deleteInquiryNotesByIds(Arrays.asList(ids)));
    }
}
