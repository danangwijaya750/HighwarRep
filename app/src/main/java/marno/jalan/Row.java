package marno.jalan;

/**
 * Created by marno on 2/24/2017.
 */
public class Row {
    private String judul_Laporan;
    private String url_Gambar;
    private String alamat;
    private String Setatus;
    private String longit;
    private String latit;
    private String jen;
    private String id;

    public Row(String judul, String urlGambar, String alamat, String setat, String longi, String lat, String jenis, String idl){
        this.judul_Laporan = judul;
        this.url_Gambar = urlGambar;
        this.alamat = alamat;
        this.Setatus = setat;
        this.longit = longi;
        this.latit = lat;
        this.jen=jenis;
        this.id=idl;

    }




    public void setNamaTempat(String namaTempat){
        this.judul_Laporan = namaTempat;
    }
    public void setId(String idlap){
        this.id=idlap;
    }
    public String getId(){
        return id;
    }

    public String getJudulLaporan(){
        return judul_Laporan;
    }
    public void setUrlGambar(String urlGambar){
        this.url_Gambar = urlGambar;
    }
    public String getUrlGambar(){
        return url_Gambar;
    }
    public void setAlamat(String alamat){
        this.alamat = alamat;
    }
    public String getAlamat(){
        return alamat;
    }

    public void setSetatus(String setatuss){
        this.Setatus = setatuss;
    }
    public String getSetatus(){
        return Setatus;
    }
    public void setlong(String longg){
        this.longit = longg;
    }
    public String getlong(){
        return longit;
    }
    public void setlat(String lati){
        this.latit=lati;
    }

    public String getlat () {
        return latit;

    }
    public String getJen(){
        return jen;
    }
    public void setJen(String jeni){
        this.jen=jeni;
    }
}
