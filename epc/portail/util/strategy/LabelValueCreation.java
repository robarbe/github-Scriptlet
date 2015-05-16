/**
 * 
 */
package com.rtefrance.declic.portail.util.strategy;

import javax.servlet.http.HttpServletRequest;

import com.rtefrance.declic.common.dto.LabelValueDto;

/**
 * Cette interface represente une strategie de creation de LabelValueCreation
 * 
 * @author SQLI
 */
public interface LabelValueCreation {
	/**
	 * Cette methode correspond a la strategie de creation de LabelValueDto en fonction de la valeur passée
	 * en parametres
	 * 
	 * @param request la requete
	 * @param value la valeur
	 * @return la LabelValueDto correspondante
	 */
	LabelValueDto createLabelValue(HttpServletRequest request, Object value);
}
