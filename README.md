# Stack Overflow Duygu Analizi Gelişmiş

Bu proje, Stack Overflow'dan belirli bir etiketle sorgulanan soruları çeken, gelişmiş duygu analizi gerçekleştiren ve JFreeChart kullanarak sonuçları görselleştiren bir Java projesidir.

## Genel Bakış

Proje, Stack Exchange API'sini kullanarak Stack Overflow'dan berirlenen miktarda  (1600 adet post (soru)) getirir. Her soru başlığının duygu durumu, özel bir duygu sözlüğü ve PorterStemmer kullanılarak analiz edilir. Sonuçlar, pozitif, negatif ve nötr post sayılarını gösteren bir bar grafiğinde sunulur.

## Özellikler

- **Veri Çekme:** Belirli bir etiket (örneğin, `java`) ile Stack Overflow'dan soruları getirir.
- **Gelişmiş Duygu Analizi:** Özel duygu sözlüğü ve PorterStemmer kullanarak kelimelerin köklerini bulur ve duygu analizi yapar.
- **Görselleştirme:** Analiz sonuçlarını JFreeChart kullanarak bar grafiği şeklinde görselleştirir.
- **API Rate Limit Yönetimi:** API yanıtında yer alan "backoff" yönergelerini uygulayarak istek limitlerine uyar.

## Kullanılan Teknolojiler

- **Java 8+**
- **OkHttp:** HTTP istekleri yapmak için.
- **org.json:** JSON yanıtlarını ayrıştırmak için.
- **JFreeChart:** Grafik oluşturmak için.
- **Snowball Stemmer (PorterStemmer):** Kelime köklerini bulmak için.
- **Stack Exchange API:** Stack Overflow'dan veri çekmek için.

## Kurulum

1. **Depoyu Klonlayın:**
   ```bash
   git clone https://github.com/kullanici-adiniz/sentimental-analysis-on-stack-overflow.git

2. **Proje Dizinine Geçin:**
    ```bash
    cd sentimental-analysis-on-stack-overflow

3. **Projeyi Derleyin: Maven yüklü olduğundan emin olun ve ardından:**
    ```bash
    mvn clean install

4. **Uygulamayı Çalıştırın:**
   ```bash
    mvn exec:java -Dexec.mainClass="org.example.Main"

5. **Sonuçları Görüntüleyin:**
Uygulama, verileri çekecek, duygu analizini yapacak ve sonuçları bir bar grafiği penceresinde gösterecektir.

**Katkıda Bulunma**

Katkılarınız memnuniyetle karşılanır ! Öneri, iyileştirme veya hata bildirimleri için lütfen bir issue açın veya pull request gönderin.

**İletişim**

Sorularınız veya geri bildirimleriniz için lütfen edizssenturk@gmail.com adresiyle iletişime geçin.

    

