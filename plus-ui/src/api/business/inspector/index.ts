import request from '@/utils/request';
import { InspectorQuery, InspectorForm } from './types';

// 查询检察人员列表
export const listInspector = (query: InspectorQuery) => {
  return request({
    url: '/enterprise/inspector/list',
    method: 'get',
    params: query
  });
};

// 获取全部检察人员（下拉选择用）
export const getAllInspector = () => {
  return request({
    url: '/enterprise/inspector/all',
    method: 'get'
  });
};

// 查询检察人员详情
export const getInspector = (id: string | number) => {
  return request({
    url: '/enterprise/inspector/' + id,
    method: 'get'
  });
};

// 新增检察人员
export const addInspector = (data: InspectorForm) => {
  return request({
    url: '/enterprise/inspector',
    method: 'post',
    data
  });
};

// 修改检察人员
export const updateInspector = (data: InspectorForm) => {
  return request({
    url: '/enterprise/inspector',
    method: 'put',
    data
  });
};

// 删除检察人员
export const delInspector = (ids: string) => {
  return request({
    url: '/enterprise/inspector/' + ids,
    method: 'delete'
  });
};

// 导出检察人员
export const exportInspector = (query: InspectorQuery) => {
  return request({
    url: '/enterprise/inspector/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
};
