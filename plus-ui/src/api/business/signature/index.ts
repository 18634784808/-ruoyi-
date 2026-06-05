import request from '@/utils/request';
import { SignatureQuery, SignatureForm } from './types';

export const listSignature = (query: SignatureQuery) => {
  return request({ url: '/enterprise/signature/list', method: 'get', params: query });
};

export const getSignature = (id: string | number) => {
  return request({ url: '/enterprise/signature/' + id, method: 'get' });
};

export const addSignature = (data: SignatureForm) => {
  return request({ url: '/enterprise/signature', method: 'post', data });
};

export const updateSignature = (data: SignatureForm) => {
  return request({ url: '/enterprise/signature', method: 'put', data });
};

export const delSignature = (ids: string) => {
  return request({ url: '/enterprise/signature/' + ids, method: 'delete' });
};

export const exportSignature = (query: SignatureQuery) => {
  return request({ url: '/enterprise/signature/export', method: 'post', params: query, responseType: 'blob' });
};
