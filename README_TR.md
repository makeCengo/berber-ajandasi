# Berber Ajandası — Android APK kaynak projesi

Bu klasör, ilk sürümün Android Studio ile APK'ya dönüştürülebilecek kaynak projesidir. Bu ZIP'in içinde hazır APK yoktur; APK derlemesi Android Studio/Gradle ortamında yapılmalıdır.

## Neler var?
- Android uygulama kabuğu (Kotlin + Android WebView)
- Uygulama ekranları yerel `assets/index.html` dosyasından açılır; temel kullanım internet gerektirmez.
- Randevu ve müşteri kayıtları WebView yerel depolamasında tutulur.
- WhatsApp düğmeleri bağlantıyı WhatsApp'a veya uygun uygulamaya yönlendirir.
- Klasik, sade yeşil/krem arayüz.

## APK üretme (Android Studio)
1. Bilgisayara Android Studio kurun.
2. ZIP'i çıkartın.
3. Android Studio > **Open** ile `BerberAjandasi` klasörünü seçin.
4. Gradle eşitlemesi tamamlanınca **Build > Build Bundle(s) / APK(s) > Build APK(s)** seçin.
5. Başarılı derlemede Android Studio APK'nın konumunu gösterir. Genellikle `app/build/outputs/apk/debug/app-debug.apk` olur.
6. APK'yı telefona aktarın ve kurun. Telefon, dosya yöneticisi için “bilinmeyen uygulamaları yükleme” izni isteyebilir; yalnızca kendi oluşturduğunuz ve güvendiğiniz APK'yı yükleyin.

## Önemli sınırlar ve sonraki geliştirmeler
- Bu proje burada derlenmiş/test edilmiş bir APK değildir. Android SDK/Gradle kurulu bir ortamda derlenmeli ve gerçek telefonda test edilmelidir.
- Uygulama şu an çevrimdışı randevu takvimi sunar. Hatırlatma, uygulama açıkken kontrol edilir; uygulama kapalıyken yerel Android bildirimi henüz uygulanmamıştır.
- JSON yedekleme/geri yükleme ilk sürümde vardır; yedek dosyasını güvenli yerde tutun.
- Kayıtlar cihazda saklanır. Uygulamayı kaldırmak veya uygulama verilerini temizlemek kayıtları silebilir. Düzenli yedek alın.
- WhatsApp mesajları otomatik okunmaz veya kendiliğinden gönderilmez; kullanıcı bağlantıya dokunur ve mesajı WhatsApp'ta kontrol eder.
- Bu sürümde Google Play'e yayımlama, imzalı dağıtım APK'sı ve otomatik zamanlanmış bildirimler yapılmamıştır.
