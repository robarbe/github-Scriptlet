package com.rtefrance.declic.portail.util.strategy.imp;

import javax.servlet.http.HttpServletRequest;

import com.rtefrance.declic.common.dto.LabelValueDto;
import com.rtefrance.declic.common.re.enums.DemandOutputType;
import com.rtefrance.declic.portail.common.resources.ResourcesFactory;
import com.rtefrance.declic.portail.util.strategy.LabelValueCreation;

/**
 * Strategie pour la conversion en LabelValueDto de l'initiative.
 * 
 * @author SQLI
 */
public class InitiativeLabelValueCreation implements LabelValueCreation {

	/**
	 * Cette fonction permet de convertir une initiative en LabelValueDto
	 * 
	 * @param request la requête
	 * @param initiativeValue l'initiative
	 * @return LabelValueDto correspontante à l'initiative
	 */
	public LabelValueDto createLabelValue(HttpServletRequest request, Object initiativeValue) {
		if (initiativeValue instanceof String) {
			String tmpInitiativeValue = (String) initiativeValue;
			String initiativeLabelkey = DemandOutputType.getDemandOutputTypeLabelKey(tmpInitiativeValue);
			if (initiativeLabelkey == null) {
				throw new IllegalArgumentException("L'initiative " + tmpInitiativeValue + "passé en parametre n'est pas une initiative connue");
			}
			String initiativeLabel = ResourcesFactory.getMessage(request, initiativeLabelkey);
			return new LabelValueDto(tmpInitiativeValue, initiativeLabel);
		} else {
			throw new ClassCastException("regionValue canno't be parse to string");
		}
	}
}
