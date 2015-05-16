package com.rtefrance.declic.portail.util.strategy.imp;

import javax.servlet.http.HttpServletRequest;

import com.rtefrance.declic.common.dto.LabelValueDto;
import com.rtefrance.declic.portail.common.resources.CodeUtil;
import com.rtefrance.declic.portail.util.strategy.LabelValueCreation;

/**
 * Strategie pour la conversion en LabelValueDto de la region.
 * 
 * @author SQLI
 */
public class RegionLabelValueCreation implements LabelValueCreation {

	/**
	 * Cette fonction permet de convertir une initiative en LabelValueDto
	 * 
	 * @param request la requête
	 * @param regionValue la region
	 * @return LabelValueDto correspontante à la region
	 */
	public LabelValueDto createLabelValue(HttpServletRequest request, Object regionValue) {
		if (regionValue instanceof String) {
			String tmpRegionValue = (String) regionValue;
			String regionLabel = CodeUtil.retrieveNameRegion(request, tmpRegionValue);
			return new LabelValueDto(tmpRegionValue, regionLabel);
		} else {
			throw new ClassCastException("regionValue canno't be parse to string");
		}
	}

}
