package com.go.model;

import java.util.HashMap;
import java.util.Map;

public class UsageSummary {
	private Long startTime;
	private Integer totalCount = 0;
	private Map<String, Integer> linkCountMap = new HashMap<String, Integer>();

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Map<String, Integer> getLinkCountMap() {
		return linkCountMap;
	}

	public void setLinkCountMap(Map<String, Integer> linkCountMap) {
		this.linkCountMap = linkCountMap;
	}

	public void addLinkCount(String linkName) {
		addLinkCount(linkName, 1);
	}

	public void addLinkCount(String linkName, Integer count) {
		if (linkCountMap.containsKey(linkName)) {
			linkCountMap.put(linkName, linkCountMap.get(linkName) + count);
		} else {
			linkCountMap.put(linkName, count);
		}
		totalCount += count;
	}

	@Override
	public String toString() {
		return "UsageSummary [startTime=" + startTime + ", totalCount=" + totalCount + "]";
	}
}
