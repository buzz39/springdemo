/*
 * ---------------------------------------------------------------------------
 *
 * COPYRIGHT (c) 2016 Nuance Communications Inc.
 *
 * All Rights Reserved. Nuance Confidential.
 *
 * The copyright to the computer program(s) herein is the property of
 * Nuance Communications Inc. The program(s) may be used and/or copied
 * only with the written permission from Nuance Communications Inc.
 * or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 *
 *
 * ---------------------------------------------------------------------------
 */
package com.example.springsessionredis.application.model;

/**
 * Names of the variables stored in the HTTP sessions.
 * 
 * @author Nuance Communications
 */
public interface SessionConstants {

	// internally known names

	public final String HANGUP_REQUIRED = "hangupRequired";

	public final String GOTO_AGENT = "goToAgent";
	
	public final String GOTO_NLCS = "goToNLCS";
	
	public final String GOTO_SMS = "goToSMS";
	
	public final String GOTO_OTHERSERVICE = "goToOtherService";
	
	public final String SERVICE_TARGET = "serviceTarget";
	
	public final String INFOTAINMENT_REQUIRED = "infotainmentRequired";
	
	public final String AUTHENTICATED = "authenticated";
	
	public final String CUST_AUTH = "custAuth";
	
	public final String AUTHENTICATION_REQUIRED = "authenticationRequired";
	
	public final String WEBINFO_REQUIRED = "webInfoRequired";
	
	public final String FIRST_ENTRY = "firstEntry";

	public final String UR_BRAND = "urBrand";
	
	public final String APPLICATION_SERVER_HOST_NAME = "applicationServerHostname";
	
	public final String IVR_CLI_TYPE = "ivrCliType";
	
	//public final String UR_SRV_BRAND_ID = "urSrvBrandId";
	
	public final String COMING_FROM = "comingFrom";
	
	public final String MODULE_TARGET = "moduleTarget";

	public final String MODULE_ID = "moduleID";
	
	public final String MODULE_INVOKED = "moduleInvoked";
	
	// externally known names
	
	public final String IVR_ORIGINAL_ENTRY = "ivrOriginalEntry";

	public final String IVR_CUSTOMER_SEGMENT = "ivrCustomerSegment";
	
	public final String IVR_CALLER_SEGMENT  = "ivrCallerSegment";

	public final String IVR_OPEN_SERVICES = "ivrOpenServices";

	public final String IVR_MSISDN = "msisdn";

	public final String IVR_ID_TYPE = "ivrIdType";

	public final String IVR_ID_VALUE = "ivrIdValue";
	
	public final String UR_FIRST_CONNID = "urFirstConnId";
	
	public final String IVR_SMS_SENT = "IVR_SMSsent";

    public final String IVR_LANG = "ivrLang";

	/**
	 * Possible values: SUCCESS, FAILURE, ERROR, EVENT
	 */
	public final String RETURNCODE = "returncode";

	public final String APPLICATIONTAG = "applicationtag";

	public final String TRANSFER_TO = "transferTo";
	
	// database related
	
	public final String PKK = "pkk";
	
	public final String VVL = "vvl";
	
	public final String VVL_LAST = "vvlLast";
	
	public final String WINBACK = "winback";
	
	public final String BRANDCOLOR = "brandColor";
	
	public final String TRANSFER_TARGET_NOT_IN_OPENSERVICES = "transferTargetNotInOpenServices";

	public final String VASA_FORCES_HANGUP = "vasa_forces_hang_up";

	public final String SIM_NUMBER = "simnumber";

	// reporting related

	public final String REPORTING_MANAGER = "reportingManager";

	public final String KPI = "kpi";

	public final String CORBA_ACCESSIBLE = "CorbaAccessible";

	public final String IVR_CLI = "ivrCLI";

	public final String IVR_CRS = "ivrCRS";

	public final String IVR_DSL = "ivrDSL";

	public final String IVR_GREETED = "ivrGreeted";

	public final String IVR_PKK = "ivrPKK";

	public final String UR_CLIR = "urClir";

	public final String UR_SERVICE = "urService";

	public final String UR_ORIGINAL_ENTRY = "urOriginalEntry";

	public final String IVR_SERVICE = "ivrService";

	public final String IVR_ID_RESULT = "ivrIdResult";

	public final String IVR_MOBILE = "ivrMobile";

	public final String IVR_VVL = "ivrVVL";

	public final String UR_CALL_SOURCE = "urCallSource";

	public final String UR_ORIG_DNIS = "urOrigDnis";

	public final String IVR_RETURN = "ivrReturn";

	public final String SMS_TEXT = "smsText";

	public final String ACTIVATION_MESSAGE = "activationMessage";
	
	public final String ACTIVATION_STATUS = "activationStatus";

	public final String ACTIVATION_ORDER_ID = "activationOrderId";
	
	public final String CUSTOMERID = "custId";

	public final String SELF_SERVICES_STARTED = "selfServicesStarted";

	public final String SELF_SERVICES_COMPLETED = "selfServicesCompleted";

	public final String SELF_SERVICES_ABORTED = "selfServicesAborted";

    public final Object ABORT_SELF_SERVICE = "abortSelfService";

    public final String START_SELF_SERVICE = "startSelfService";
    
    public final String COMPLETE_SELF_SERVICE = "completeSelfService";
	
    public final String SELF_SERVICE_NAME = "name";
	
    //public final String SELF_SERVICE_ACTION = "selfServiceAction";
	
    public final String OH = "oh";
	
    public final String HNPRODL = "HNPRODL";

    public final String O2PRODL = "O2PRODL";

	//Usage info session constants - start
	public final String DAYS_LEFT_IN_CURRENT_BILL_CYCLE =  "DaysLeftInCurrentBillCycle";
	public final String BIG_INCIDENT_PLAYED = "bigIncidentPlayed";

	public final String VOICE_O2_NET =  "VoiceO2Net";
	public final String ORDER_STATUS_PLAYED = "orderStatusPlayed";

	public final String VOICE_FIXED_NET =  "VoiceFixedNet";
	public final String VASA_INCIDENT_PLAYED = "vasaIncidentPlayed";

	public final String VOICE_OTHER_DOMESTIC_MOBILE_NET =  "VoiceOtherDomesticMobileNet";

	public final String VOICE_INTERNATIONAL =  "VoiceInternational";

	public final String VOICE_PREMIUM =  "VoicePremium";
	
	public final String VOICE_TOTAL = "VoiceTotal";

	public final String SMS_TOTAL =  "SMSTotal";

	public final String MOBILE_DATA_MB =  "MobileDataMB";

	public final String MOBILE_DATA_MINUTES =  "MobileDataMinutes";
	//Usage info session constants - end
	
	//Address Topics session constants - start
	public final String AREA_CODE = "areaCode";
	//Address Topics session constants - end
	
	//Added to have the logging for WhatNext condition in hl0010_WhatsNext_DS - start
	public final String WHATS_NEXT_CONDITION  = "whatsNextCondition";
	//Added to have the logging for WhatNext condition in hl0010_WhatsNext_DS - end
	
	//Added to sync the hl0010_WhatsNext_DS Phase 2 update- start
	public final String SIM_ORDER_GET_ADDRESS = "simOrderGetAdress";
	public static final String SIM_REPLACEMENT_NEW_AUTH_NEEDED = "simReplaceNewAuthNeeded";
	public static final String SIM_NEW_CARD_NEW_AUTH_NEEDED = "simNewCardNewAuthNeeded";
	public static final String REPLACEMENT_FOR_BARRED_SIM = "replacementForBarredSim";
	//Added to sync the hl0010_WhatsNext_DS Phase 2 update- end
	
	//Added ThirdParty and DataContract Return variables Phase 2 update- start
	public final String FORCE_CALLER_TO_HANGUP = "forceCallerToHangup";
	//Added ThirdParty and DataContract Return variables Phase 2 update- end

	//Added for Address information from LookUPInit CDP - start
	public final String COMPANY_NAME = "companyName";
	public final String FIRST_NAME = "firstName";
	public final String LAST_NAME = "lastName";
	public final String ADDRESS_ID = "addressId";
	public final String ADDRESS_NAME = "addressName";
	public final String SUBSCRIPTION_ID = "subscriptionId";
	public final String SIM_ORDER_ACTION = "simOrderAction";
	//Added for Address information from LookUPInit CDP - start

	public final String MIS = "MIS";

	public final String SIM_CARD_ID_INDEX = "simcardIdIndex";
    public final String NUMBER_OF_ACTIVE_SIMS = "numberOfActiveSIMs";
    public final String CURRENT_SIMCARD_ID = "currentSIMCardId";
    public final String SIM_CARD_ID_RETURNED = "simcardIdReturned";

	public final String SIM_NUMBERTOTRY = "numberOfTry";
	public final String SIM_FIRSTSIMNRWRONGCOUNTER = "firstSimNrWrongCounter";
	public final String SIM_SV2610RETRIED = "sv2610Retried";
	public static final String FIXNET_NUMBER = "fixnetNumber";
	public static final String IS_VALID = "isValid";
	public static final String HAS_ACTIVE_SUBCRIPTIONS = "hasActiveSubcriptions";

}
