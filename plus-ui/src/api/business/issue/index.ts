import request from '@/utils/request';
import { IssueQuery, IssueForm } from './types';

export const listIssue = (query: IssueQuery) => {
  return request({ url: '/enterprise/issue/list', method: 'get', params: query });
};

export const getIssue = (id: string | number) => {
  return request({ url: '/enterprise/issue/' + id, method: 'get' });
};

export const addIssue = (data: IssueForm) => {
  return request({ url: '/enterprise/issue', method: 'post', data });
};

export const updateIssue = (data: IssueForm) => {
  return request({ url: '/enterprise/issue', method: 'put', data });
};

export const delIssue = (ids: string) => {
  return request({ url: '/enterprise/issue/' + ids, method: 'delete' });
};

export const exportIssue = (query: IssueQuery) => {
  return request({ url: '/enterprise/issue/export', method: 'post', params: query, responseType: 'blob' });
};
