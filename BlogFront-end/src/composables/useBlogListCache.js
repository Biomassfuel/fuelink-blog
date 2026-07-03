// 首页文章列表缓存：从文章详情返回时，若回到的是刚才那一页（相同搜索/标签/页码），
// 直接用缓存秒显、免去"转圈等加载"的突兀；随后后台静默刷新保持数据最新。
// 非响应式普通对象即可（仅作跨挂载的数据暂存）。
export const blogListCache = {
  key: null,
  list: [],
  total: 0,
}

export const makeListKey = (titleSearch, tagId, page) =>
  `${titleSearch || ''}|${tagId || ''}|${page || 1}`
