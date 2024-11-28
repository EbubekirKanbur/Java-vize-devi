import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class BaseEntity {
    protected String id;

    public BaseEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

// Müşteri sınıfı
class Musteri extends BaseEntity {
    private String isim;
    private String salonAd;
    private String filmAd;
    private String saat;

    public Musteri(String id, String isim, String salonAd, String filmAd, String saat) {
        super(id);
        this.isim = isim;
        this.salonAd = salonAd;
        this.filmAd = filmAd;
        this.saat = saat;
    }

    public String getIsim() {
        return isim;
    }

    public String getSalonAd() {
        return salonAd;
    }

    public String getFilmAd() {
        return filmAd;
    }

    public String getSaat() {
        return saat;
    }

    @Override
    public String toString() {
        return "Müşteri ID: " + id + ", İsim: " + isim + ", Salon: " + salonAd + ", Film: " + filmAd + ", Saat: " + saat;
    }
}

// Film sınıfı
class Film {
    private String ad;
    private int sure; // Süre (dakika olarak)
    private String tur; // Tür
    private double ucret; // Ücret
    private List<String> saatler; // Saat bilgileri

    public Film(String ad, int sure, String tur, double ucret) {
        this.ad = ad;
        this.sure = sure;
        this.tur = tur;
        this.ucret = ucret;
        this.saatler = new ArrayList<>();
    }

    public String getAd() {
        return ad;
    }

    public int getSure() {
        return sure;
    }

    public String getTur() {
        return tur;
    }

    public double getUcret() {
        return ucret;
    }

    public List<String> getSaatler() {
        return saatler;
    }

    public void saatEkle(String saat) {
        saatler.add(saat);
    }

    @Override
    public String toString() {
        return "Film: " + ad + ", Süre: " + sure + " dk, Tür: " + tur + ", Ücret: " + ucret + " TL";
    }
}

// Salon sınıfı
class Salon extends BaseEntity {
    private String ad;
    private List<Film> filmler;
    private List<Musteri> musteriler;

    public Salon(String id, String ad) {
        super(id);
        this.ad = ad;
        this.filmler = new ArrayList<>();
        this.musteriler = new ArrayList<>();
    }

    public String getAd() {
        return ad;
    }

    public List<Film> getFilmler() {
        return filmler;
    }

    public List<Musteri> getMusteriler() {
        return musteriler;
    }

    public void filmEkle(Film film) {
        filmler.add(film);
    }

    // Saatin dolu olup olmadığını kontrol et (Başlangıç saati + Film süresi)
    public boolean saatDoluMu(String saat, int sure) {
        int saatBaslangic = toMinutes(saat); // Saatten dakika hesaplama
        int saatBitis = saatBaslangic + sure;

        // Salonun filmlerinin saat dilimlerini kontrol et
        for (Film film : filmler) {
            for (String filmSaat : film.getSaatler()) {
                int filmSaatBaslangic = toMinutes(filmSaat);
                int filmSaatBitis = filmSaatBaslangic + film.getSure();

                // Eğer saatler çakışıyorsa, salon doludur
                if ((saatBaslangic < filmSaatBitis) && (saatBitis > filmSaatBaslangic)) {
                    return true; // Saat çakışıyor, dolu
                }
            }
        }
        return false; // Saat boş
    }

    // Saat formatını dakikaya çevir (örn. 09:00 -> 540)
    private int toMinutes(String saat) {
        String[] parts = saat.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Salon: " + ad + ", Filmler:\n");
        for (Film film : filmler) {
            sb.append(film.getAd() + " (Saatler: ");
            for (String saat : film.getSaatler()) {
                sb.append(saat + " ");
            }
            sb.append(")\n");
        }
        sb.append("Müşteri sayısı: " + musteriler.size() + "\n");
        return sb.toString();
    }
}

// Ana sınıf
public class Main {
    private static List<Musteri> musteriler = new ArrayList<>();
    private static List<Salon> salonlar = new ArrayList<>();
    private static int musteriID = 1; // Otomatik müşteri ID'si

    public static void main(String[] args) {
        salonlariOncedenEkle();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Sinema Yönetim Sistemi ---");
            System.out.println("1. Müşteri Ekle");
            System.out.println("2. Film Ekle");
            System.out.println("3. Salonları Listele");
            System.out.println("4. Çıkış");
            System.out.print("Seçiminizi yapın: ");
            int secim = scanner.nextInt();
            scanner.nextLine(); // Satır sonunu temizle

            switch (secim) {
                case 1:
                    musteriEkle(scanner);
                    break;
                case 2:
                    filmEkle(scanner);
                    break;
                case 3:
                    salonlariListele();
                    break;
                case 4:
                    System.out.println("Çıkış yapılıyor...");
                    return;
                default:
                    System.out.println("Geçersiz seçim. Tekrar deneyin.");
            }
        }
    }

    private static void musteriEkle(Scanner scanner) {
        System.out.print("Müşteri İsmi: ");
        String isim = scanner.nextLine();

        System.out.println("Hangi salonu seçmek istersiniz?");
        for (int i = 0; i < salonlar.size(); i++) {
            System.out.println((i + 1) + ". " + salonlar.get(i).getAd());
        }
        int salonSecim = scanner.nextInt();
        scanner.nextLine(); // Satır sonunu temizle

        if (salonSecim < 1 || salonSecim > salonlar.size()) {
            System.out.println("Geçersiz seçim.");
            return;
        }

        Salon secilenSalon = salonlar.get(salonSecim - 1);
        System.out.println("Seçtiğiniz salon: " + secilenSalon.getAd());

        System.out.println("Bu salonda oynatılan filmler:");
        for (int i = 0; i < secilenSalon.getFilmler().size(); i++) {
            Film film = secilenSalon.getFilmler().get(i);
            System.out.println((i + 1) + ". " + film.getAd() + " - Saatler: " + String.join(", ", film.getSaatler()));
        }

        System.out.print("Film seçiminizi yapın (film numarası): ");
        int filmSecim = scanner.nextInt();
        scanner.nextLine(); // Satır sonunu temizle

        if (filmSecim < 1 || filmSecim > secilenSalon.getFilmler().size()) {
            System.out.println("Geçersiz film seçimi.");
            return;
        }

        Film secilenFilm = secilenSalon.getFilmler().get(filmSecim - 1);

        // Müşteri için ID oluştur
        String id = "M-" + musteriID++;
        Musteri musteri = new Musteri(id, isim, secilenSalon.getAd(), secilenFilm.getAd(), secilenFilm.getSaatler().get(0));

        // Müşteri salonuna ekleniyor
        musteriler.add(musteri);
        secilenSalon.getMusteriler().add(musteri);

        System.out.println("Müşteri kaydedildi! Müşteri ID: " + musteri.getId() + " - " + secilenFilm.getAd() + " filmi, " + secilenFilm.getSaatler().get(0) + " saatinde oynayacak.");
    }

    private static void filmEkle(Scanner scanner) {
        System.out.print("Film Adı: ");
        String ad = scanner.nextLine();
        System.out.print("Film Süresi (dakika): ");
        int sure = scanner.nextInt();
        scanner.nextLine(); // Satır sonunu temizle
        System.out.print("Film Türü: ");
        String tur = scanner.nextLine();
        System.out.print("Film Ücreti: ");
        double ucret = scanner.nextDouble();
        scanner.nextLine(); // Satır sonunu temizle

        Film film = new Film(ad, sure, tur, ucret);

        System.out.println("Hangi salonu seçmek istersiniz?");
        for (int i = 0; i < salonlar.size(); i++) {
            System.out.println((i + 1) + ". " + salonlar.get(i).getAd());
        }
        int salonSecim = scanner.nextInt();
        scanner.nextLine(); // Satır sonunu temizle

        if (salonSecim < 1 || salonSecim > salonlar.size()) {
            System.out.println("Geçersiz seçim.");
            return;
        }

        Salon secilenSalon = salonlar.get(salonSecim - 1);

        // Film için saat seçimi
        while (true) {
            System.out.print("Filmin başlangıç saatini girin (Örn: 09:00): ");
            String saat = scanner.nextLine();

            // Saatin dolu olup olmadığını kontrol et
            if (secilenSalon.saatDoluMu(saat, film.getSure())) {
                System.out.println("Bu saat dolu. Başka bir saat seçin.");
            } else {
                film.saatEkle(saat);
                break;
            }
        }

        secilenSalon.filmEkle(film);
        System.out.println("Film başarıyla eklendi. " + secilenSalon.getAd() + " salonunda oynatılacak.");
    }

    private static void salonlariListele() {
        System.out.println("\n--- Salonlar ---");
        for (Salon salon : salonlar) {
            System.out.println(salon);
        }
    }

    private static void salonlariOncedenEkle() {
        salonlar.add(new Salon("1", "Salon 1"));
        salonlar.add(new Salon("2", "Salon 2"));
    }
}
