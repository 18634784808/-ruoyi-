package org.dromara.enterprise.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.enterprise.domain.bo.SceneNotesBo;
import org.dromara.enterprise.domain.vo.SceneNotesVo;

import java.util.List;

public interface ISceneNotesService {

    TableDataInfo<SceneNotesVo> selectPageSceneNotesList(SceneNotesBo bo, PageQuery pageQuery);

    List<SceneNotesVo> selectSceneNotesList(SceneNotesBo bo);

    SceneNotesVo selectSceneNotesById(Long id);

    int insertSceneNotes(SceneNotesBo bo);

    int updateSceneNotes(SceneNotesBo bo);

    int deleteSceneNotesByIds(List<Long> ids);
}
