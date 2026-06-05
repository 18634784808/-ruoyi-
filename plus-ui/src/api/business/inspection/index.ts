import request from '@/utils/request';
import { InspectionQuery, InspectionForm } from './types';

// 查询检查记录列表
export const listInspection = (query: InspectionQuery) => {
  return request({
    url: '/enterprise/inspection/list',
    method: 'get',
    params: query
  });
};

// 查询检查记录详情
export const getInspection = (id: string | number) => {
  return request({
    url: '/enterprise/inspection/' + id,
    method: 'get'
  });
};

// 新增检查记录
export const addInspection = (data: InspectionForm) => {
  return request({
    url: '/enterprise/inspection',
    method: 'post',
    data
  });
};

// 修改检查记录
export const updateInspection = (data: InspectionForm) => {
  return request({
    url: '/enterprise/inspection',
    method: 'put',
    data
  });
};

// 删除检查记录
export const delInspection = (ids: string) => {
  return request({
    url: '/enterprise/inspection/' + ids,
    method: 'delete'
  });
};

// 导出检查记录
export const exportInspection = (query: InspectionQuery) => {
  return request({
    url: '/enterprise/inspection/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
};
