import request from '@/utils/request'

// 查询索引管理列表
export function listEsIndex(query) {
  return request({
    url: '/es/esIndex/list',
    method: 'get',
    params: query
  })
}

// 查询索引管理详细
export function getEsIndex(indexId) {
  return request({
    url: '/es/esIndex/' + indexId,
    method: 'get'
  })
}

// 新增索引管理
export function addEsIndex(data) {
  return request({
    url: '/es/esIndex',
    method: 'post',
    data: data
  })
}

// 修改索引管理
export function updateEsIndex(data) {
  return request({
    url: '/es/esIndex',
    method: 'put',
    data: data
  })
}

// 删除索引管理
export function delEsIndex(indexId) {
  return request({
    url: '/es/esIndex/' + indexId,
    method: 'delete'
  })
}

// 导出索引管理
export function exportEsIndex(query) {
  return request({
    url: '/es/esIndex/export',
    method: 'get',
    params: query
  })
}

// 查询索引列表排除 已经导入的
export function indexList(query) {
  return request({
    url: '/es/esApi/indexList',
    method: 'get',
    params: query
  })
}

// 查询索引列表排除 已经导入的
export function importIndex(data) {
  return request({
    url: '/es/esApi/importIndex',
    method: 'post',
    params: data
  })
}