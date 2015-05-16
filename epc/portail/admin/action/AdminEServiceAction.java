package com.rtefrance.declic.portail.admin.action;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.rtefrance.declic.common.admin.dto.ContractTypeDto;
import com.rtefrance.declic.common.admin.ordering.MenuDtoByIndexOrdering;
import com.rtefrance.declic.common.constant.DeclicConstants;
import com.rtefrance.declic.common.dto.LabelValueDto;
import com.rtefrance.declic.common.exception.InfrastructureException;
import com.rtefrance.declic.common.habilitation.dto.MenuDto;
import com.rtefrance.declic.common.habilitation.dto.ServiceDto;
import com.rtefrance.declic.common.log.LogFactory;
import com.rtefrance.declic.common.utils.impl.LocaleUtils;
import com.rtefrance.declic.common.validator.Validator;
import com.rtefrance.declic.portail.admin.form.ModifyEServiceForm;
import com.rtefrance.declic.portail.common.action.AbstractDeclicMappingDispachAction;
import com.rtefrance.declic.portail.common.proxy.ContractTypeProxy;
import com.rtefrance.declic.portail.common.resources.ResourcesFactory;
import com.rtefrance.declic.portail.habilitation.proxy.ServiceProxy;
import com.rtefrance.declic.portail.habilitation.util.HabilitationUtil;

/**
 * The Class AdminEServiceAction.
 */
public class AdminEServiceAction extends AbstractDeclicMappingDispachAction {

	/** The log. */
	private Logger log = LogFactory.getLog(getClass());

	/**
	 * Nombre de libellés par menu
	 */
	private final static int numberLabelMenu = 2;

	/**
	 * Gets the path first.
	 * 
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the path first
	 * @see com.rtefrance.declic.portail.common.action.AbstractDeclicMappingDispachAction#getPathFirst (org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	 @Override
	protected LabelValueDto getPathFirst(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return new LabelValueDto("/adminEServiceDisplay.do", ResourcesFactory.getMessage(request, "declic.navigation.rubrique.eService"));
	}

	/**
	 * Gets the path second.
	 * 
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the path second
	 * @see com.rtefrance.declic.portail.common.action.AbstractDeclicMappingDispachAction#getPathSecond (org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	 @Override
	protected LabelValueDto getPathSecond(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	/**
	 * Gets the title.
	 * 
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the title
	 * @see com.rtefrance.declic.portail.common.action.AbstractDeclicMappingDispachAction#getTitle (org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	 @Override
	protected String getTitle(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return ResourcesFactory.getMessage(request, "declic.navigation.rubrique.eService");
	}

	/**
	 * Gets the functionnality.
	 * 
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the functionnality
	 * @see com.rtefrance.declic.portail.common.action.AbstractDeclicMappingDispachAction #getFunctionnality(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	 @Override
	protected String getFunctionnality(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	/**
	 * Report functionnality access.
	 * 
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the string
	 * @see com.rtefrance.declic.portail.common.action.AbstractDeclicMappingDispachAction#getFunctionnality (org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	 @Override
	protected String reportFunctionnalityAccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		log.debug("Admin Contract Type fucntionnality access.");
		return null;
	}

	/**
	 * Inits the.
	 * 
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the action forward
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		ModifyEServiceForm modifyEServiceForm = (ModifyEServiceForm) form;

		String locale = "fr.";
		if (!LocaleUtils.isFrench(getLocale(request))) {
			locale = "en.";
		}
		MenuDtoByIndexOrdering menuDtoOrdering = new MenuDtoByIndexOrdering();

		String idEService = request.getParameter("id");
		String[] selectedContrats = new String[0];
		String[] selectedSousMenus = new String[0];
		// Renseignement du formulaire à partir du serviceDto
		if (idEService != null && idEService.length() > 0 && !idEService.equals("0")) {
			try {
				ServiceDto serviceDto = ServiceProxy.retrieveServiceDtoByPk(new Long(idEService));
				if (serviceDto == null) {
					log.debug("serviceDto is null for id : " + idEService);
					return getErrorForward(mapping);
				}
				int i = 0;
				if (serviceDto.getListeContrats() != null && serviceDto.getListeContrats().size() > 0) {
					selectedContrats = new String[serviceDto.getListeContrats().size()];
					for (ContractTypeDto contratTypeDto : serviceDto.getListeContrats()) {
						selectedContrats[i] = contratTypeDto.getCode();
						i++;
					}
				}
				i = 0;
				List<MenuDto> sousMenuDtoList = serviceDto.getListeSousMenus();
				if (sousMenuDtoList != null && !sousMenuDtoList.isEmpty()) {
					selectedSousMenus = new String[serviceDto.getListeSousMenus().size()];
					for (MenuDto menuDto : sousMenuDtoList) {
						changeLabelMenuParentAndLabelMenus(menuDto, locale);
						selectedSousMenus[i] = String.valueOf(menuDto.getId());
						i++;
					}
					sousMenuDtoList = menuDtoOrdering.nullsLast().sortedCopy(sousMenuDtoList);
				}
				modifyEServiceForm.setId(serviceDto.getId().longValue());
				modifyEServiceForm.setLibelle(serviceDto.getLibelleFr());
				modifyEServiceForm.setLibelleEn(serviceDto.getLibelleEn());
				modifyEServiceForm.setSelectedContrats(selectedContrats);
				modifyEServiceForm.setSelectedSousMenus(selectedSousMenus);
				String typeGestion = serviceDto.getTypeGestion();
				modifyEServiceForm.setSelectedTypeGestion(typeGestion);
				modifyEServiceForm.setTypeGestionPreviouslySelected(typeGestion);
				modifyEServiceForm.setDroitSocietyMax(serviceDto.getDroitSocietyMax());
				modifyEServiceForm.setDroitSocietyDefault(serviceDto.getDroitSocietyDefault());
				modifyEServiceForm.setDroitUserMax(serviceDto.getDroitUserMax());
				modifyEServiceForm.setDroitUserDefault(serviceDto.getDroitUserDefault());
				modifyEServiceForm.setDroitUserMaxPreviouslySelected(serviceDto.getDroitUserMax());
				modifyEServiceForm.setDroitUserDefaultPreviouslySelected(serviceDto.getDroitUserDefault());
				modifyEServiceForm.setDroitSocietyMaxPreviouslySelected(serviceDto.getDroitSocietyMax());
				modifyEServiceForm.setDroitSocietyDefaultPreviouslySelected(serviceDto.getDroitSocietyDefault());
				modifyEServiceForm.setHasWriteActions(serviceDto.isHasWriteActions());
				modifyEServiceForm.setSelectedPublic(String.valueOf(serviceDto.isPublicService()));
				String sendMail = String.valueOf(serviceDto.getSendMail());
				modifyEServiceForm.setSelectedSendMail(String.valueOf(serviceDto.getSendMail()));
				modifyEServiceForm.setSendMailPreviouslySelected(sendMail);
			} catch (NumberFormatException nfe) {
				log.debug(nfe.getMessage());
				return mapping.findForward("error");
			} /*
			 * catch (InfrastructureException ie) {
			 * log.debug(ie.getMessage());
			 * return mapping.findForward("error");
			 * }
			 */
		}
		try {
			// Création de l'ensemble des combobox pour contrats et sous-menus
			List<ContractTypeDto> contratTypes = ContractTypeProxy.retrieveAllDistinctContractTypeDto();
			if (contratTypes != null && contratTypes.size() > 0) {
				modifyEServiceForm.setListeContrats(contratTypes);
			} else {
				log.debug("Problem contrat");
				return mapping.findForward("error");

			}
			List<MenuDto> sousMenus = ServiceProxy.getAllMenusForEService();
			if (sousMenus != null && sousMenus.size() > 0) {
				for (MenuDto menuDto : sousMenus) {
					changeLabelMenuParentAndLabelMenus(menuDto, locale);
				}
				Collections.sort(sousMenus);
				List<MenuDto> menusDtosSorted = menuDtoOrdering.nullsLast().sortedCopy(sousMenus);
				modifyEServiceForm.setListeSousMenus(menusDtosSorted);
			} else {
				log.debug("pas de sous menus");
				return mapping.findForward("error");
			}
			// Valeurs possible radio buttons type contrat
			List<String> listeTypeGestion = new ArrayList<String>();
			for (String typeGestion : DeclicConstants.TYPE_GESTION_KEYS_MAP.values()) {
				listeTypeGestion.add(typeGestion);
			}
			Collections.sort(listeTypeGestion);
			modifyEServiceForm.setListeTypeGestion(listeTypeGestion);
			// Valeurs possible radio buttons droit par défaut
			List<String> listeDroit = new ArrayList<String>();
			for (String valueDroit : DeclicConstants.RIGHTS_TO_DROIT_LIBELLE.values()) {
				listeDroit.add(valueDroit);
			}
			Collections.sort(listeDroit);
			modifyEServiceForm.setListeDroit(listeDroit);

		} catch (InfrastructureException ex) {
			log.debug(ex.getMessage());
			throw ex;
		}

		return mapping.findForward("success");

	}

	/**
	 * Modify.
	 * 
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the action forward
	 */
	public ActionForward modify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		ModifyEServiceForm modifyEServiceForm = (ModifyEServiceForm) form;

		ActionErrors errors = modifyEServiceForm.validate(mapping, request);
		if (!errors.isEmpty()) {
			this.saveErrors(request, errors);
			return mapping.getInputForward();
		}

		ServiceDto serviceDto = null;
		String droitSocietyDefault = ModifyEServiceForm.retrieveKeyDroitAccordingToValueToMap(modifyEServiceForm.getDroitSocietyDefault());
		String droitSocietyMax = ModifyEServiceForm.retrieveKeyDroitAccordingToValueToMap(modifyEServiceForm.getDroitSocietyMax());
		String droitUserDefault = ModifyEServiceForm.retrieveKeyDroitAccordingToValueToMap(modifyEServiceForm.getDroitUserDefault());
		String droitUserMax = ModifyEServiceForm.retrieveKeyDroitAccordingToValueToMap(modifyEServiceForm.getDroitUserMax());

		String typeGestion = Validator.isEmpty(modifyEServiceForm.getSelectedTypeGestion()) ? modifyEServiceForm.getSelectedTypeGestionHiddenFirst() : modifyEServiceForm
				.getSelectedTypeGestion();
		if (typeGestion.equals(DeclicConstants.TYPE_GESTION_NOMINATIF_KEY)) {
			modifyEServiceForm.setSelectedSendMail("true");
		}
		serviceDto = new ServiceDto(new Long(modifyEServiceForm.getId()), modifyEServiceForm.getLibelle(), modifyEServiceForm.getLibelleEn(), droitSocietyDefault, droitSocietyMax,
				droitUserDefault, droitUserMax, typeGestion, Boolean.valueOf(modifyEServiceForm.getSelectedSendMail()), Boolean.valueOf(modifyEServiceForm.getSelectedPublic()));
		if (serviceDto.getTypeGestion().equalsIgnoreCase(DeclicConstants.TYPE_GESTION_NOMINATIF_KEY)) {
			serviceDto.setSendMail(false);
		}
		List<ContractTypeDto> listeContratDto = new ArrayList<ContractTypeDto>();
		if (!modifyEServiceForm.getSelectedPublic().equals("true") && modifyEServiceForm.getSelectedContrats() != null && modifyEServiceForm.getSelectedContrats().length > 0) {
			for (int i = 0; i < modifyEServiceForm.getSelectedContrats().length; i++) {
				ContractTypeDto contractTypeDto = new ContractTypeDto(null, modifyEServiceForm.getSelectedContrats()[i], null);
				listeContratDto.add(contractTypeDto);
			}
		}
		serviceDto.setListeContrats(listeContratDto);

		List<MenuDto> listeMenuDto = newArrayList();
		if (modifyEServiceForm.getSelectedSousMenus() != null && modifyEServiceForm.getSelectedSousMenus().length > 0) {
			for (int i = 0; i < modifyEServiceForm.getSelectedSousMenus().length; i++) {
				MenuDto menuDto = new MenuDto(Long.parseLong(modifyEServiceForm.getSelectedSousMenus()[i]), "");
				listeMenuDto.add(menuDto);
			}
		}
		serviceDto.setListeSousMenus(listeMenuDto);

		if (serviceDto != null) {
			try {
				ServiceProxy.saveOrUpdateService(serviceDto);
			} catch (InfrastructureException ie) {
				log.error("Error on method modify e-service id : " + serviceDto.getId());
				ActionErrors msgs = new ActionErrors();
				if (ie.getMessage().indexOf("ServiceAlreadyExistsException") >= 0) {
					msgs.add("declic.admin.eservice.body.erreur.libelle.unique", new ActionMessage("declic.admin.eservice.body.erreur.libelle.unique", serviceDto.getLibelleFr()));
				} else {
					msgs.add("declic.admin.eservice.body.erreur.modify", new ActionMessage("declic.admin.eservice.body.erreur.modify", serviceDto.getLibelleFr()));

				}
				this.saveErrors(request, msgs);
				return mapping.getInputForward();
			}
		}

		List<ServiceDto> mAllEServiceDtoList = ServiceProxy.retrieveAllEServiceDtos();
		request.setAttribute("listEService", mAllEServiceDtoList);
		return mapping.findForward("success");
	}

	/**
	 * Chargement de tous les e-service et de leurs libelles.
	 * 
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the action forward
	 */
	public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List<ServiceDto> mAllEServiceDtoList = ServiceProxy.retrieveAllEServiceDtos();
		String localePrefix = getPrefixForLabelAccordingToLocal(request);

		for (ServiceDto serviceDto : mAllEServiceDtoList) {
			if (serviceDto.getTypeGestion().equals(DeclicConstants.TYPE_GESTION_NOMINATIF_KEY)) {
				serviceDto.setSendMail(true);
			}

			if (serviceDto.getListeSousMenus() != null) {
				for (MenuDto menuDto : serviceDto.getListeSousMenus()) {
					try {
						String libelleMenu = HabilitationUtil.returnLabelIfLabelKeyExists(DeclicConstants.SOUS_MENU_BEGIN_KEY, menuDto.getMenuKey(), localePrefix);
						String labelMenuParent = HabilitationUtil.returnLabelIfLabelKeyExists(DeclicConstants.MENU_BEGIN_KEY, menuDto.getMenuParentLabel(), localePrefix);
						if (libelleMenu != null) {
							menuDto.setLibelle(libelleMenu);
						} else {
							log.info("Impossible de récupérer le libellé du menu dans le fichier declic.properties : " + menuDto.getMenuKey());
						}

						if (labelMenuParent != null) {
							menuDto.setMenuParentLabel(labelMenuParent);
						} else {
							log.info("Impossible de récupérer le libellé du menu dans le fichier declic.properties : " + menuDto.getMenuParentLabel());
						}

					} catch (Exception ex) {
						log.info("Impossible de récupérer le libellé du menu dans le fichier declic.properties : " + menuDto.getMenuKey());
					}
				}
				List<MenuDto> menuDtos = serviceDto.getListeSousMenus();
				Collections.sort(menuDtos);
				serviceDto.setListeSousMenus(menuDtos);
			}
		}
		Collections.sort(mAllEServiceDtoList);
		request.setAttribute("listEService", mAllEServiceDtoList);
		return mapping.findForward("success");
	}

	/**
	 * Delete a e-service.
	 * 
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the action forward
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String idEService = request.getParameter("id");
		ServiceDto serviceDto = new ServiceDto();
		try {
			serviceDto.setId(Long.valueOf(idEService));
			ServiceProxy.deleteService(serviceDto);
		} catch (Exception ex) {
			log.error("Error on method delete e-service id : " + idEService);
			ActionErrors msgs = new ActionErrors();
			msgs.add("declic.admin.eservice.body.erreur.delete", new ActionMessage("declic.admin.eservice.body.erreur.delete"));
			this.saveErrors(request, msgs);
			return mapping.findForward("error");
		}
		List<ServiceDto> mAllEServiceDtoList = ServiceProxy.retrieveAllEServiceDtos();
		request.setAttribute("listEService", mAllEServiceDtoList);
		return mapping.findForward("success");
	}

	/**
	 * Change label of the menu Parent
	 * 
	 * @param menuDtos list of {@link MenuDto}
	 * @param request the request
	 * @return list of {@link MenuDto}
	 */
	private List<MenuDto> changeKeyMenuParentToLabel(List<MenuDto> menuDtos, HttpServletRequest request) {
		if (menuDtos == null || menuDtos.isEmpty()) {
			return menuDtos;
		}
		for (MenuDto menuDto : menuDtos) {
			String labelMenuParent = HabilitationUtil.returnLabelIfLabelKeyExists(DeclicConstants.MENU_BEGIN_KEY, menuDto.getMenuParentLabel(), getPrefixForLabelAccordingToLocal(request));
			menuDto.setMenuParentLabel(labelMenuParent);
		}
		return menuDtos;
	}

	/**
	 * Rajoute les libellés des menus pour l'affichage en fonction de la langue choisi par l'utilisateur
	 * 
	 * @param menuDto {@link MenuDto}
	 * @param locale String representant la locale
	 * @return
	 */
	private MenuDto changeLabelMenuParentAndLabelMenus(MenuDto menuDto, String locale) {
		if (menuDto == null) {
			return null;
		}
		try {
			String libelleMenu = HabilitationUtil.returnLabelIfLabelKeyExists(DeclicConstants.SOUS_MENU_BEGIN_KEY, menuDto.getMenuKey(), locale);
			String labelMenuParent = HabilitationUtil.returnLabelIfLabelKeyExists(DeclicConstants.MENU_BEGIN_KEY, menuDto.getMenuParentLabel(), locale);
			String[] menuLabels = HabilitationUtil.returnLabelClientAndCrcByMenuKey(menuDto, locale);
			if (libelleMenu != null) {
				menuDto.setLibelle(libelleMenu);
			} else {
				log.info("Impossible de récupérer le libellé du menu dans le fichier declicCommon.properties : " + menuDto.getMenuKey());
			}
			if (menuLabels != null && menuLabels.length >= numberLabelMenu) {
				menuDto.setLabelClient(menuLabels[0]);
				menuDto.setLabelCrc(menuLabels[1]);
			} else {
				log.info("Impossible de récupérer le libellé du menu dans le fichier declicCommon.properties : " + menuDto.getMenuKey());
			}
			if (labelMenuParent != null) {
				menuDto.setMenuParentLabel(labelMenuParent);
			}
		} catch (Exception ex) {
			log.info("Impossible de récupérer le libellé du menu dans le fichier declicCommon.properties : " + menuDto.getMenuKey());
		}
		return menuDto;
	}
}