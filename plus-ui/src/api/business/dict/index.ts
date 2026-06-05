import request from '@/utils/request';
import { BizDictQuery, BizDictForm } from './types';

// 查询业务字典列表
export const listBizDict = (query: BizDictQuery) => {
  return request({
    url: '/enterprise/dict/list',
    method: 'get',
    params: query
  });
};

// 根据字典类型获取选项列表（下拉选择用）
export const getBizDictByType = (dictType: string) => {
  return request({
    url: '/enterprise/dict/all',
    method: 'get',
    params: { dictType, status: '0' }
  });
};

// 获取全部业务字典
export const getAllBizDict = (query?: BizDictQuery) => {
  return request({
    url: '/enterprise/dict/all',
    method: 'get',
    params: query
  });
};

// 查询业务字典详情
export const getBizDict = (id: string | number) => {
  return request({
    url: '/enterprise/dict/' + id,
    method: 'get'
  });
};

// 新增业务字典
export const addBizDict = (data: BizDictForm) => {
  return request({
    url: '/enterprise/dict',
    method: 'post',
    data
  });
};

// 修改业务字典
export const updateBizDict = (data: BizDictForm) => {
  return request({
    url: '/enterprise/dict',
    method: 'put',
    data
  });
};

// 删除业务字典
export const delBizDict = (ids: string) => {
  return request({
    url: '/enterprise/dict/' + ids,
    method: 'delete'
  });
};

// 导出业务字典
export const exportBizDict = (query: BizDictQuery) => {
  return request({
    url: '/enterprise/dict/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
};
