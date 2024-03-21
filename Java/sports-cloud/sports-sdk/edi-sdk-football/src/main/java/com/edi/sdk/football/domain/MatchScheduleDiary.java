package com.edi.sdk.football.domain;

import java.util.List;

import lombok.Data;

/**
 * null
 */
@Data
public class MatchScheduleDiary {
	/**
	 * 比赛列表
	 */
	private List<Match> match;
	/**
	 * 赛事列表
	 */
	private List<Competition> competition;

	@Data
	public static class Competition {
		/**
		 * 赛事名称
		 */
		private String name;
		/**
		 * 赛事logo
		 */
		private String logo;
		/**
		 * 赛事id
		 */
		private Integer id;
	}

	/**
	 * 球队列表
	 */
	private List<Team> team;

	@Data
	public static class Team {
		/**
		 * 球队名称
		 */
		private String name;
		/**
		 * 球队logo
		 */
		private String logo;
		/**
		 * 球队id
		 */
		private Integer id;
	}
}