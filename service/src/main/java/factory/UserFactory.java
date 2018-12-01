package factory;

import com.workbei.constant.WbConstant;
import com.workbei.model.domain.user.*;

/**
 * @author Wallace Mao
 * Date: 2018-11-28 10:26
 */
public class UserFactory {
    public static WbAccountDO getAccountDO(){
        WbAccountDO accountDO = new WbAccountDO();
        accountDO.setAccountLocked(false);
        accountDO.setCheckEmail(true);
        return accountDO;
    }

    public static WbUserRegisterDO getUserRegisterDO(){
        WbUserRegisterDO userRegisterDO = new WbUserRegisterDO();
        userRegisterDO.setSystem(false);
        return userRegisterDO;
    }

    public static WbUserOauthDO getUserOauthDO(){
        WbUserOauthDO userOauthDO = new WbUserOauthDO();
        return userOauthDO;
    }

    public static WbUserDO getUserDO(){
        WbUserDO userDO = new WbUserDO();
        userDO.setDisplay(true);
        userDO.setParent(false);
        return userDO;
    }

    public static WbUserGuideDO getUserGuideDO(){
        WbUserGuideDO userGuideDO = new WbUserGuideDO();
        userGuideDO.setGuideWebFlag(true);
        userGuideDO.setGuideTaskFlag(true);
        userGuideDO.setGuideKanbanFlag(true);
        userGuideDO.setGuideNoteFlag(true);
        return userGuideDO;
    }

    public static WbUserDisplayOrderDO getUserDisplayOrderDO(){
        WbUserDisplayOrderDO userDisplayOrderDO = new WbUserDisplayOrderDO();
        final Double DEFAULT_MAX = 65535d;
        final Double DEFAULT_MIN = 65534d;
        userDisplayOrderDO.setMaxCorpusDisplayOrder(DEFAULT_MAX);
        userDisplayOrderDO.setMinCorpusDisplayOrder(DEFAULT_MIN);
        userDisplayOrderDO.setMaxStarCorpusDisplayOrder(DEFAULT_MAX);
        userDisplayOrderDO.setMinStarCorpusDisplayOrder(DEFAULT_MIN);
        userDisplayOrderDO.setMaxKanbanDisplayOrder(DEFAULT_MAX);
        userDisplayOrderDO.setMinKanbanDisplayOrder(DEFAULT_MIN);
        userDisplayOrderDO.setMaxStarKanbanDisplayOrder(DEFAULT_MAX);
        userDisplayOrderDO.setMinKanbanDisplayOrder(DEFAULT_MIN);

        return userDisplayOrderDO;
    }

    public static WbUserRoleGroupDO getUserRoleGroupDO(){
        return new WbUserRoleGroupDO();
    }

    public static WbUserUiSettingDO getUserUiSettingDO(){
        WbUserUiSettingDO userUiSettingDO = new WbUserUiSettingDO();
        userUiSettingDO.setThemeSkin(WbConstant.USER_THEME_SKIN_DEFAULT);
        userUiSettingDO.setNarrowTheInbox(false);
        userUiSettingDO.setNarrowTheKanban(false);
        userUiSettingDO.setNarrowTheCorpus(false);
        userUiSettingDO.setNarrowTheCompany(false);
        userUiSettingDO.setNarrowTheApplication(false);
        userUiSettingDO.setRegistAlertFlag(true);
        userUiSettingDO.setCompanyView(WbConstant.TEAM_COMPANY_VIEW_INDEX);
        userUiSettingDO.setShowTipBox(true);
        userUiSettingDO.setExpireAlertFlag(true);
        userUiSettingDO.setLunarFlag(true);
        userUiSettingDO.setFestivalFlag(true);
        userUiSettingDO.setDayAlertTime("09:00");
        userUiSettingDO.setPhoneThemeSkin("basic0");
        userUiSettingDO.setInboxMode("simple");

        return userUiSettingDO;
    }

    public static WbUserFunctionSettingDO getUserFunctionSettingDO(){
        WbUserFunctionSettingDO userFunctionSettingDO = new WbUserFunctionSettingDO();
        userFunctionSettingDO.setWeekBegins("Sun");
        userFunctionSettingDO.setOpenNotificationPlugin(true);
        userFunctionSettingDO.setReadCollageCount(WbConstant.TEAM_READ_COLLAGE_COUNT);
        userFunctionSettingDO.setShakeMode(true);

        return userFunctionSettingDO;
    }

    public static WbOuterDataUserDO getOuterDataUserDO(){
        WbOuterDataUserDO outerDataUserDO = new WbOuterDataUserDO();
        return outerDataUserDO;
    }
}
