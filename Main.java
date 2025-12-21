// Main.java — Students version

import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========


    public static int[][][] allData = new int[MONTHS][DAYS][COMMS];

    static {
        loadData();
    }

    public static int getCommodityIndex(String commodity) {
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(commodity)) {
                return i;
            }
        }
        return -1;// geçersiz emtia olunca
    }

    public static void loadData() {
        for (int m = 0; m < MONTHS; m++) {
            String filename = "Data_Files/" + months[m] + ".txt";

            try {
                Scanner sc = new Scanner(new File(filename));
                boolean skipLine = true;
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (skipLine) {
                        skipLine = false;
                        continue;
                    }
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        int day = Integer.parseInt(parts[0].trim()) - 1;
                        String commName = parts[1].trim();
                        int commIndex = getCommodityIndex(commName);
                        int profit = Integer.parseInt(parts[2].trim());
                        if (day >= 0 && day < DAYS && commIndex != -1) {
                            allData[m][day][commIndex] = profit;
                        }
                    }
                }
                sc.close();
            } catch (Throwable e) {
                System.out.println(e.toString());

            }
        }
    }


    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month >= MONTHS) {
            return "INVALID_MONTH";
        }
        int maxProfit = Integer.MIN_VALUE;
        String bestCommName = ",";

        for (int c = 0; c < COMMS; c++) {
            int currentCommTotal = 0;
            //o aydaki toplam karı hesaplama
            for (int d = 0; d < DAYS; d++) {
                currentCommTotal += allData[month][d][c];
            }
            if (currentCommTotal > maxProfit) {
                maxProfit = currentCommTotal;
                bestCommName = commodities[c];
            }
        }
        return bestCommName + " " + maxProfit;

    }

    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month >= MONTHS || day < 1 || day > DAYS) {
            return -99999; // exception almak yasak olduğu için negatif büyük bir sayı aldım bu sayede kar ile karşılaşaştırılmaz.
        }
        int totalProfit = 0; // günlük total karı burda biriktiriyorum.
        for (int c = 0; c < COMMS; c++) { // belirli gün için emtiaların dğerlerine bakıyorum
            totalProfit += allData[month][day - 1][c];
        }
        return totalProfit; // tüm emtiaların total karına burda bakıyorum
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        int commIndex = getCommodityIndex(commodity); // emtia adını dzideki index numaasına döndür
        if (commIndex == -1 || from < 1 || to > DAYS || from > to) {
            return -99999;
        }

        int totalProfit = 0; // total kar değişkeni

        for (int m = 0; m < MONTHS; m++) {
            for (int d = from - 1; d <= to - 1; d++) { // -1 yapmamızın sebebi aylar 0 dan başlıyor
                totalProfit += allData[m][d][commIndex]; // belirli ay gün ve ürün için kaarı bulur
            }
        }
        return totalProfit;
        // Belirli bir emtianın gün aralığına göre toplam kârını hesaplayan metot eklendi
    }


    public static int bestDayOfMonth(int month) {
        if (month < 0 || month >= MONTHS) {
            return -1;
        }

        int bestDay = 1;
        int maxProfit = Integer.MIN_VALUE;

        for (int d = 0; d < DAYS; d++) {
            int dailyProfit = totalProfitOnDay(month, d + 1);

            if (dailyProfit > maxProfit) {
                maxProfit = dailyProfit;
                bestDay = d + 1;
            }
        }
        return bestDay;
    }


    public static String bestMonthForCommodity(String comm) { // geçerli emtia  mı?
        int commIndex = getCommodityIndex(comm);
        if (commIndex == -1) {
            return "INVALID_COMMODITY";
        }
        // 2) En iyi ayı bulmak için başlangıç değerleri
        int bestMonthIndex = 0;              // şimdilik Ocak (0) varsayıyoruz
        int bestMonthProfit = Integer.MIN_VALUE; // ilk ayı bilerek küçük sayı veriyorum ki sonrasında güncellensin
        // 3) 12 ayı tek tek gez
        for (int m = 0; m < MONTHS; m++) {
            int monthProfit = 0; // 0-11 arası tüm ayların karlarını brda biriktiriyoruz

            // 4) Bu ayın 28 gününü topla (sadece seçilen emtia için)
            for (int d = 0; d < DAYS; d++) {
                monthProfit += allData[m][d][commIndex];
            }
            // 5) Bu ay daha iyiyse "en iyi ay" olarak güncelle
            if (monthProfit > bestMonthProfit) {
                bestMonthProfit = monthProfit;
                bestMonthIndex = m;
            }

        }
        // 6) Sonucu döndür: ay adı + toplam kâr
        return months[bestMonthIndex] + " " + bestMonthProfit;
    }

    public static int consecutiveLossDays(String comm) {
        int commIndex = getCommodityIndex(comm);
        if (commIndex == -1) {
            return 0;
        }
        int longestStreak = 0;
        int currentStreak = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                int profitToday = allData[m][d][commIndex];
                if (profitToday < 0) {// negatif bir sayıysa seriyi uzatır
                    currentStreak++;
                }
                if (currentStreak > longestStreak) {
                    longestStreak = currentStreak;
                } else {
                    currentStreak = 0;
                }
            }
        }
        return longestStreak;
    }

    public static int daysAboveThreshold(String comm, int threshold) {
        int commIndex = getCommodityIndex(comm);
        if (commIndex == -1) {
            return 0;    // emtia yoksa sıfıra döndür
        }
        int dayCount = 0; // eşik değeri geçen günleri burda biriktirir
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (allData[m][d][commIndex] > threshold) { // seçilen emtianın değerlerine bakılır eşik değer üzerinde ise gün geeçerli sauılır
                    dayCount++;
                }
            }
        }
        return dayCount;
    }

    public static int biggestDailySwing(int month) { // birgünden ertesi güne olan fark
        int maxSwing = 0; // en büyük farkı tutacak değişken
        for (int c = 0; c < COMMS; c++) {
            for (int d = 0; d < DAYS - 1; d++) { // günleri gezerken 28 den fazla gezmemesi için -1 yapıyorz
                int today = allData[month][d][c]; // emtianın değerine burda bakıyroruz
                int nextDay = allData[month][d + 1][c]; // aynı emtianın  bir sonraki günki değerine burda bakıyroruz
                int swing;
// iki gün arasındaki değerleri mutlak değer içinde birbirinden çıkarıyoruz
                if (nextDay >= today) {
                    swing = nextDay - today;
                } else {
                    swing = today - nextDay;
                }
                if (swing > maxSwing) {
                    maxSwing = swing;
                }
            }
        }
        return maxSwing;
    }

    public static String compareTwoCommodities(String c1, String c2) { // 2 emtianıyı kıyaslama methodu
        int index1 = getCommodityIndex(c1); // string olan emtiaları arraylara döndürüyoruz
        int index2 = getCommodityIndex(c2);
        if (index1 == -1 || index2 == -1) { // 2 tane emtia var mı diye kontrol ediyoruz yoksa kıyaslama olmaz
            return "INVALID_COMMODITY";
        }
        long total1 = 0; // yıl boyu kazancı burda biriktiriyoruz
        long total2 = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                total1 += allData[m][d][index1];// seçilen emtianın aygün ve yıl değerlini topluyoryz
                total2 += allData[m][d][index2];
            }
        }
        if (total1 > total2) {
            return c1 + "PERFORMED_BETTER";
        } else if (total2 > total1) {
            return c2 + "PERFORMED_BETTER";
        } else {
            return "BOTH_PERFORMED_SAME";
        }
    }

    public static String bestWeekOfMonth(int month) {
        if (month >= MONTHS) {
            return "DUMMY";

        }
        int bestWeek = 0; // başlangıçta en iyi haftayı 1. hafta seçiyoruz sonra güncellenecek
        int maxTotal = Integer.MIN_VALUE; // karşılaştırma yapabilmek için çok küçük bir değerle başlanır
        for (int w = 0; w < 4; w++) {
            int weekTotal = 0;
            for (int d = w * 7; d < w * 7 + 7; d++) {
                for (int c = 0; c < COMMS; c++) {
                    weekTotal += allData[month][d][c];
                }
            }
            if (weekTotal > maxTotal) { // hafta kontorlü önceki eniyi hafta ile kıyaslanıyor
                maxTotal = weekTotal;
                bestWeek = w + 1; // w de 0 dan başlandığından kafa karışmasın diye
            }
        }
        return bestWeek + "BEST_WEEK_IS_THIS_WEEK";
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
}
