package org.databunker.options;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to convert option objects to Map<String, Object> for backward compatibility
 */
public class OptionsConverter {
    
    /**
     * Converts BasicOptions to Map<String, Object>
     * @param options The BasicOptions object
     * @return A Map representation of the options
     */
    public static Map<String, Object> toMap(BasicOptions options) {
        Map<String, Object> map = new HashMap<>();
        if (options != null) {
            if (options.getFinaltime() != null) {
                map.put("finaltime", options.getFinaltime());
            }
            if (options.getSlidingtime() != null) {
                map.put("slidingtime", options.getSlidingtime());
            }
        }
        return map;
    }
    
    /**
     * Converts UserOptions to Map<String, Object>
     * @param options The UserOptions object
     * @return A Map representation of the options
     */
    public static Map<String, Object> toMap(UserOptions options) {
        Map<String, Object> map = new HashMap<>();
        if (options != null) {
            if (options.getGroupname() != null) {
                map.put("groupname", options.getGroupname());
            }
            if (options.getGroupid() != null) {
                map.put("groupid", options.getGroupid());
            }
            if (options.getRolename() != null) {
                map.put("rolename", options.getRolename());
            }
            if (options.getRoleid() != null) {
                map.put("roleid", options.getRoleid());
            }
            if (options.getSlidingtime() != null) {
                map.put("slidingtime", options.getSlidingtime());
            }
            if (options.getFinaltime() != null) {
                map.put("finaltime", options.getFinaltime());
            }
        }
        return map;
    }
    
    /**
     * Converts SharedRecordOptions to Map<String, Object>
     * @param options The SharedRecordOptions object
     * @return A Map representation of the options
     */
    public static Map<String, Object> toMap(SharedRecordOptions options) {
        Map<String, Object> map = new HashMap<>();
        if (options != null) {
            if (options.getFields() != null) {
                map.put("fields", options.getFields());
            }
            if (options.getPartner() != null) {
                map.put("partner", options.getPartner());
            }
            if (options.getAppname() != null) {
                map.put("appname", options.getAppname());
            }
            if (options.getFinaltime() != null) {
                map.put("finaltime", options.getFinaltime());
            }
        }
        return map;
    }

    /**
     * Converts LegalBasisOptions to Map<String, Object>
     * @param options The LegalBasisOptions object
     * @return A Map representation of the options
     */
    public static Map<String, Object> toMap(LegalBasisOptions options) {
        Map<String, Object> map = new HashMap<>();
        if (options != null) {
            if (options.getBrief() != null) {
                map.put("brief", options.getBrief());
            }
            if (options.getStatus() != null) {
                map.put("status", options.getStatus());
            }
            if (options.getModule() != null) {
                map.put("module", options.getModule());
            }
            if (options.getFulldesc() != null) {
                map.put("fulldesc", options.getFulldesc());
            }
            if (options.getShortdesc() != null) {
                map.put("shortdesc", options.getShortdesc());
            }
            if (options.getBasistype() != null) {
                map.put("basistype", options.getBasistype());
            }
            if (options.getRequiredmsg() != null) {
                map.put("requiredmsg", options.getRequiredmsg());
            }
            if (options.getRequiredflag() != null) {
                map.put("requiredflag", options.getRequiredflag());
            }
        }
        return map;
    }



    /**
     * Converts AgreementAcceptOptions to Map<String, Object>
     * @param options The AgreementAcceptOptions object
     * @return A Map representation of the options
     */
    public static Map<String, Object> toMap(AgreementAcceptOptions options) {
        Map<String, Object> map = new HashMap<>();
        if (options != null) {
            if (options.getAgreementmethod() != null) {
                map.put("agreementmethod", options.getAgreementmethod());
            }
            if (options.getReferencecode() != null) {
                map.put("referencecode", options.getReferencecode());
            }
            if (options.getStarttime() != null) {
                map.put("starttime", options.getStarttime());
            }
            if (options.getFinaltime() != null) {
                map.put("finaltime", options.getFinaltime());
            }
            if (options.getStatus() != null) {
                map.put("status", options.getStatus());
            }
            if (options.getLastmodifiedby() != null) {
                map.put("lastmodifiedby", options.getLastmodifiedby());
            }
        }
        return map;
    }

    /**
     * Converts ConnectorOptions to Map<String, Object>
     * @param options The ConnectorOptions object
     * @return A Map representation of the options
     */
    public static Map<String, Object> toMap(ConnectorOptions options) {
        Map<String, Object> map = new HashMap<>();
        if (options != null) {
            if (options.getConnectorid() != null) {
                map.put("connectorid", options.getConnectorid());
            }
            if (options.getConnectorname() != null) {
                map.put("connectorname", options.getConnectorname());
            }
            if (options.getConnectortype() != null) {
                map.put("connectortype", options.getConnectortype());
            }
            if (options.getApikey() != null) {
                map.put("apikey", options.getApikey());
            }
            if (options.getUsername() != null) {
                map.put("username", options.getUsername());
            }
            if (options.getConnectordesc() != null) {
                map.put("connectordesc", options.getConnectordesc());
            }
            if (options.getDbhost() != null) {
                map.put("dbhost", options.getDbhost());
            }
            if (options.getDbport() != null) {
                map.put("dbport", options.getDbport());
            }
            if (options.getDbname() != null) {
                map.put("dbname", options.getDbname());
            }
            if (options.getTablename() != null) {
                map.put("tablename", options.getTablename());
            }
            if (options.getStatus() != null) {
                map.put("status", options.getStatus());
            }
        }
        return map;
    }

    /**
     * Converts TenantOptions to Map<String, Object>
     * @param options The TenantOptions object
     * @return A Map representation of the options
     */
    public static Map<String, Object> toMap(TenantOptions options) {
        Map<String, Object> map = new HashMap<>();
        if (options != null) {
            if (options.getTenantname() != null) {
                map.put("tenantname", options.getTenantname());
            }
            if (options.getTenantorg() != null) {
                map.put("tenantorg", options.getTenantorg());
            }
            if (options.getEmail() != null) {
                map.put("email", options.getEmail());
            }
        }
        return map;
    }

    /**
     * Converts ProcessingActivityOptions to Map<String, Object>
     * @param options The ProcessingActivityOptions object
     * @return A Map representation of the options
     */
    public static Map<String, Object> toMap(ProcessingActivityOptions options) {
        Map<String, Object> map = new HashMap<>();
        if (options != null) {
            if (options.getActivity() != null) {
                map.put("activity", options.getActivity());
            }
            if (options.getTitle() != null) {
                map.put("title", options.getTitle());
            }
            if (options.getScript() != null) {
                map.put("script", options.getScript());
            }
            if (options.getFulldesc() != null) {
                map.put("fulldesc", options.getFulldesc());
            }
            if (options.getApplicableto() != null) {
                map.put("applicableto", options.getApplicableto());
            }
        }
        return map;
    }



    /**
     * Converts GroupOptions to Map<String, Object>
     * @param options The GroupOptions object
     * @return A Map representation of the options
     */
    public static Map<String, Object> toMap(GroupOptions options) {
        Map<String, Object> map = new HashMap<>();
        if (options != null) {
            if (options.getGroupname() != null) {
                map.put("groupname", options.getGroupname());
            }
            if (options.getGrouptype() != null) {
                map.put("grouptype", options.getGrouptype());
            }
            if (options.getGroupdesc() != null) {
                map.put("groupdesc", options.getGroupdesc());
            }
        }
        return map;
    }

    /**
     * Converts RoleOptions to Map<String, Object>
     * @param options The RoleOptions object
     * @return A Map representation of the options
     */
    public static Map<String, Object> toMap(RoleOptions options) {
        Map<String, Object> map = new HashMap<>();
        if (options != null) {
            if (options.getRolename() != null) {
                map.put("rolename", options.getRolename());
            }
            if (options.getRoledesc() != null) {
                map.put("roledesc", options.getRoledesc());
            }
        }
        return map;
    }

    /**
     * Converts TokenOptions to Map<String, Object>
     * @param options The TokenOptions object
     * @return A Map representation of the options
     */
    public static Map<String, Object> toMap(TokenOptions options) {
        Map<String, Object> map = new HashMap<>();
        if (options != null) {
            if (options.getUnique() != null) {
                map.put("unique", options.getUnique());
            }
            if (options.getSlidingtime() != null) {
                map.put("slidingtime", options.getSlidingtime());
            }
            if (options.getFinaltime() != null) {
                map.put("finaltime", options.getFinaltime());
            }
        }
        return map;
    }

    /**
     * Converts PolicyOptions to Map<String, Object>
     * @param options The PolicyOptions object
     * @return A Map representation of the options
     */
    public static Map<String, Object> toMap(PolicyOptions options) {
        Map<String, Object> map = new HashMap<>();
        if (options != null) {
            if (options.getPolicyname() != null) {
                map.put("policyname", options.getPolicyname());
            }
            if (options.getPolicydesc() != null) {
                map.put("policydesc", options.getPolicydesc());
            }
            if (options.getPolicy() != null) {
                map.put("policy", options.getPolicy());
            }
        }
        return map;
    }
} 