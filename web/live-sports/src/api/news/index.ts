import { get } from '@/utils/useAxiosApi';

const getNewsDetail = async (id: any) => get<{}>({ url: `news/getNewsInfo/${id}` });

export default { getNewsDetail };
