import request from '@/utils/request';
import { InquiryNotesQuery, InquiryNotesForm } from './types';

export const listInquiryNotes = (query: InquiryNotesQuery) => {
  return request({ url: '/enterprise/inquiry-notes/list', method: 'get', params: query });
};

export const getInquiryNotes = (id: string | number) => {
  return request({ url: '/enterprise/inquiry-notes/' + id, method: 'get' });
};

export const addInquiryNotes = (data: InquiryNotesForm) => {
  return request({ url: '/enterprise/inquiry-notes', method: 'post', data });
};

export const updateInquiryNotes = (data: InquiryNotesForm) => {
  return request({ url: '/enterprise/inquiry-notes', method: 'put', data });
};

export const delInquiryNotes = (ids: string) => {
  return request({ url: '/enterprise/inquiry-notes/' + ids, method: 'delete' });
};

export const exportInquiryNotes = (query: InquiryNotesQuery) => {
  return request({ url: '/enterprise/inquiry-notes/export', method: 'post', params: query, responseType: 'blob' });
};
