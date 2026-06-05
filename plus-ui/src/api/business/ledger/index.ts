import request from '@/utils/request';
import { LedgerQuery, LedgerForm } from './types';

// 查询台账列表
export const listLedger = (query: LedgerQuery) => {
  return request({
    url: '/enterprise/ledger/list',
    method: 'get',
    params: query
  });
};

// 查询台账详情
export const getLedger = (id: string | number) => {
  return request({
    url: '/enterprise/ledger/' + id,
    method: 'get'
  });
};

// 新增台账
export const addLedger = (data: LedgerForm) => {
  return request({
    url: '/enterprise/ledger',
    method: 'post',
    data
  });
};

// 修改台账
export const updateLedger = (data: LedgerForm) => {
  return request({
    url: '/enterprise/ledger',
    method: 'put',
    data
  });
};

// 删除台账
export const delLedger = (ids: string) => {
  return request({
    url: '/enterprise/ledger/' + ids,
    method: 'delete'
  });
};

// 导出台账
export const exportLedger = (query: LedgerQuery) => {
  return request({
    url: '/enterprise/ledger/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  });
};
