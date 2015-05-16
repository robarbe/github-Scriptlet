package com.rtefrance.declic.portail.util.strategy.imp;

import javax.servlet.http.HttpServletRequest;

import com.rtefrance.declic.common.dto.LabelValueDto;
import com.rtefrance.declic.portail.util.strategy.LabelValueCreation;

/**
 * Strategie pour la conversion en LabelValueDto d'une valeur. elle correspond à la strategie par defaut utilisé
 * 
 * @author SQLI
 */
public class DefaultLabelValueCreation implements LabelValueCreation {

	/**
	 * Cette fonction permet de convertir une valeur en LabelValueDto. Cette valeur est
	 * à la fois valeur et libelle de la LabelValueDto
	 * 
	 * @param request la requête
	 * @param value la region
	 * @return LabelValueDto correspontante à la region
	 */
	public LabelValueDto createLabelValue(HttpServletRequest request, Object value) {
		if (value instanceof String) {
			String tmpValue = (String) value;
			return new LabelValueDto(tmpValue, tmpValue);
		} else {
			throw new ClassCastException("listValue of the function getLabelValueSetWithTraduction must be a list of string");
		}
	}

}
