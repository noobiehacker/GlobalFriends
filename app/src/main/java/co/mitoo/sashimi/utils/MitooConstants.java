package co.mitoo.sashimi.utils;

/**
 * Created by david on 15-01-21.
 */
public final class MitooConstants {
    
    public static final int invalidConstant = -1;
    public static final int searchRadius = 40233;
    public static boolean persistenceStorage = true;
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

    public static MitooEnum.SteakEnvironment steakEnvironment = MitooEnum.SteakEnvironment.STAGING;
}
