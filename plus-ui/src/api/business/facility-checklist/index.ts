import request from '@/utils/request';
import { FacilityChecklistQuery, FacilityChecklistForm } from './types';

export const listFacilityChecklist = (query: FacilityChecklistQuery) => {
  return request({ url: '/enterprise/facility-checklist/list', method: 'get', params: query });
};

export const getFacilityChecklist = (id: string | number) => {
  return request({ url: '/enterprise/facility-checklist/' + id, method: 'get' });
};

export const addFacilityChecklist = (data: FacilityChecklistForm) => {
  return request({ url: '/enterprise/facility-checklist', method: 'post', data });
};

export const updateFacilityChecklist = (data: FacilityChecklistForm) => {
  return request({ url: '/enterprise/facility-checklist', method: 'put', data });
};

export const delFacilityChecklist = (ids: string) => {
  return request({ url: '/enterprise/facility-checklist/' + ids, method: 'delete' });
};

export const exportFacilityChecklist = (query: FacilityChecklistQuery) => {
  return request({ url: '/enterprise/facility-checklist/export', method: 'post', params: query, responseType: 'blob' });
};
