package com.rtefrance.declic.portail.util;

import javax.servlet.http.HttpServletRequest;

import com.rtefrance.declic.common.constant.DeclicConstants;
import com.rtefrance.declic.common.dto.LabelValueDto;
import com.rtefrance.declic.portail.common.resources.ResourcesFactory;

/**
 * class util permetant la gestion des filtres
 * 
 * @author SQLI
 */
public final class FiltersUtils {
	/**
	 * index du tableau des valeur par defaut des liste de choix des filtres. Cette index correspond à la valeur tous
	 */
	public static final int TOUS = 0;
	/**
	 * index du tableau des valeur par defaut des liste de choix des filtres. Cette index correspond à la valeur toutes
	 */
	public static final int TOUTES = 1;

	/**
	 * index du tableau des valeur oui Non des liste de choix des filtres. Cette index correspond à la valeur oui
	 */
	public static final int YES = 0;
	/**
	 * index du tableau des valeur oui Non des liste de choix des filtres. Cette index correspond à la valeur Non
	 */
	public static final int NO = 1;

	/**
	 * constructeur privé
	 */
	private FiltersUtils() {

	}

	/**
	 * retorune les valeurs par défaut des listes de choix des filtres
	 * index du tableau :
	 * TOUS (=0) : retourne le label tous
	 * TOUTES (=1) : retroune le label toutes
	 * 
	 * @param request la requête
	 * @return les valeurs de liste de choix des filtres
	 */
	public static LabelValueDto[] getDefaultOptionValueFilter(HttpServletRequest request) {
		LabelValueDto tous = new LabelValueDto(DeclicConstants.DECLIC_WEB_ADMIN_STREAM_FILTER_ALL, ResourcesFactory.getMessage(request, "declic.re.commun.tous"));
		LabelValueDto toutes = new LabelValueDto(DeclicConstants.DECLIC_WEB_ADMIN_STREAM_FILTER_ALL, ResourcesFactory.getMessage(request, "declic.re.commun.toutes"));

		LabelValueDto[] defaultFilterValue = new LabelValueDto[2];
		defaultFilterValue[TOUS] = tous;
		defaultFilterValue[TOUTES] = toutes;

		return defaultFilterValue;
	}

	/**
	 * retorune les valeurs par oui ou non des listes de choix des filtres
	 * index du tableau :
	 * YES (=0) : retourne le label yes
	 * NO (=1) : retroune le label no
	 * 
	 * @param request la requête
	 * @return les valeurs de liste de choix des filtres
	 */
	public static LabelValueDto[] getYesNoLabelValue(HttpServletRequest request) {
		LabelValueDto yes = new LabelValueDto(DeclicConstants.DECLIC_WEB_ADMIN_STREAM_FILTER_YES, ResourcesFactory.getMessage(request, "declic.re.commun.yes"));
		LabelValueDto no = new LabelValueDto(DeclicConstants.DECLIC_WEB_ADMIN_STREAM_FILTER_NO, ResourcesFactory.getMessage(request, "declic.re.commun.no"));

		LabelValueDto[] yesNoFilterValue = new LabelValueDto[2];
		yesNoFilterValue[YES] = yes;
		yesNoFilterValue[NO] = no;

		return yesNoFilterValue;
	}
}
