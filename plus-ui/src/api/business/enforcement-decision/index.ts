import request from '@/utils/request';
import { EnforcementDecisionQuery, EnforcementDecisionForm } from './types';

export const listEnforcementDecision = (query: EnforcementDecisionQuery) => {
  return request({ url: '/enterprise/enforcement-decision/list', method: 'get', params: query });
};

export const getAllEnforcementDecision = (query?: EnforcementDecisionQuery) => {
  return request({ url: '/enterprise/enforcement-decision/all', method: 'get', params: query });
};

export const getEnforcementDecision = (id: string | number) => {
  return request({ url: '/enterprise/enforcement-decision/' + id, method: 'get' });
};

export const addEnforcementDecision = (data: EnforcementDecisionForm) => {
  return request({ url: '/enterprise/enforcement-decision', method: 'post', data });
};

export const updateEnforcementDecision = (data: EnforcementDecisionForm) => {
  return request({ url: '/enterprise/enforcement-decision', method: 'put', data });
};

export const delEnforcementDecision = (ids: string) => {
  return request({ url: '/enterprise/enforcement-decision/' + ids, method: 'delete' });
};

export const exportEnforcementDecision = (query: EnforcementDecisionQuery) => {
  return request({ url: '/enterprise/enforcement-decision/export', method: 'post', params: query, responseType: 'blob' });
};
