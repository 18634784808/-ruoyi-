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
import org.dromara.enterprise.domain.bo.LedgerBo;
import org.dromara.enterprise.domain.vo.LedgerVo;
import org.dromara.enterprise.service.ILedgerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/ledger")
public class LedgerController extends BaseController {

    private final ILedgerService ledgerService;

    @SaCheckPermission("business:table:ledger:list")
    @GetMapping("/list")
    public TableDataInfo<LedgerVo> list(LedgerBo bo, PageQuery pageQuery) {
        return ledgerService.selectPageLedgerList(bo, pageQuery);
    }

    @Log(title = "台账管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("business:table:ledger:export")
    @PostMapping("/export")
    public void export(LedgerBo bo, HttpServletResponse response) {
        List<LedgerVo> list = ledgerService.selectLedgerList(bo);
        ExcelUtil.exportExcel(list, "台账数据", LedgerVo.class, response);
    }

    @SaCheckPermission("business:table:ledger:query")
    @GetMapping("/{id}")
    public R<LedgerVo> getInfo(@PathVariable Long id) {
        return R.ok(ledgerService.selectLedgerById(id));
    }

    @SaCheckPermission("business:table:ledger:add")
    @Log(title = "台账管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody LedgerBo bo) {
        return toAjax(ledgerService.insertLedger(bo));
    }

    @SaCheckPermission("business:table:ledger:edit")
    @Log(title = "台账管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody LedgerBo bo) {
        return toAjax(ledgerService.updateLedger(bo));
    }

    @SaCheckPermission("business:table:ledger:remove")
    @Log(title = "台账管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(ledgerService.deleteLedgerByIds(Arrays.asList(ids)));
    }
}
