import request from '@/utils/request';
import { EquipmentCheckRecordQuery, EquipmentCheckRecordForm } from './types';

export const listEquipmentCheckRecord = (query: EquipmentCheckRecordQuery) => {
  return request({ url: '/enterprise/equipment-check-record/list', method: 'get', params: query });
};
export const getAllEquipmentCheckRecord = (query?: EquipmentCheckRecordQuery) => {
  return request({ url: '/enterprise/equipment-check-record/all', method: 'get', params: query });
};
export const getEquipmentCheckRecord = (id: string | number) => {
  return request({ url: '/enterprise/equipment-check-record/' + id, method: 'get' });
};
export const addEquipmentCheckRecord = (data: EquipmentCheckRecordForm) => {
  return request({ url: '/enterprise/equipment-check-record', method: 'post', data });
};
export const updateEquipmentCheckRecord = (data: EquipmentCheckRecordForm) => {
  return request({ url: '/enterprise/equipment-check-record', method: 'put', data });
};
export const delEquipmentCheckRecord = (ids: string) => {
  return request({ url: '/enterprise/equipment-check-record/' + ids, method: 'delete' });
};
export const exportEquipmentCheckRecord = (query: EquipmentCheckRecordQuery) => {
  return request({ url: '/enterprise/equipment-check-record/export', method: 'post', params: query, responseType: 'blob' });
};
