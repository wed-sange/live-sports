package com.edi.sdk.basketball.domain;

import java.util.List;

import lombok.Data;

/**
 * 比赛日程

 * 2023年5月22日 下午5:07:10
 */
@Data
public class Schedule {
	/**
	 * 比赛列表
	 */
	private List<Match> match;
	/**
	 * 赛事列表
	 */
	private List<ScheduleCompetition> competition;
	/**
	 * 球队列表
	 */
	private List<ScheduleTeam> team;
	
	@Data
	public static class ScheduleCompetition { 
		/**
		 * 赛事id
		 */
		private Integer id;
		/**
		 * 赛事名称
		 */
		private String name;
		/**
		 * 赛事logo
		 */
		private String logo;
	}
	
	@Data
	public static class ScheduleTeam {
		/**
		 * 球队id
		 */
		private Integer id;
		/**
		 * 球队名称
		 */
		private String name;
		/**
		 * 球队logo
		 */
		private String logo;
	}
}
