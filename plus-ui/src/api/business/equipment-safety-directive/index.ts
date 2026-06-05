import request from '@/utils/request';
import { EquipmentSafetyDirectiveQuery, EquipmentSafetyDirectiveForm } from './types';

export const listEquipmentSafetyDirective = (query: EquipmentSafetyDirectiveQuery) => {
  return request({ url: '/enterprise/equipment-safety-directive/list', method: 'get', params: query });
};

export const getAllEquipmentSafetyDirective = (query?: EquipmentSafetyDirectiveQuery) => {
  return request({ url: '/enterprise/equipment-safety-directive/all', method: 'get', params: query });
};

export const getEquipmentSafetyDirective = (id: string | number) => {
  return request({ url: '/enterprise/equipment-safety-directive/' + id, method: 'get' });
};

export const addEquipmentSafetyDirective = (data: EquipmentSafetyDirectiveForm) => {
  return request({ url: '/enterprise/equipment-safety-directive', method: 'post', data });
};

export const updateEquipmentSafetyDirective = (data: EquipmentSafetyDirectiveForm) => {
  return request({ url: '/enterprise/equipment-safety-directive', method: 'put', data });
};

export const delEquipmentSafetyDirective = (ids: string) => {
  return request({ url: '/enterprise/equipment-safety-directive/' + ids, method: 'delete' });
};

export const exportEquipmentSafetyDirective = (query: EquipmentSafetyDirectiveQuery) => {
  return request({ url: '/enterprise/equipment-safety-directive/export', method: 'post', params: query, responseType: 'blob' });
};
