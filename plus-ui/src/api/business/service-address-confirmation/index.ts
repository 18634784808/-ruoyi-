import request from '@/utils/request';
import { ServiceAddressConfirmationQuery, ServiceAddressConfirmationForm } from './types';

export const listServiceAddressConfirmation = (query: ServiceAddressConfirmationQuery) => {
  return request({ url: '/enterprise/service-address-confirmation/list', method: 'get', params: query });
};

export const getAllServiceAddressConfirmation = (query?: ServiceAddressConfirmationQuery) => {
  return request({ url: '/enterprise/service-address-confirmation/all', method: 'get', params: query });
};

export const getServiceAddressConfirmation = (id: string | number) => {
  return request({ url: '/enterprise/service-address-confirmation/' + id, method: 'get' });
};

export const addServiceAddressConfirmation = (data: ServiceAddressConfirmationForm) => {
  return request({ url: '/enterprise/service-address-confirmation', method: 'post', data });
};

export const updateServiceAddressConfirmation = (data: ServiceAddressConfirmationForm) => {
  return request({ url: '/enterprise/service-address-confirmation', method: 'put', data });
};

export const delServiceAddressConfirmation = (ids: string) => {
  return request({ url: '/enterprise/service-address-confirmation/' + ids, method: 'delete' });
};

export const exportServiceAddressConfirmation = (query: ServiceAddressConfirmationQuery) => {
  return request({ url: '/enterprise/service-address-confirmation/export', method: 'post', params: query, responseType: 'blob' });
};
