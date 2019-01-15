package LoginRegister;

/**
 * Created by ryuze on 6/26/2018.
 */

public class Pengurus {

    public String userID;
    public String password;
    public String namaPengurus;
    public String gender;
    public String alamat;
    public String noTelepon;
    public String emailPengurus;

    public String getUserID(){return userID;}
    public void setUserID(String userID) {this.userID = userID;}

    public String getPassword(){return password;}
    public void setPassword(String password) {this.password = password;}

    public String getNamaPengurus(){return namaPengurus;}
    public void setNamaPengurus(String namaPengurus) {this.namaPengurus = namaPengurus;}

    public String getGender(){return gender;}
    public void setGender(String gender) {this.gender = gender;}

    public String getAlamat(){return alamat;}
    public void setAlamat(String alamat) {this.alamat = alamat;}

    public String getNoTelepon(){return noTelepon;}
    public void setNoTelepon(String noTelepon) {this.noTelepon = noTelepon;}

    public String getEmailPengurus(){return emailPengurus;}
    public void setEmailPengurus(String emailPengurus) {this.emailPengurus = emailPengurus;}
}
