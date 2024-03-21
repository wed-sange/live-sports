import { post, get } from '@/utils/useAxiosApi';
import { BannerInfo, MatchInfo, LiveInfo, NewsInfo } from './model';

const getBanners = async (data: any) => get<BannerInfo>({ url: '/advertising/banner/list', data });

const getHotMaths = async (data: any) => post<MatchInfo>({ url: '/schedule/latestMatch', data });

const getHomeLiving = async (data: any) => post<LiveInfo>({ url: '/home/living/page', data });

const getHotSearch = async (data: any) => post<LiveInfo>({ url: '/schedule/ongoingMatch', data });

const getNews = async (data: any) => post<NewsInfo>({ url: '/news/page', data });

export default { getBanners, getHomeLiving, getHotMaths, getNews, getHotSearch };
