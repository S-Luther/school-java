/*
    Chap 9 Book Program
    Password
    Bernard Kintzing
 */

import java.util.*;

public class Password{
    final static int MIN_SIZE = 6;
    final static int MAX_SIZE = 15;
    static int maxHistory = 4;
    static int expiresNotifyLimit = 3;

    private int maxUses = 120;
    private int remainingUses = maxUses;
    private boolean autoExpires = true;
    private boolean expired = false;

    private ArrayList<String> pswdHistory;

    //Constructors for objects of class Password
    public Password(String newPassword, int numMaxUses) throws Exception{
        pswdHistory = new ArrayList<String>(maxHistory);
        maxUses = numMaxUses;
        remainingUses = numMaxUses;
        set(newPassword);
    }

    public Password(String newPassword, boolean pswdAutoExpires) throws Exception{
        pswdHistory = new ArrayList<String>(maxHistory);
        autoExpires = pswdAutoExpires;
        set(newPassword);
    }

    public Password(String newPassword, int numMaxUses, boolean pswdAutoExpires) throws Exception{
        pswdHistory = new ArrayList<String>(maxHistory);
        maxUses = numMaxUses;
        remainingUses = numMaxUses;
        autoExpires = pswdAutoExpires;
        set(newPassword);
    }

    public boolean getAutoExpires(){
        return autoExpires;
    }

    public void setAutoExpires(boolean autoExpires){
        this.autoExpires = autoExpires;
        if(autoExpires)
            remainingUses = maxUses;
    }

    public boolean isExpired(){
        return expired;
    }

    public void setExpired(boolean newExpired){
        expired = newExpired;
    }

    public int getExpiresNotifyLimit(){
        return expiresNotifyLimit;
    }

    public void setExpiresNotifyLimit(int newNotifyLimit){
        if(newNotifyLimit >= 2 && newNotifyLimit <= 20)
            expiresNotifyLimit = newNotifyLimit;
    }

    public int getMaxHistory(){
        return maxHistory;
    }

    public void setMaxHistory(int newMaxHistory){
        int overage = 0;
        if(newMaxHistory >= 1 && newMaxHistory <10){
            maxHistory = newMaxHistory;
            if(overage > 0){
                do{
                    pswdHistory.remove(0);
                    overage--;
                }while(overage > 0);

                pswdHistory.trimToSize();
            }
        }
    }

    public int getRemainingUses(){
        return remainingUses;
    }

    public int getHistorySize(){
        return pswdHistory.size();
    }

    public boolean isExpiring(){
        boolean expiring = false;
        if(autoExpires && remainingUses <= expiresNotifyLimit)
            expiring = true;
        return expiring;
    }

    public void set(String pswd) throws Exception{
        String encryptPswd;
        boolean pswdAdded = true;
        pswd = pswd.trim();
        verifyFormat(pswd);
        encryptPswd = encrypt(pswd);

        if(!pswdHistory.contains(encryptPswd)){
            if(pswdHistory.size() == maxHistory)
                pswdHistory.remove(0);

                pswdAdded = pswdHistory.add(encryptPswd);

                if(!pswdAdded)
                    throw new Exception("Internal list error - password not excepted");
                if(expired)
                    expired = false;
                if(autoExpires)
                    remainingUses = maxUses;
        }
        else
            throw new Exception("Password recently used");
    }

    public void validate(String pswd) throws Exception{
        String encryptPswd;
        String currentPswd;
        int currentPswdIndex;
        verifyFormat(pswd);
        encryptPswd = encrypt(pswd);
        if(!pswdHistory.isEmpty()){
            currentPswdIndex = pswdHistory.size()-1;
            currentPswd = pswdHistory.get(currentPswdIndex);

            if(!encryptPswd.equals(currentPswd))
                throw new Exception("Password invalid");

            if(expired)
                throw new Exception("Password has expired - please change");

            if(autoExpires){
                --remainingUses;
                if(remainingUses <= 0)
                    expired = true;
            }
            else
                throw new Exception("NoPassword on file - list corrupted");
        }
    }

    private void verifyFormat(String pswd) throws Exception {
        boolean numFound = false;
        if (pswd.length() == 0)
            throw new Exception("No password provided");

        if(pswd.length() < MIN_SIZE)
            throw new Exception("Password must be a least " +MIN_SIZE+ " characters in length");

        if(pswd.length() > MAX_SIZE)
            throw new Exception("Password can not be greater than " +MAX_SIZE+ " characters in length");

        for(int i=0; i<pswd.length() && !numFound; ++i)
            if(Character.isDigit(pswd.charAt(i)))
                numFound = true;

        if(!numFound)
            throw new Exception("Password is invalid - must have at least one numeric digit");
    }

    private String encrypt(String pswd){
        StringBuffer encryptPswd;
        int pswdSize = 0;
        int midpoint = 0;
        int hashCode = 0;

        pswdSize = pswd.length();
        midpoint = pswdSize/2;
        encryptPswd = new StringBuffer(pswd.substring(midpoint) + pswd.substring(0,midpoint));
        encryptPswd.reverse();

        for(int i=0; i<pswdSize; ++i)
            encryptPswd.setCharAt(i, (char)(encryptPswd.charAt(i) & pswd.charAt(i)));

        hashCode = pswd.hashCode();
        encryptPswd.append(hashCode);
        return encryptPswd.toString();
    }
}