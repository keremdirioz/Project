// Main.java — Students version
import java.io.*;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
   static {
       loadData();
    }
    public static int[][][] allData = new int[MONTHS][DAYS][COMMS];

    public static int getCommodityIndex(String commodity) {
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(commodity)){
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

                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
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
            } catch (FileNotFoundException e) {
            }catch (NumberFormatException e){
            }

        }
    }



    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 ||  month >= MONTHS) {
            return  "INVALID_MONTH";
        }
        int maxProfit = Integer.MIN_VALUE;
        String bestCommName = ",";

        for(int c=0; c<COMMS; c++){
            int currentCommTotal = 0;
            //o aydaki toplam karı hesaplama
            for (int d= 0; d<DAYS; d++){
                currentCommTotal +=allData[month][d][c];
            }
            if (currentCommTotal> maxProfit){
                maxProfit=currentCommTotal;
                bestCommName=commodities[c];
            }
        }
        return bestCommName + " " + maxProfit;

    }

    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month>= MONTHS || day<1 || day>DAYS ) {
            return -99999; // exception almak yasak olduğu için negatif büyük bir sayı aldım bu sayede kar ile karşılaşaştırılmaz.
        }
        int totalProfit= 0; // günlük total karı burda biriktiriyorum.
        for(int c=0; c<COMMS;c++){ // belirli gün için emtiaların dğerlerine bakıyorum
            totalProfit += allData[month][day - 1][c];
        }
        return totalProfit; // tüm emtiaların total karına burda bakıyorum
    }

public static int commodityProfitInRange(String commodity, int from, int to) {
        int commIndex= getCommodityIndex(commodity); // emtia adını dzideki index numaasına döndür
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
        if(commIndex==-1){
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

          // 5) Bu ay daha iyiyse "en iyi ay" olarak güncelle
            if (monthProfit > bestMonthProfit) {
                bestMonthProfit = monthProfit;
                bestMonthIndex = m;
            }
        }

    }
    // 6) Sonucu döndür: ay adı + toplam kâr
        return months[bestMonthIndex] + " " + bestMonthProfit;
    }

    public static int consecutiveLossDays(String comm) {
        int commIndex = getCommodityIndex(comm);
        if (commIndex == -1) {
            return -99999;
        }
        int longestStreak = 0;
        int currentStreak = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                int profitToday=allData[d][m][commIndex];
                if (profitToday<0){// negatif bir sayıysa seriyi uzatır
                    currentStreak++;
                }
                if (currentStreak>longestStreak){
                    currentStreak=longestStreak;
                }else{
                    currentStreak=0;
                }
            }
        }
        return longestStreak;
    }
    
    public static int daysAboveThreshold(String comm, int threshold) { 
        return 1234; 
    }

    public static int biggestDailySwing(int month) { 
        return 1234; 
    }
    
    public static String compareTwoCommodities(String c1, String c2) { 
        return "DUMMY is better by 1234"; 
    }
    
    public static String bestWeekOfMonth(int month) { 
        return "DUMMY"; 
    }

    public static void main(String[] args) {
       loadData();
        System.out.println("Data loaded – ready for queries");
    }
}