package org.example;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.tartarus.snowball.ext.PorterStemmer;

public class Main {

    // AFINN
    private static final Map<String, Integer> sentimentDictionary = new HashMap<>();

    static {
        sentimentDictionary.put("good", 2);
        sentimentDictionary.put("great", 3);
        sentimentDictionary.put("happy", 2);
        sentimentDictionary.put("love", 3);
        sentimentDictionary.put("excellent", 3);
        sentimentDictionary.put("awesome", 3);
        //(Stack Overflow için)
        sentimentDictionary.put("efficient", 2);
        sentimentDictionary.put("clean", 2);
        sentimentDictionary.put("scalable", 2);

        sentimentDictionary.put("bad", -2);
        sentimentDictionary.put("terrible", -3);
        sentimentDictionary.put("sad", -2);
        sentimentDictionary.put("hate", -3);
        sentimentDictionary.put("awful", -3);
        sentimentDictionary.put("worst", -3);
        // Domain negatif kelimeler
        sentimentDictionary.put("buggy", -2);
        sentimentDictionary.put("error", -2);
        sentimentDictionary.put("crash", -3);
        sentimentDictionary.put("slow", -2);
    }

    // Snowball PorterStemmer nesnesi
    private static final PorterStemmer stemmer = new PorterStemmer();

    //Tokenize edip stemming uyguladıktan sonra kelime sözlüğünden puan topla
    private static String analyzeSentiment(String text) {
        String[] tokens = text.split("\\W+");
        int score = 0;
        for (String token : tokens) {
            if (token.isEmpty()) continue;
            String lowerToken = token.toLowerCase();
            stemmer.setCurrent(lowerToken);
            stemmer.stem();
            String stemmed = stemmer.getCurrent();
            score += sentimentDictionary.getOrDefault(stemmed, 0);
        }
        if (score > 0) {
            return "Pozitif";
        } else if (score < 0) {
            return "Negatif";
        } else {
            return "Nötr";
        }
    }

    public static void main(String[] args) {
        final String queryTag = "ChatGPT";
        final int maxPosts = 1600;
        final int pageSize = 100;
        int currentPage = 1;

        OkHttpClient client = new OkHttpClient();
        int postCount = 0;
        int positiveCount = 0;
        int negativeCount = 0;
        int neutralCount = 0;

        //Toplam 1600 post veya 16 sayfa içersinde 100 olmak üzere çekene kadar döngü
        while (postCount < maxPosts && currentPage <= 16) {
            String url = "https://api.stackexchange.com/2.2/questions?order=desc&sort=activity" +
                    "&tagged=" + queryTag +
                    "&site=stackoverflow" +
                    "&pagesize=" + pageSize +
                    "&page=" + currentPage;

            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.out.println("İstek başarısız: " + response);
                    break;
                }
                String jsonResponse;
                if (response.body() != null) {
                    jsonResponse = response.body().string();
                } else {
                    System.err.println("Body boş döndü");
                    continue;
                }
                JSONObject json = new JSONObject(jsonResponse);

                // API backoff veriyorsa bekle
                if (json.has("backoff")) {
                    int backoffSeconds = json.getInt("backoff");
                    System.out.println("Dikkat: API istek limitine ulaşıldı. Lütfen " + backoffSeconds + " saniye bekleyin.");
                    Thread.sleep(((long) backoffSeconds) * 1000);
                }

                JSONArray items = json.getJSONArray("items");
                for (int i = 0; i < items.length() && postCount < maxPosts; i++) {
                    JSONObject question = items.getJSONObject(i);
                    String title = question.getString("title");
                    String sentiment = analyzeSentiment(title);
                    if (sentiment.equals("Pozitif")) {
                        positiveCount++;
                    } else if (sentiment.equals("Negatif")) {
                        negativeCount++;
                    } else {
                        neutralCount++;
                    }
                    postCount++;
                }
                System.out.println("Toplam alınan post sayısı: " + postCount);

                if (!json.getBoolean("has_more")) {
                    break;
                }
                currentPage++;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        System.out.println("Analiz Edilen Toplam Post: " + postCount);
        System.out.println("Pozitif: " + positiveCount);
        System.out.println("Negatif: " + negativeCount);
        System.out.println("Nötr: " + neutralCount);

        //JFreeChart ile görselleştir
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(positiveCount, "Duygu", "Pozitif");
        dataset.addValue(negativeCount, "Duygu", "Negatif");
        dataset.addValue(neutralCount, "Duygu", "Nötr");

        JFreeChart barChart = ChartFactory.createBarChart(
                "Duygu Analizi",
                "Duygu Türü",
                "Post Sayısı",
                dataset);

        ChartFrame chartFrame = new ChartFrame("Sentimental Analysis Sonuçları", barChart);
        chartFrame.setSize(800, 600);
        chartFrame.setVisible(true);
    }
}
