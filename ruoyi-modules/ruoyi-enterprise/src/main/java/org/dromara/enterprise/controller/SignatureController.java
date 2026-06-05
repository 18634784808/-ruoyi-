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
import org.dromara.enterprise.domain.bo.SignatureBo;
import org.dromara.enterprise.domain.vo.SignatureVo;
import org.dromara.enterprise.service.ISignatureService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/signature")
public class SignatureController extends BaseController {

    private final ISignatureService signatureService;

    @SaCheckPermission("business:table:signature:list")
    @GetMapping("/list")
    public TableDataInfo<SignatureVo> list(SignatureBo bo, PageQuery pageQuery) {
        return signatureService.selectPageSignatureList(bo, pageQuery);
    }

    @Log(title = "签名档案管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("business:table:signature:export")
    @PostMapping("/export")
    public void export(SignatureBo bo, HttpServletResponse response) {
        List<SignatureVo> list = signatureService.selectSignatureList(bo);
        ExcelUtil.exportExcel(list, "签名档案数据", SignatureVo.class, response);
    }

    @SaCheckPermission("business:table:signature:query")
    @GetMapping("/{id}")
    public R<SignatureVo> getInfo(@PathVariable Long id) {
        return R.ok(signatureService.selectSignatureById(id));
    }

    @SaCheckPermission("business:table:signature:add")
    @Log(title = "签名档案管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody SignatureBo bo) {
        return toAjax(signatureService.insertSignature(bo));
    }

    @SaCheckPermission("business:table:signature:edit")
    @Log(title = "签名档案管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SignatureBo bo) {
        return toAjax(signatureService.updateSignature(bo));
    }

    @SaCheckPermission("business:table:signature:remove")
    @Log(title = "签名档案管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(signatureService.deleteSignatureByIds(Arrays.asList(ids)));
    }
}
