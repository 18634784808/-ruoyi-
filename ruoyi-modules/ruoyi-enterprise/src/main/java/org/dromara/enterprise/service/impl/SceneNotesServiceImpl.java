package org.dromara.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.SceneNotes;
import org.dromara.enterprise.domain.bo.SceneNotesBo;
import org.dromara.enterprise.domain.vo.SceneNotesVo;
import org.dromara.enterprise.mapper.SceneNotesMapper;
import org.dromara.enterprise.service.ISceneNotesService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SceneNotesServiceImpl implements ISceneNotesService {

    private final SceneNotesMapper baseMapper;

    private LambdaQueryWrapper<SceneNotes> buildQueryWrapper(SceneNotesBo bo) {
        return new LambdaQueryWrapper<SceneNotes>()
            .like(StringUtils.isNotBlank(bo.getPlace()), SceneNotes::getPlace, bo.getPlace())
            .like(StringUtils.isNotBlank(bo.getParty()), SceneNotes::getParty, bo.getParty())
            .like(StringUtils.isNotBlank(bo.getInspector()), SceneNotes::getInspector, bo.getInspector())
            .eq(StringUtils.isNotBlank(bo.getStatus()), SceneNotes::getStatus, bo.getStatus())
            .orderByDesc(SceneNotes::getCreateTime);
    }

    @Override
    public TableDataInfo<SceneNotesVo> selectPageSceneNotesList(SceneNotesBo bo, PageQuery pageQuery) {
        Page<SceneNotesVo> page = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(page);
    }

    @Override
    public List<SceneNotesVo> selectSceneNotesList(SceneNotesBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public SceneNotesVo selectSceneNotesById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public int insertSceneNotes(SceneNotesBo bo) {
        SceneNotes entity = MapstructUtils.convert(bo, SceneNotes.class);
        return baseMapper.insert(entity);
    }

    @Override
    public int updateSceneNotes(SceneNotesBo bo) {
        SceneNotes entity = MapstructUtils.convert(bo, SceneNotes.class);
        return baseMapper.updateById(entity);
    }

    @Override
    public int deleteSceneNotesByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }
}
