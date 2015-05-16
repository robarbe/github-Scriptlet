package com.rtefrance.declic.portail.util.strategy.imp;

import javax.servlet.http.HttpServletRequest;

import com.rtefrance.declic.common.dto.LabelValueDto;
import com.rtefrance.declic.common.re.enums.DemandStatus;
import com.rtefrance.declic.portail.common.resources.ResourcesFactory;
import com.rtefrance.declic.portail.util.strategy.LabelValueCreation;

/**
 * Strategie pour la conversion en LabelValueDto de l'initiative.
 * 
 * @author SQLI
 */
public class EtatLabelValueCreation implements LabelValueCreation {

	/**
	 * Cette fonction permet de convertir une initiative en LabelValueDto
	 * 
	 * @param request la requête
	 * @param etatValue l'initiative
	 * @return LabelValueDto correspontante à l'initiative
	 */
	public LabelValueDto createLabelValue(HttpServletRequest request, Object etatValue) {
		if (etatValue instanceof String) {
			String tmpEtatValue = (String) etatValue;
			String etatLabelkey = DemandStatus.getDemandStatusLabelKey(tmpEtatValue);
			if (etatLabelkey == null) {
				throw new IllegalArgumentException("L'état" + tmpEtatValue + " passé en parametre n'est pas un état connue");
			}
			String etatLabel = ResourcesFactory.getMessage(request, etatLabelkey);
			return new LabelValueDto(tmpEtatValue, etatLabel);
		} else {
			throw new ClassCastException("regionValue canno't be parse to string");
		}
	}
}
