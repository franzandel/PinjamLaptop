package BottomNavigation;

/**
 * Created by OK on 5/3/2018.
 */

public class Config {
    /*public static final String URL_GET_ANGGOTA =
            "http://10.0.2.2/Pinjam_Laptop(Local)/get_anggota.php";
    public static final String URL_GET_M_LAPTOP =
            "http://10.0.2.2/Pinjam_Laptop(Local)/get_m_laptop.php";
    public static final String URL_GET_INVENTARIS =
            "http://10.0.2.2/Pinjam_Laptop(Local)/get_inventaris.php?tgl_pinjam='";
    public static final String URL_ADD_M_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Local)/add_m_pinjam.php";
    public static final String URL_ADD_P_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Local)/add_p_pinjam.php";
    public static final String URL_ADD_INVENTARIS =
            "http://10.0.2.2/Pinjam_Laptop(Local)/add_inventaris.php";
    public static final String URL_ADD_KEMBALI =
            "http://10.0.2.2/Pinjam_Laptop(Local)/add_kembali.php";
    public static final String URL_ADD_QTY_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Local)/add_qty_pinjam.php";
    public static final String URL_UPDATE_M_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Local)/update_m_pinjam.php";
    public static final String URL_UPDATE_INVENTARIS =
            "http://10.0.2.2/Pinjam_Laptop(Local)/update_inventaris.php";
    public static final String URL_DELETE_QTY_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Local)/delete_qty_pinjam.php";
    public static final String URL_VIEW_M_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Local)/view_m_pinjam.php";
    public static final String  URL_VIEW_P_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Local)/view_p_pinjam.php?kd_pinjam='";
    public static final String URL_VIEW_KEMBALI_HOME =
            "http://10.0.2.2/Pinjam_Laptop(Local)/view_kembali_home.php";
    public static final String  URL_VIEW_KEMBALI =
            "http://10.0.2.2/Pinjam_Laptop(Local)/view_kembali.php?kd_pinjam='";
    public static final String URL_DELETE_M_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Local)/delete_m_pinjam.php?kd_pinjam='";
    public static final String URL_DELETE_P_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Local)/delete_p_pinjam.php?kd_pinjam='";
    public static final String URL_DELETE_KEMBALI =
            "http://10.0.2.2/Pinjam_Laptop(Local)/delete_kembali.php?kd_pinjam='";
    public static final String URL_DELETE_TGL_KEMBALI =
            "http://10.0.2.2/Pinjam_Laptop(Local)/delete_tgl_kembali.php?tgl_pinjam='";
    public static final String URL_DELETE_INVENTARIS =
            "http://10.0.2.2/Pinjam_Laptop(Local)/delete_inventaris.php?tgl_pinjam='";*/

    /*public static final String URL_GET_ANGGOTA =
            "http://timalamb.online/android_server_pinjamlaptop/get_anggota.php";*/

    //URL Master
    public static final String URL_ADD_MEREK =
            "http://10.0.2.2/Pinjam_Laptop(Online)/add_merek.php";
    public static final String URL_VIEW_ALL_MEREK =
            "http://10.0.2.2/Pinjam_Laptop(Online)/view_all_merek.php";
    public static final String URL_UPDATE_MEREK =
            "http://10.0.2.2/Pinjam_Laptop(Online)/update_merek.php?";
    public static final String URL_DELETE_MEREK =
            "http://10.0.2.2/Pinjam_Laptop(Online)/delete_merek.php?kd_merek='";
    public static final String URL_ADD_PROCESSOR =
            "http://10.0.2.2/Pinjam_Laptop(Online)/add_processor.php";
    public static final String URL_VIEW_ALL_PROCESSOR =
            "http://10.0.2.2/Pinjam_Laptop(Online)/view_all_processor.php";
    public static final String URL_UPDATE_PROCESSOR =
            "http://10.0.2.2/Pinjam_Laptop(Online)/update_processor.php?";
    public static final String URL_DELETE_PROCESSOR =
            "http://10.0.2.2/Pinjam_Laptop(Online)/delete_processor.php?kd_processor='";
    public static final String URL_ADD_VGA =
            "http://10.0.2.2/Pinjam_Laptop(Online)/add_vga.php";
    public static final String URL_VIEW_ALL_VGA =
            "http://10.0.2.2/Pinjam_Laptop(Online)/view_all_vga.php";
    public static final String URL_UPDATE_VGA =
            "http://10.0.2.2/Pinjam_Laptop(Online)/update_vga.php?";
    public static final String URL_DELETE_VGA =
            "http://10.0.2.2/Pinjam_Laptop(Online)/delete_vga.php?kd_vga='";
    public static final String URL_ADD_ANGGOTA =
            "http://10.0.2.2/Pinjam_Laptop(Online)/add_anggota.php";
    public static final String URL_VIEW_ALL_ANGGOTA =
            "http://10.0.2.2/Pinjam_Laptop(Online)/view_all_anggota.php";
    public static final String URL_UPDATE_ANGGOTA =
            "http://10.0.2.2/Pinjam_Laptop(Online)/update_anggota.php";
    public static final String URL_DELETE_ANGGOTA =
            "http://10.0.2.2/Pinjam_Laptop(Online)/delete_anggota.php?no_anggota='";
    public static final String URL_VIEW_ALL_LAPTOP =
            "http://10.0.2.2/Pinjam_Laptop(Online)/view_all_laptop.php";
    public static final String URL_DELETE_M_LAPTOP =
            "http://10.0.2.2/Pinjam_Laptop(Online)/delete_m_laptop.php?no_laptop='";
    public static final String URL_ADD_M_LAPTOP =
            "http://10.0.2.2/Pinjam_Laptop(Online)/add_m_laptop.php";
    public static final String URL_ADD_P_LAPTOP =
            "http://10.0.2.2/Pinjam_Laptop(Online)/add_p_laptop.php";
    public static final String URL_UPDATE_M_LAPTOP =
            "http://10.0.2.2/Pinjam_Laptop(Online)/update_m_laptop.php";
    public static final String URL_UPDATE_P_LAPTOP =
            "http://10.0.2.2/Pinjam_Laptop(Online)/update_p_laptop.php";

    public static final String URL_GET_M_LAPTOP =
            "http://10.0.2.2/Pinjam_Laptop(Online)/get_m_laptop.php";
    public static final String URL_GET_INVENTARIS =
            "http://10.0.2.2/Pinjam_Laptop(Online)/get_inventaris.php?tgl_pinjam='";
    public static final String URL_ADD_M_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Online)/add_m_pinjam.php";
    public static final String URL_ADD_P_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Online)/add_p_pinjam.php";
    public static final String URL_ADD_INVENTARIS =
            "http://10.0.2.2/Pinjam_Laptop(Online)/add_inventaris.php";
    public static final String URL_ADD_KEMBALI =
            "http://10.0.2.2/Pinjam_Laptop(Online)/add_kembali.php";
    public static final String URL_ADD_QTY_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Online)/add_qty_pinjam.php";
    public static final String URL_UPDATE_M_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Online)/update_m_pinjam.php";
    public static final String URL_UPDATE_INVENTARIS =
            "http://10.0.2.2/Pinjam_Laptop(Online)/update_inventaris.php";
    public static final String URL_DELETE_QTY_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Online)/delete_qty_pinjam.php";
    public static final String  URL_VIEW_EACH_ANGGOTA =
            "http://10.0.2.2/Pinjam_Laptop(Online)/view_each_anggota.php?no_anggota='";
    public static final String URL_VIEW_M_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Online)/view_m_pinjam.php";
    public static final String  URL_VIEW_P_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Online)/view_p_pinjam.php?kd_pinjam='";
    public static final String URL_VIEW_ALL_P_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Online)/view_all_p_pinjam.php";
    public static final String URL_VIEW_KEMBALI_HOME =
            "http://10.0.2.2/Pinjam_Laptop(Online)/view_kembali_home.php";
    public static final String  URL_VIEW_KEMBALI =
            "http://10.0.2.2/Pinjam_Laptop(Online)/view_kembali.php?kd_pinjam='";
    public static final String URL_DELETE_M_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Online)/delete_m_pinjam.php?kd_pinjam='";
    public static final String URL_DELETE_P_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Online)/delete_p_pinjam.php?kd_pinjam='";
    public static final String URL_DELETE_KEMBALI =
            "http://10.0.2.2/Pinjam_Laptop(Online)/delete_kembali.php?kd_pinjam='";
    public static final String URL_DELETE_TGL_KEMBALI =
            "http://10.0.2.2/Pinjam_Laptop(Online)/delete_tgl_kembali.php?tgl_pinjam='";
    public static final String URL_DELETE_INVENTARIS =
            "http://10.0.2.2/Pinjam_Laptop(Online)/delete_inventaris.php?tgl_pinjam='";
    public static final String URL_VALIDASI_QTY_PINJAM =
            "http://10.0.2.2/Pinjam_Laptop(Online)/validasi_qty_pinjam.php";
    public static final String URL_VIEW_EACH_P_LAPTOP =
            "http://10.0.2.2/Pinjam_Laptop(Online)/view_p_laptop.php?no_laptop='";
    public static final String URL_VALIDASI_MEREK =
            "http://10.0.2.2/Pinjam_Laptop(Online)/validasi_merek.php?kd_merek='";
    public static final String URL_VALIDASI_PROCESSOR =
            "http://10.0.2.2/Pinjam_Laptop(Online)/validasi_processor.php?kd_processor='";
    public static final String URL_VALIDASI_VGA =
            "http://10.0.2.2/Pinjam_Laptop(Online)/validasi_vga.php?kd_vga='";
    public static final String URL_VALIDASI_ANGGOTA =
            "http://10.0.2.2/Pinjam_Laptop(Online)/validasi_anggota.php?no_anggota='";
    public static final String URL_VALIDASI_LAPTOP =
            "http://10.0.2.2/Pinjam_Laptop(Online)/validasi_laptop.php?no_laptop='";


    public static final String KEY_KD_PINJAM = "KD_PINJAM";
    public static final String KEY_NO_ANGGOTA = "NO_ANGGOTA";
    public static final String KEY_TGL_PINJAM = "TGL_PINJAM";

    public static final String KEY_NO_LAPTOP = "NO_LAPTOP";
    public static final String KEY_QTY_PINJAM = "QTY_PINJAM";
    public static final String KEY_KEPERLUAN = "KEPERLUAN";

    public static final String KEY_KD_INVENTARIS = "KD_INVENTARIS";
    public static final String KEY_TGL_KEMBALI = "TGL_KEMBALI";
    public static final String KEY_STATUS = "STATUS";

    public static final String KEY_STATUS_KEMBALI = "STATUS_KEMBALI";

    //For Table Master
    public static final String KEY_KD_MEREK = "KD_MEREK";
    public static final String KEY_NM_MEREK = "NM_MEREK";

    public static final String KEY_KD_PROCESSOR = "KD_PROCESSOR";
    public static final String KEY_NM_PROCESSOR = "NM_PROCESSOR";

    public static final String KEY_KD_VGA = "KD_VGA";
    public static final String KEY_NM_VGA = "NM_VGA";
    public static final String KEY_RAM = "RAM";
    public static final String KEY_MODEL = "MODEL";
    public static final String KEY_HDD = "HDD";
    public static final String KEY_SATUAN = "SATUAN";
    public static final String KEY_QTY_TOTAL = "QTY_TOTAL";
    public static final String KEY_QTY_BAIK = "QTY_BAIK";
    public static final String KEY_QTY_RUSAK = "QTY_RUSAK";
    public static final String KEY_QTY_HILANG = "QTY_HILANG";

    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_PASSWORD = "PASSWORD";
    public static final String KEY_NM_PENGURUS = "NM_PENGURUS";
    public static final String KEY_GENDER = "GENDER";
    public static final String KEY_ALAMAT = "ALAMAT";
    public static final String KEY_NO_TELP = "NO_TELP";
    public static final String KEY_EMAIL_PENGURUS = "EMAIL_PENGURUS";

    public static final String KEY_NM_ANGGOTA = "NM_ANGGOTA";
    public static final String KEY_GENDER_ANGGOTA = "GENDER";
    public static final String KEY_ALAMAT_ANGGOTA = "ALAMAT";
    public static final String KEY_NO_TELP_ANGGOTA = "NO_TELP";
    public static final String KEY_EMAIL_ANGGOTA = "EMAIL_ANGGOTA";
    public static final String KEY_FOTO_ANGGOTA = "FOTO_ANGGOTA";

    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_KD_PINJAM = "KD_PINJAM";
    public static final String TAG_NO_ANGGOTA = "NO_ANGGOTA";
    public static final String TAG_NO_LAPTOP = "NO_LAPTOP";
    public static final String TAG_TGL_PINJAM = "TGL_PINJAM";

    public static final String TAG_QTY_PINJAM = "QTY_PINJAM";
    public static final String TAG_KEPERLUAN = "KEPERLUAN";

    public static final String TAG_KD_INVENTARIS = "KD_INVENTARIS";
    public static final String TAG_TGL_KEMBALI = "TGL_KEMBALI";
    public static final String TAG_STATUS_KEMBALI = "STATUS_KEMBALI";
    public static final String TAG_FLOW_KEMBALI = "FLOW_KEMBALI";

    public static final String TAG_QTY_TOTAL = "QTY_TOTAL";
    public static final String TAG_SATUAN = "SATUAN";
    public static final String TAG_QTY_BAIK = "QTY_BAIK";
    public static final String TAG_QTY_RUSAK = "QTY_RUSAK";
    public static final String TAG_QTY_HILANG = "QTY_HILANG";

    //----------------
    public static final String TAG_KD_MEREK = "KD_MEREK";
    public static final String TAG_NM_MEREK = "NM_MEREK";
    public static final String TAG_KD_PROCESSOR = "KD_PROCESSOR";
    public static final String TAG_NM_PROCESSOR = "NM_PROCESSOR";
    public static final String TAG_KD_VGA = "KD_VGA";
    public static final String TAG_NM_VGA = "NM_VGA";
    public static final String TAG_USER_ID = "USER_ID";
    public static final String TAG_PASSWORD = "PASSWORD";
    public static final String TAG_NM_PENGURUS = "NM_PENGURUS";
    public static final String TAG_GENDER = "GENDER";
    public static final String TAG_ALAMAT = "ALAMAT";
    public static final String TAG_NO_TELP = "NO_TELP";
    public static final String TAG_EMAIL_PENGURUS = "EMAIL_PENGURUS";
    public static final String TAG_NM_ANGGOTA = "NM_ANGGOTA";
    public static final String TAG_GENDER_ANGGOTA = "GENDER";
    public static final String TAG_ALAMAT_ANGGOTA = "ALAMAT";
    public static final String TAG_NO_TELP_ANGGOTA = "NO_TELP";
    public static final String TAG_EMAIL_ANGGOTA = "EMAIL_ANGGOTA";
    public static final String TAG_FOTO_ANGGOTA = "FOTO_ANGGOTA";
    public static final String TAG_MODEL = "MODEL";
    public static final String TAG_RAM = "RAM";
    public static final String TAG_HDD = "HDD";
}
