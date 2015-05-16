package com.rtefrance.declic.portail.util;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static com.rtefrance.declic.common.validator.Validator.isNullOrEmpty;
import static com.rtefrance.declic.portail.common.util.DeclicPortailUtil.getSessionVariableWithCast;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;
import com.rtefrance.declic.common.dto.SocieteDto;
import com.rtefrance.declic.common.mecapa.dto.object.EdcAttachementSiteDto;
import com.rtefrance.declic.common.mecapa.dto.object.EdcDto;
import com.rtefrance.declic.common.mecapa.dto.object.EdcRpcDto;
import com.rtefrance.declic.common.utils.impl.HtmlUtils;
import com.rtefrance.declic.common.validator.Validator;
import com.rtefrance.declic.portail.habilitation.proxy.AccesSettingProxy;
import com.rtefrance.declic.portail.mecapa.constant.MecapaPortailConstants;
import com.rtefrance.declic.portail.mecapa.faces.AttachmentDemandRpcFaces;

/**
 * useful class
 * 
 * @author SQLI
 */
public final class MecapaUtils {

	/**
	 * Extract id from list.
	 * 
	 * @param societeDtos the societe dtos
	 * @return an array of String with the id of the society
	 */
	public static String[] extractIdFromList(List<SocieteDto> societeDtos) {
		String[] idSocieteArray = new String[societeDtos.size()];
		for (int i = 0; i < societeDtos.size(); i++) {
			idSocieteArray[i] = societeDtos.get(i).getIdSap();
		}
		return idSocieteArray;
	}

	/**
	 * Retrieve a edc by his code
	 * 
	 * @param codeEdc
	 * @param edcDtoList
	 * @return
	 */
	public static EdcDto retrieveEdcByCode(String codeEdc, List<EdcDto> edcDtoList) {
		if (Validator.isListEmpty(edcDtoList) || Strings.isNullOrEmpty(codeEdc)) {
			return null;
		}
		for (EdcDto edcDto : edcDtoList) {
			if (codeEdc.equals(edcDto.getCode())) {
				return edcDto;
			}
		}
		return null;
	}

	/**
	 * Method to retrieve a site by his sap code
	 * 
	 * @param sapCode
	 * @param edcCodeSelected
	 * @param request
	 * @return
	 */
	public static EdcAttachementSiteDto retrieveSiteBySapCode(String sapCode, List<EdcAttachementSiteDto> attachementSiteDtos) {
		for (EdcAttachementSiteDto edcAttachementSiteDto : attachementSiteDtos) {
			if (sapCode.equals(edcAttachementSiteDto.getEdcSiteDto().getSapCode())) {
				return edcAttachementSiteDto;
			}
		}
		return null;
	}

	/**
	 * Method to retrieve the RPC by his sap code
	 * 
	 * @param edcCodeSelected
	 * @param attribute
	 * @return
	 */
	public static EdcRpcDto retrieveRpcByCodeSap(String edcCodeSelected, List<EdcRpcDto> listRpc) {
		for (EdcRpcDto edcRpcDto : listRpc) {
			if (edcCodeSelected.equals(edcRpcDto.getSapCodeSociete())) {
				return edcRpcDto;
			}
		}
		return null;
	}

	/**
	 * @param edcCode
	 * @param edcDtos
	 * @return
	 */
	public static boolean hasAttachmentRpc(String edcCode, HttpServletRequest request) {
		EdcDto edcDto = MecapaUtils.retrieveEdcByCode(edcCode, getEdcDtoList(request));
		if (Validator.isListEmpty(edcDto.getRpcDtoList())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Get list of Label action of Mecapa
	 * 
	 * @return list of the actions' label of mecapa
	 */
	public static List<String> getActionLabelMecapaSet() {
		Set<String> menuLabelsMecapaSet = newLinkedHashSet(Arrays.asList(MecapaPortailConstants.MENU_LABELS_MECAPA));
		return AccesSettingProxy.retrieveLibelleActionsByMenu(menuLabelsMecapaSet);
	}

	/**
	 * Get the list of {@link EdcDto}. This variable in session is used a lot
	 * 
	 * @param request the request
	 * @return list of {@link EdcDto}
	 */
	public static List<EdcDto> getEdcDtoList(HttpServletRequest request) {
		return getSessionVariableWithCast(MecapaPortailConstants.EDC_DTO_LIST, request);
	}

	/**
	 * Decode all html character in informations of a List of {@link AttachmentDemandRpcFaces}
	 * 
	 * @param attachmentDemandRpcFaces List of {@link AttachmentDemandRpcFaces}
	 * @return List of {@link AttachmentDemandRpcFaces} with html code in informations decoded
	 */
	public static List<AttachmentDemandRpcFaces> decodeHtmlInInformations(List<AttachmentDemandRpcFaces> attachmentDemandRpcFaces) {
		if (!isNullOrEmpty(attachmentDemandRpcFaces)) {
			for (AttachmentDemandRpcFaces currentAttachmentDemandRpcFaces : attachmentDemandRpcFaces) {
				currentAttachmentDemandRpcFaces.setInformations(HtmlUtils.decode(currentAttachmentDemandRpcFaces.getInformations()));
			}
		}
		return attachmentDemandRpcFaces;
	}

}
