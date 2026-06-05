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
import org.dromara.enterprise.domain.bo.SceneNotesBo;
import org.dromara.enterprise.domain.vo.SceneNotesVo;
import org.dromara.enterprise.service.ISceneNotesService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/enterprise/scene-notes")
public class SceneNotesController extends BaseController {

    private final ISceneNotesService sceneNotesService;

    @SaCheckPermission("business:table:scene-notes:list")
    @GetMapping("/list")
    public TableDataInfo<SceneNotesVo> list(SceneNotesBo bo, PageQuery pageQuery) {
        return sceneNotesService.selectPageSceneNotesList(bo, pageQuery);
    }

    @Log(title = "现场笔录管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("business:table:scene-notes:export")
    @PostMapping("/export")
    public void export(SceneNotesBo bo, HttpServletResponse response) {
        List<SceneNotesVo> list = sceneNotesService.selectSceneNotesList(bo);
        ExcelUtil.exportExcel(list, "现场笔录数据", SceneNotesVo.class, response);
    }

    @SaCheckPermission("business:table:scene-notes:query")
    @GetMapping("/{id}")
    public R<SceneNotesVo> getInfo(@PathVariable Long id) {
        return R.ok(sceneNotesService.selectSceneNotesById(id));
    }

    @SaCheckPermission("business:table:scene-notes:add")
    @Log(title = "现场笔录管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody SceneNotesBo bo) {
        return toAjax(sceneNotesService.insertSceneNotes(bo));
    }

    @SaCheckPermission("business:table:scene-notes:edit")
    @Log(title = "现场笔录管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SceneNotesBo bo) {
        return toAjax(sceneNotesService.updateSceneNotes(bo));
    }

    @SaCheckPermission("business:table:scene-notes:remove")
    @Log(title = "现场笔录管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(sceneNotesService.deleteSceneNotesByIds(Arrays.asList(ids)));
    }
}
