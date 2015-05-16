/**
 * 
 */
package com.rtefrance.declic.portail.admin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.rtefrance.declic.common.admin.dto.MailContentDto;
import com.rtefrance.declic.common.constant.DeclicConstants;
import com.rtefrance.declic.common.dto.ContextUserDto;
import com.rtefrance.declic.common.dto.LabelValueDto;
import com.rtefrance.declic.common.exception.InfrastructureException;
import com.rtefrance.declic.common.log.LogFactory;
import com.rtefrance.declic.portail.admin.form.MailContentForm;
import com.rtefrance.declic.portail.common.action.AbstractDeclicMappingDispachAction;
import com.rtefrance.declic.portail.common.proxy.MailContentProxy;
import com.rtefrance.declic.portail.common.resources.ResourcesFactory;

/**
 * Classe responsable des actions d'affichage et de gestion des paramètres d'envoi de mails au CRC des régions.
 * 
 * @author athearud
 */
public class AdminMailContentAction extends AbstractDeclicMappingDispachAction {

	/** log de l'action. */
	private Logger log = LogFactory.getLog(getClass());

	/**
	 * Action permettant l'affichage de la liste des contenus de mail enregistrés en base.
	 * 
	 * @param pMapping the mapping
	 * @param pForm the form
	 * @param pRequest the request
	 * @param pResponse the response
	 * @return the action forward
	 */
	public ActionForward display(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		// Récupération de la liste des contenus de mail
		this.loadMailContent(pRequest);
		return pMapping.findForward("success");
	}

	/**
	 * Action permettant l'initialisation du formulaire de modification du contenu d'un mail.
	 * 
	 * @param pMapping the mapping
	 * @param pForm the form
	 * @param pRequest the request
	 * @param pResponse the response
	 * @return the action forward
	 */
	public ActionForward init(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) {

		MailContentForm mMailContentForm = (MailContentForm) pForm;

		// Récupération du contenu de mail à modifier via son code, si il s'agit d'une modification
		String mCode = pRequest.getParameter("code");
		if (mCode != null) {
			MailContentDto mailContentDto = MailContentProxy.retrieveMailContent(mCode);
			if (mailContentDto != null) {
				// alimentation du form mMailContentForm
				mMailContentForm.setName(mailContentDto.getName());
				mMailContentForm.setCode(mailContentDto.getCode());
				mMailContentForm.setObjectMail(mailContentDto.getObject());
				mMailContentForm.setBodyMail(mailContentDto.getBody());
				mMailContentForm.setActivated(mailContentDto.isActivated());
			} else {

				return pMapping.findForward("success");
			}
		} else {
			// Vérification si il s'agit de la création d'une nouvelle région
			String mNewMailContent = pRequest.getParameter("newMailContent");
			if (mNewMailContent != null) {
				// Reinitialisation de la mMailContentForm
				mMailContentForm.setName(null);
				mMailContentForm.setCode(null);
				mMailContentForm.setCodeNewMailContent(null);
				mMailContentForm.setObjectMail(null);
				mMailContentForm.setBodyMail(null);
				// Contenu de mail activé par défaut lors de sa création
				mMailContentForm.setActivated(true);
			}
		}
		return pMapping.findForward("success");
	}

	/**
	 * Action permettant de mettre à jour ou de créer une région donnée (Ecriture en base).
	 * 
	 * @param pMapping the mapping
	 * @param pForm the form
	 * @param pRequest the request
	 * @param pResponse the response
	 * @return the action forward
	 */
	public ActionForward saveOrUpdate(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) {

		MailContentForm mMailContentForm = (MailContentForm) pForm;

		// Validation du formulaire
		ActionErrors mErrors = mMailContentForm.validate(pMapping, pRequest);
		// On vérifie que si, il s'agit d'une nouvelle région, le code n'existe pas déjà en base
		if (MailContentProxy.isCodeMailContentExists(mMailContentForm.getCodeNewMailContent()).booleanValue()) {
			mErrors.add("objectMail", new ActionMessage("declic.admin.gestion.contenu.mail.duplicatedCodeMailContent"));
		}
		if (!mErrors.isEmpty()) {
			this.saveErrors(pRequest, mErrors);
			return pMapping.getInputForward();
		}

		MailContentDto mMailContentDto = new MailContentDto();

		String mCode = "";
		if (!mMailContentForm.getCode().equals("")) {
			mCode = mMailContentForm.getCode();
		} else if (!mMailContentForm.getCodeNewMailContent().equals("")) {
			mCode = mMailContentForm.getCodeNewMailContent();
		}

		// Récupération des données du formulaire
		mMailContentDto.setName(mMailContentForm.getName());
		mMailContentDto.setCode(mCode);
		mMailContentDto.setObject(mMailContentForm.getObjectMail());
		mMailContentDto.setBody(mMailContentForm.getBodyMail());
		mMailContentDto.setActivated(mMailContentForm.getActivated());

		// Validation des mails de la région
		MailContentProxy.saveOrUpdateMailContent(mMailContentDto);
		if (log.isInfoEnabled()) {
			ContextUserDto mContextUserDto = (ContextUserDto) pRequest.getSession().getAttribute(DeclicConstants.LDAP_CONTEXT_REMOTE_USER);
			String mLogin = mContextUserDto.getUserLogin();
			log.info("Modify mail content, user=" + mLogin + ", code =" + mMailContentDto.getCode());
		}

		String[] mArgMessage = {mMailContentForm.getName()};
		pRequest.setAttribute("saveMailContentMessage", ResourcesFactory.getMessage(pRequest, "declic.admin.gestion.mail.region.saveRegion.message", mArgMessage));

		return pMapping.findForward("success");
	}

	/**
	 * Action permettant la suppression d'un contenu de mail (écriture en base).
	 * 
	 * @param pMapping the mapping
	 * @param pForm the form
	 * @param pRequest the request
	 * @param pResponse the response
	 * @return the action forward
	 */
	public ActionForward delete(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		// Récupération du code de la région à supprimer
		String mCodeContentMail = pRequest.getParameter("code");

		// Suppression du contenu du mail
		MailContentProxy.deleteMailContent(mCodeContentMail);
		return pMapping.findForward("success");
	}

	/**
	 * Récupération de la liste des contenus de mail pour l'affichage.
	 * 
	 * @param pRequest the request
	 * @throws InfrastructureException the infrastructure exception
	 */
	private void loadMailContent(HttpServletRequest pRequest) throws InfrastructureException {
		// Récupération de la liste des régions pour l'affichage
		List mListMailContentDto = MailContentProxy.getAllMailContent();
		pRequest.setAttribute("listMailContent", mListMailContentDto);
	}

	/**
	 * Gets the path first.
	 * 
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the path first
	 * @see com.rtefrance.declic.portail.common.action.AbstractDeclicMappingDispachAction#getPathFirst()
	 */
	protected LabelValueDto getPathFirst(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return new LabelValueDto("/adminMailContentDisplay.do", ResourcesFactory.getMessage(request, "declic.navigation.rubrique.gestionContenuMail"));
	}

	/**
	 * Gets the path second.
	 * 
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the path second
	 * @see com.rtefrance.declic.portail.common.action.AbstractDeclicMappingDispachAction#getPathSecond(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
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
	 * @see com.rtefrance.declic.portail.common.action.AbstractDeclicMappingDispachAction#getTitle(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String getTitle(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return ResourcesFactory.getMessage(request, "declic.navigation.rubrique.gestionContenuMail");
	}

	/**
	 * Report functionnality access.
	 * 
	 * @param mapping the mapping
	 * @param form the form
	 * @param request the request
	 * @param response the response
	 * @return the string
	 * @see com.rtefrance.declic.portail.common.action.AbstractDeclicMappingDispachAction#reportFunctionnalityAccess(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected String reportFunctionnalityAccess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return null;
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
	protected String getFunctionnality(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
}
