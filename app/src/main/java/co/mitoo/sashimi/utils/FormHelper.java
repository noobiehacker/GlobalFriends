package co.mitoo.sashimi.utils;

import co.mitoo.sashimi.R;
import co.mitoo.sashimi.views.activities.MitooActivity;
import co.mitoo.sashimi.views.fragments.MitooFragment;

/**
 * Created by david on 15-02-20.
 */
public class FormHelper {

    private MitooActivity activity;
    private MitooFragment fragment;

    public FormHelper(MitooFragment fragment) {
        this.fragment = fragment;
    }

    public MitooActivity getActivity() {
        if(activity==null)
            activity=fragment.getMitooActivity();
        return activity;
    }

    public boolean validIdentifier(String input) {
        boolean result = false;
        if(input.contains("@"))
            result = validEmail(input);
        else
            result = validPhone(input);
        return result;
    }

    public boolean validName(String input) {
        boolean result = false;
        result = stringLengthBetween(input, MitooConstants.minUserNameLength,
                MitooConstants.maxUserNameLength);
        return result;
    }

    public boolean validEmail(String input) {
        boolean result = false;
        result = stringLengthBetween(input, MitooConstants.minEmailLength,
                MitooConstants.maxEmailLength) && validEmailString(input);
        return result;
    }

    public boolean validPhone(String input) {
        boolean result = false;
        String numericOnlyInput = reduceToNumeric(input);
        result = stringLengthBetween(numericOnlyInput
                ,MitooConstants.minPhoneLength,MitooConstants.maxPhoneLength)
                && hasNumOfDigits(input, MitooConstants.requiredPhoneDigits);
        return result;
    }

    public boolean validPassword(String input) {
        boolean result = false;
        result = stringLengthBetween(input, MitooConstants.minPasswordLength,
                MitooConstants.maxPasswordLength);
        return result;
    }

    private boolean validEmailString(String input) {

        boolean result = false;
        if (input.contains("@") && input.contains(".")) {
            result = input.indexOf("@") < input.indexOf(".");
        }
        return result;

    }

    private boolean hasNumOfDigits(String input, int digitNum) {

        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            char value = input.charAt(i);
            if (value >= '0' && value <= '9')
                count++;
        }
        return count >= digitNum;

    }

    private boolean stringLengthBetween(String input, int lowerEnd, int higherEnd) {

        return input.length() >= lowerEnd && input.length() <= higherEnd;

    }

    public boolean passwordTooShort(String input){

        return stringLengthShorterThanBound(input, MitooConstants.minPasswordLength);

    }

    public boolean passwordTooLong(String input){

        return stringLengthLongerThanBound(input, MitooConstants.maxPasswordLength);

    }

    public boolean emailTooShort(String input){

        return stringLengthShorterThanBound(input, MitooConstants.minEmailLength);

    }

    public boolean emailTooLong(String input){

        return stringLengthLongerThanBound(input, MitooConstants.maxEmailLength);

    }

    public boolean userNameTooShort(String input){

        return stringLengthShorterThanBound(input, MitooConstants.minUserNameLength);

    }

    public boolean userNameTooLong(String input){

        return stringLengthLongerThanBound(input, MitooConstants.maxUserNameLength);

    }

    public boolean phoneTooShort(String input){

        return stringLengthShorterThanBound(input, MitooConstants.minPhoneLength);

    }

    public boolean phoneTooLong(String input){

        return stringLengthLongerThanBound(input, MitooConstants.maxPhoneLength);

    }
    
    private boolean stringLengthShorterThanBound(String input, int bound) {

        return input.length() < bound;

    }

    private boolean stringLengthLongerThanBound(String input, int bound) {

        return input.length() > bound;

    }

    public void handleInvalidIdentifier(String identifier){

        if(identifier.contains("@"))
            handleInvalidEmail(identifier);
        else
            handleInvalidPhone(identifier);

    }

    public void handleInvalidEmail(String email){

        if(emailTooShort(email))
            displayText(getActivity().getString(R.string.toast_email_length_too_short));
        else if(emailTooLong(email))
            displayText(getActivity().getString(R.string.toast_email_length_too_long));
        else
            displayText(getActivity().getString(R.string.toast_invalid_email));

    }

    public void handleInvalidPassword(String password){

        if(passwordTooShort(password))
            displayText(getActivity().getString(R.string.toast_password_length_too_short));
        else if(passwordTooLong(password))
            displayText(getActivity().getString(R.string.toast_password_length_too_long));
        else
            displayText(getActivity().getString(R.string.toast_invalid_password));
    }

    public void handleInvalidUserName(String username){
        if(userNameTooShort(username))
            displayText(getActivity().getString(R.string.toast_name_length_too_short));
        else if(userNameTooLong(username))
            displayText(getActivity().getString(R.string.toast_name_length_too_long));
        else
            displayText(getActivity().getString(R.string.toast_invalid_name));

    }

    public void handleInvalidPhone(String phone){
        if(phoneTooShort(phone))
            displayText(getActivity().getString(R.string.toast_phone_length_too_short));
        else if(phoneTooLong(phone))
            displayText(getActivity().getString(R.string.toast_phone_length_too_long));
        else
            displayText(getActivity().getString(R.string.toast_invalid_phone));

    }
    private void displayText(String text){
        
        getFragment().displayText(text);
    }

    private String reduceToNumeric(String input){

        String result = input;
        if(input!=null){
            StringBuilder sb = new StringBuilder();
            for(char c : input.toCharArray()){
                if(c>='0' && c <= '9')
                    sb.append(c);
            }
            result = sb.toString();

        }
        return result;
    }

    public MitooFragment getFragment() {
        return fragment;
    }
}
