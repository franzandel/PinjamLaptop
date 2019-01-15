package LoginRegister;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by OK on 4/9/2018.
 */

public class Peminjaman {
    private String kd_Pinjam;
    private String no_Anggota;
    private String tgl_Pinjam;
    private String no_Laptop;
    private int qty_Pinjam;
    private String keperluan;
    private ArrayList<HashMap<String,String>> list;

    private String kd_Inventaris;
    private String status;

    public String getKd_Pinjam() {
        return kd_Pinjam;
    }

    public void setKd_Pinjam(String kd_Pinjam) {
        this.kd_Pinjam = kd_Pinjam;
    }

    public String getNo_Anggota() {
        return no_Anggota;
    }

    public void setNo_Anggota(String no_Anggota) {
        this.no_Anggota = no_Anggota;
    }

    public String getTgl_Pinjam() {
        return tgl_Pinjam;
    }

    public void setTgl_Pinjam(String tgl_Pinjam) {
        this.tgl_Pinjam = tgl_Pinjam;
    }

    public String getNo_Laptop() {
        return no_Laptop;
    }

    public void setNo_Laptop(String no_Laptop) {
        this.no_Laptop = no_Laptop;
    }

    public int getQty_Pinjam() {
        return qty_Pinjam;
    }

    public void setQty_Pinjam(int qty_Pinjam) {
        this.qty_Pinjam = qty_Pinjam;
    }

    public String getKeperluan() {
        return keperluan;
    }

    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }

    public ArrayList<HashMap<String,String>> getListLaptop() {
        return list;
    }

    public void setListLaptop(ArrayList<HashMap<String,String>> list) {
        this.list = list;
    }

    public String getKd_Inventaris() {
        return kd_Inventaris;
    }

    public void setKd_Inventaris(String kd_Inventaris) {
        this.kd_Inventaris = kd_Inventaris;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
