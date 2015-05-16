package com.rtefrance.declic.portail.util;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.rtefrance.declic.common.dto.LabelValueDto;
import com.rtefrance.declic.portail.util.strategy.LabelValueCreation;
import com.rtefrance.declic.portail.util.strategy.imp.DefaultLabelValueCreation;
import com.rtefrance.declic.portail.util.strategy.imp.EtatLabelValueCreation;
import com.rtefrance.declic.portail.util.strategy.imp.InitiativeLabelValueCreation;
import com.rtefrance.declic.portail.util.strategy.imp.RegionLabelValueCreation;

/**
 * Cette class est une class utile qui permet de gere les LabelValueDto
 * 
 * @author SQLI
 */
public final class LabelValueUtils {

	/**
	 * la strategie par defaut utilisé pour la creation d'un LabelValueDto
	 */
	private static LabelValueCreation DEFAULT_LABEL_VALUE_CREATION_STRATEGY = new DefaultLabelValueCreation();

	/**
	 * Constructeur par defaut
	 */
	private LabelValueUtils() {

	}

	/**
	 * Cette fonction permet de generer l'ensemble de LabelValueDto depuis un liste de region des demandes
	 * 
	 * @param request la liste de valeur a convertir
	 * @param stringSet la liste de valeur a convertir
	 * @return l'ensemble de LabelValueDto
	 */
	public static Set/* <LabelValueDto> */convertRegionSetToLabelValueSet(HttpServletRequest request, Set/* <String> */stringSet) {
		assert stringSet != null;

		return convertAllToLabelValue(request, new RegionLabelValueCreation(), stringSet);
	}

	/**
	 * Cette fonction permet de generer l'ensemble de LabelValueDto depuis un liste d'initiative de demande
	 * 
	 * @param request la liste de valeur a convertir
	 * @param stringSet la liste de valeur a convertir
	 * @return l'ensemble de LabelValueDto
	 */
	public static Set/* <LabelValueDto> */convertInitiativeSetToLabelValueSet(HttpServletRequest request, Set/* <String> */stringSet) {
		assert stringSet != null;

		return convertAllToLabelValue(request, new InitiativeLabelValueCreation(), stringSet);
	}

	/**
	 * Cette fonction permet de generer l'ensemble de LabelValueDto depuis un liste d'état de demande
	 * 
	 * @param request la liste de valeur a convertir
	 * @param stringSet la liste de valeur a convertir
	 * @return l'ensemble de LabelValueDto
	 */
	public static Set/* <LabelValueDto> */convertEtatSetToLabelValueSet(HttpServletRequest request, Set/* <String> */stringSet) {
		assert stringSet != null;

		return convertAllToLabelValue(request, new EtatLabelValueCreation(), stringSet);
	}

	/**
	 * Cette fonction permet de generer l'ensemble de LabelValueDto depuis un liste de valeur
	 * 
	 * @param stringSet la liste de valeur a convertir
	 * @return l'ensemble de LabelValueDto
	 */
	public static Set/* <LabelValueDto> */convertAllToLabelValueSet(Set/* <String> */stringSet) {
		assert stringSet != null;

		return convertAllToLabelValue(null, DEFAULT_LABEL_VALUE_CREATION_STRATEGY, stringSet);
	}

	/**
	 * Converti l'ensemble des données passé en parametre ne un ensemble de ValueLabelDto
	 * 
	 * @param request la requete
	 * @param strategy la strategie de conversion
	 * @param stringSet les données
	 * @return l'ensemble de ValueLabelDto converti
	 */
	public static Set convertAllToLabelValue(HttpServletRequest request, LabelValueCreation strategy, Set stringSet) {
		Set/* <LabelValueDto> */result = new LinkedHashSet();

		for (Iterator it = stringSet.iterator(); it.hasNext();) {
			Object tmpRegionValue = it.next();
			LabelValueDto labelValue = convertToLabelValue(request, strategy, tmpRegionValue);
			result.add(labelValue);
		}

		return result;
	}

	/**
	 * converti un object en LabelValueDto suivant une strategie de conversion
	 * 
	 * @param request la requete
	 * @param strategy la strategie de conversion
	 * @param value la valeur
	 * @return le LabelValueDto correspondant à la valeur
	 */
	public static LabelValueDto convertToLabelValue(HttpServletRequest request, LabelValueCreation strategy, Object value) {
		return strategy.createLabelValue(request, value);
	}
}
