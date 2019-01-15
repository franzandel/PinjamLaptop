package LoginRegister;

/**
 * Created by OK on 4/9/2018.
 */

public class Pengembalian {
    private String kd_Pinjam;
    private String kd_Inventaris;
    private String tgl_Kembali;
    private String status_Kembali;

    private String tgl_Pinjam;
    private String flowKembali;

    public Pengembalian() {

    }

    public Pengembalian(Pengembalian pengembalian) {
        this.kd_Pinjam = pengembalian.getKd_Pinjam();
        this.kd_Inventaris = pengembalian.getKd_Inventaris();
        this.tgl_Kembali = pengembalian.getTgl_Kembali();
        this.status_Kembali = pengembalian.getStatus_Kembali();
        this.tgl_Pinjam = pengembalian.getTgl_Pinjam();
        this.flowKembali = pengembalian.getFlowKembali();
    }

    public String getKd_Pinjam() {
        return kd_Pinjam;
    }

    public void setKd_Pinjam(String kd_Pinjam) {
        this.kd_Pinjam = kd_Pinjam;
    }

    public String getKd_Inventaris() {
        return kd_Inventaris;
    }

    public void setKd_Inventaris(String kd_Inventaris) {
        this.kd_Inventaris = kd_Inventaris;
    }

    public String getTgl_Kembali() {
        return tgl_Kembali;
    }

    public void setTgl_Kembali(String tgl_Kembali) {
        this.tgl_Kembali = tgl_Kembali;
    }

    public String getStatus_Kembali() {
        return status_Kembali;
    }

    public void setStatus_Kembali(String status_Kembali) {
        this.status_Kembali = status_Kembali;
    }

    public String getTgl_Pinjam() {
        return tgl_Pinjam;
    }

    public void setTgl_Pinjam(String tgl_Pinjam) {
        this.tgl_Pinjam = tgl_Pinjam;
    }

    public String getFlowKembali() {
        return flowKembali;
    }

    public void setFlowKembali(String flowKembali) {
        this.flowKembali = flowKembali;
    }
}
