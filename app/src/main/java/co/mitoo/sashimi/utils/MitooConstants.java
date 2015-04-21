package co.mitoo.sashimi.utils;

import co.mitoo.sashimi.BuildConfig;

/**
 * Created by david on 15-01-21.
 */
public final class MitooConstants {
    
    public static final int invalidConstant = -1;
    public static final int searchRadius = 40233;
    public static final int faqOption = 1437;

    //Field min and max length

    public static int minUserNameLength = 3;
    public static int minEmailLength = 5;
    public static int minPhoneLength = 10;
    public static int minPasswordLength = 8;

    public static int maxUserNameLength = 100;
    public static int maxEmailLength = 100;
    public static int maxPhoneLength = 11;
    public static int maxPasswordLength = 100;
    
    public static int requiredPhoneDigits =10;

    public static int feedBackPopUpTime =8000;
    public static int maxLeagueCharacterName =38;

    public static int termsSpinnerNumber =0;
    public static int privacySpinnerNumber =1;

    public static int durationShort =250;
    public static int durationMedium =500;
    public static int standingHead =-97123;

    public static int durationLong =750;
    public static int durationExtraLong =1000;

    public static MitooEnum.SteakEndPoint mitooDevelopmentEndPoint =MitooEnum.SteakEndPoint.STAGING;

    public static boolean getPersistenceStorage() {
        return true;
    }

    public static MitooEnum.SteakEndPoint getEndPoint() {

        if(BuildConfig.FLAVOR.equalsIgnoreCase(MitooEnum.AppEnvironment.PRODUCTION.name()))
            return MitooEnum.SteakEndPoint.PRODUCTION;
        else if(BuildConfig.FLAVOR.equalsIgnoreCase(MitooEnum.AppEnvironment.STAGING.name())){
            return MitooEnum.SteakEndPoint.STAGING;
        }else if(BuildConfig.FLAVOR.equalsIgnoreCase(MitooEnum.AppEnvironment.LOCALHOST.name())){
            return MitooEnum.SteakEndPoint.LOCALHOST;
        }
        else
            return mitooDevelopmentEndPoint;
    }

    public static MitooEnum.AppEnvironment getAppEnvironment() {

        if(BuildConfig.FLAVOR.equalsIgnoreCase(MitooEnum.AppEnvironment.PRODUCTION.name()))
            return MitooEnum.AppEnvironment.PRODUCTION;
        else if(BuildConfig.FLAVOR.equalsIgnoreCase(MitooEnum.AppEnvironment.STAGING.name()))
            return MitooEnum.AppEnvironment.STAGING;
        else if(BuildConfig.FLAVOR.equalsIgnoreCase(MitooEnum.AppEnvironment.LOCALHOST.name()))
            return MitooEnum.AppEnvironment.LOCALHOST;
        else
            return MitooEnum.AppEnvironment.STAGING;

    }
}