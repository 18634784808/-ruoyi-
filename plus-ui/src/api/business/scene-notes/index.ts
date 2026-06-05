import request from '@/utils/request';
import { SceneNotesQuery, SceneNotesForm } from './types';

export const listSceneNotes = (query: SceneNotesQuery) => {
  return request({ url: '/enterprise/scene-notes/list', method: 'get', params: query });
};

export const getSceneNotes = (id: string | number) => {
  return request({ url: '/enterprise/scene-notes/' + id, method: 'get' });
};

export const addSceneNotes = (data: SceneNotesForm) => {
  return request({ url: '/enterprise/scene-notes', method: 'post', data });
};

export const updateSceneNotes = (data: SceneNotesForm) => {
  return request({ url: '/enterprise/scene-notes', method: 'put', data });
};

export const delSceneNotes = (ids: string) => {
  return request({ url: '/enterprise/scene-notes/' + ids, method: 'delete' });
};

export const exportSceneNotes = (query: SceneNotesQuery) => {
  return request({ url: '/enterprise/scene-notes/export', method: 'post', params: query, responseType: 'blob' });
};
