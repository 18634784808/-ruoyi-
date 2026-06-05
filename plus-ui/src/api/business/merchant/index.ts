import request from '@/utils/request';
import { MerchantQuery, MerchantForm } from './types';

export const listMerchant = (query: MerchantQuery) => {
  return request({ url: '/enterprise/merchant/list', method: 'get', params: query });
};

export const getMerchant = (id: string | number) => {
  return request({ url: '/enterprise/merchant/' + id, method: 'get' });
};

export const findMerchantByName = (name: string) => {
  return request({ url: '/enterprise/merchant/findByName', method: 'get', params: { name } });
};

export const getAllMerchant = () => {
  return request({ url: '/enterprise/merchant/all', method: 'get' });
};

export const addMerchant = (data: MerchantForm) => {
  return request({ url: '/enterprise/merchant', method: 'post', data });
};

export const updateMerchant = (data: MerchantForm) => {
  return request({ url: '/enterprise/merchant', method: 'put', data });
};

export const delMerchant = (ids: string) => {
  return request({ url: '/enterprise/merchant/' + ids, method: 'delete' });
};

export const exportMerchant = (query: MerchantQuery) => {
  return request({ url: '/enterprise/merchant/export', method: 'post', params: query, responseType: 'blob' });
};
