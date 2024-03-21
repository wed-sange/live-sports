type PageListCacheOption = { cacheKey?: string };
class PageListCache<T extends { id: string }> {
  cacheMap = new Map<string, T>();
  cacheKey = 'id';
  constructor(options?: PageListCacheOption) {
    if (options?.cacheKey) this.cacheKey = options.cacheKey;
  }
  getData(data: T[]) {
    const renderData = data.filter((cur) => {
      const hasCache = this.cacheMap.has(cur[this.cacheKey]);
      if (!hasCache) {
        this.cacheMap.set(cur[this.cacheKey], cur);
      }
      return !hasCache;
    });
    return renderData;
  }
  remove(key: string) {
    if (this.cacheMap.has(key)) {
      this.cacheMap.delete(key);
    }
  }
  clear() {
    this.cacheMap.clear();
  }
}
export const usePageListCache = <T extends { id: string }>(options?: PageListCacheOption) => {
  const pageListCache = new PageListCache<T>(options);
  const getRenderData = (data: T[]) => {
    return pageListCache.getData(data);
  };
  const clearCache = () => {
    pageListCache.clear();
  };
  const removeCache = (key: string) => {
    pageListCache.remove(key);
  };
  return {
    getRenderData,
    clearCache,
    removeCache,
  };
};
